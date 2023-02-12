package club.tesseract.rpgteleporting.gui

import club.tesseract.rpgteleporting.RPGTeleporting
import club.tesseract.rpgteleporting.config.ConfigManager
import club.tesseract.rpgteleporting.config.NoPermissionType
import club.tesseract.rpgteleporting.config.impl.GuiItemConfig
import club.tesseract.rpgteleporting.config.impl.OutpostConfig
import club.tesseract.rpgteleporting.config.impl.RandomLocationConfig
import club.tesseract.rpgteleporting.config.impl.TeleportMenuConfig
import club.tesseract.rpgteleporting.utils.ComponentUtils.format
import club.tesseract.rpgteleporting.utils.ComponentUtils.toMini
import club.tesseract.rpgteleporting.utils.InventoryUtils.calculateInventorySize
import fr.mrmicky.fastinv.FastInv
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import java.util.concurrent.atomic.AtomicInteger


class TeleportGui(currentOutPost: String,val guiConfig: TeleportMenuConfig):
    FastInv(
        calculateInventorySize(guiConfig.guiItems.size-1),
        guiConfig.title.format(Placeholder.component("outpost_name", ConfigManager.getOutpost(currentOutPost)?.name?: "Unknown".toMini() ))
    ) {

    private val currentOutpost: OutpostConfig

    init {
        this.currentOutpost = ConfigManager.getOutpost(currentOutPost)?: throw IllegalArgumentException("Outpost $currentOutPost does not exist!")
    }

    override fun onOpen(event: InventoryOpenEvent) {
        val player = event.player as Player
        ConfigManager.getSound().let{
            if(it.enabled){
                player.playSound(it.openMenu)
            }
        }

        val atomicIndex = AtomicInteger(0)
        guiConfig.guiItems.forEach { item ->
            if(item.outpostIdentifier == this.currentOutpost.identifier)return@forEach
            if(atomicIndex.get() >= this.inventory.size)return@forEach
            if(ConfigManager.getConfig().permissionPerOutpost && ConfigManager.getConfig().noPermissionType == NoPermissionType.HIDDEN && !player.hasPermission("rpgteleporting.outpost.${item.outpostIdentifier}"))return@forEach
            ConfigManager.getOutpost(item.outpostIdentifier)?.let { outpost ->
                setupGuiItem(item, outpost, player, atomicIndex)
            }?:
            ConfigManager.getRandomOutpost(item.outpostIdentifier)?.let{
                setupGuiItem(item, it, player, atomicIndex)
            }?:
            let{
                RPGTeleporting.getPlugin().logger.warning("Outpost ${item.outpostIdentifier} does not exist! gui tried to load it.")
            }
        }
    }

    private fun setupGuiItem(item: GuiItemConfig, randomLocationConfig: RandomLocationConfig, player: Player, atomicIndex: AtomicInteger){
        val index = atomicIndex.getAndIncrement()
        if(ConfigManager.getConfig().permissionPerOutpost && ConfigManager.getConfig().noPermissionType == NoPermissionType.BARRIER && !player.hasPermission("rpgteleporting.outpost.${item.outpostIdentifier}")){
            setItem(index, item.toItem(randomLocationConfig, this.currentOutpost, true)){
                teleport(player, randomLocationConfig, this.currentOutpost)
            }
        }else{
            setItem(index, item.toItem(randomLocationConfig, this.currentOutpost)){
                teleport(it.whoClicked as Player, randomLocationConfig, this.currentOutpost)
            }
        }
    }

    private fun setupGuiItem(item: GuiItemConfig, outpostConfig: OutpostConfig, player: Player, atomicIndex: AtomicInteger){
        val index = atomicIndex.getAndIncrement()
        if(ConfigManager.getConfig().permissionPerOutpost && ConfigManager.getConfig().noPermissionType == NoPermissionType.BARRIER && !player.hasPermission("rpgteleporting.outpost.${item.outpostIdentifier}")){
            setItem(index, item.toItem(outpostConfig, this.currentOutpost, true)){
                teleport(player, outpostConfig, this.currentOutpost)
            }
        }else{
            setItem(index, item.toItem(outpostConfig, this.currentOutpost)){
                teleport(it.whoClicked as Player, outpostConfig, this.currentOutpost)
            }
        }
    }

    private fun createTagResolver(to: Component, from: Component): TagResolver.Builder{
        return TagResolver.builder().resolvers(
            Placeholder.component("outpost_name", to),
            Placeholder.component("from_name", from)
        )
    }

    private fun checkPermission(player: Player, tagResolver: TagResolver.Builder, identifier: String): Boolean{
        if(ConfigManager.getConfig().permissionPerOutpost && !player.hasPermission("rpgteleporting.outpost.${identifier}")){
            player.sendMessage(ConfigManager.getMessage().noPermissionPerOutpost.format(tagResolver.build()))
            ConfigManager.getSound().let{
                if(it.enabled){
                    player.playSound(it.noPermission)
                }
            }
            return false
        }
        return true
    }

    private fun teleport(player: Player, randomLocationConfig: RandomLocationConfig, from: OutpostConfig){
        val resolverBuilder = createTagResolver(randomLocationConfig.name, from.name)
        checkPermission(player, resolverBuilder, randomLocationConfig.identifier) || return

        val center = randomLocationConfig.center.toLocation()
        val x = randomLocationConfig.center.x + (Math.random() * (randomLocationConfig.radius * 2)) - randomLocationConfig.radius
        val z = randomLocationConfig.center.z + (Math.random() * (randomLocationConfig.radius * 2)) - randomLocationConfig.radius
        val loc = Location(center.world, x, randomLocationConfig.center.y, z).toHighestLocation().add(0.0,1.0,0.0)

        validatePrice(player, randomLocationConfig.cost, resolverBuilder.resolver(Placeholder.parsed("cost", (randomLocationConfig.cost?:0).toString())).build(), loc) || return

        teleport(player, loc, resolverBuilder)
    }

    private fun teleport(player: Player, outpost: OutpostConfig, from: OutpostConfig){
        val resolverBuilder = createTagResolver(outpost.name, from.name)
        checkPermission(player, resolverBuilder, outpost.identifier) || return

        val loc = outpost.location.toLocation()
        val cost = outpost.getTotalCost(from.location)

        validatePrice(player, cost, resolverBuilder.resolver(Placeholder.parsed("cost", (cost?:0).toString())).build(), loc) || return

        teleport(player, loc, resolverBuilder)
    }


    private fun teleport(player: Player, loc: Location, resolverBuilder: TagResolver.Builder){
        player.teleport(loc)
        player.sendMessage(ConfigManager.getMessage().teleportSuccess.format(resolverBuilder.build()))
        ConfigManager.getSound().let{
            if(it.enabled){
                player.playSound(it.teleportSuccess)
            }
        }
    }


    private fun validatePrice(player: Player, rawCost: Double?, resolver: TagResolver, loc: Location): Boolean{
        if(!ConfigManager.getConfig().useEconomy)return true
        rawCost?.let { cost->
           if (RPGTeleporting.getEconomyAPI().getBalance(player) < cost) {
               player.sendMessage(ConfigManager.getMessage().notEnoughMoney.format(resolver))
               ConfigManager.getSound().let{
                   if(it.enabled){
                       player.playSound(it.insufficientFunds)
                   }
               }
               return false
           }
           RPGTeleporting.getEconomyAPI().withdraw(player, cost)
           player.teleport(loc)
           player.sendMessage(ConfigManager.getMessage().ecoTeleportSuccess.format(resolver))
           ConfigManager.getSound().let{
               if(it.enabled){
                   player.playSound(it.teleportSuccess)
               }
           }
        }
        return true
    }

}