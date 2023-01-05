package club.tesseract.rpgteleporting.gui

import club.tesseract.rpgteleporting.RPGTeleporting
import club.tesseract.rpgteleporting.config.ConfigManager
import club.tesseract.rpgteleporting.config.NoPermissionType
import club.tesseract.rpgteleporting.config.impl.OutpostConfig
import club.tesseract.rpgteleporting.config.impl.TeleportMenuConfig
import club.tesseract.rpgteleporting.utils.ComponentUtils.format
import club.tesseract.rpgteleporting.utils.ComponentUtils.toMini
import club.tesseract.rpgteleporting.utils.InventoryUtils.calculateInventorySize
import fr.mrmicky.fastinv.FastInv
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
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
                val index = atomicIndex.getAndIncrement()
                if(ConfigManager.getConfig().permissionPerOutpost && ConfigManager.getConfig().noPermissionType == NoPermissionType.BARRIER && !player.hasPermission("rpgteleporting.outpost.${item.outpostIdentifier}")){
                    setItem(index, item.toItem(outpost, this.currentOutpost, true)){
                        teleport(player, outpost, this.currentOutpost)
                    }
                }else{
                    setItem(index, item.toItem(outpost, this.currentOutpost)){
                        teleport(it.whoClicked as Player, outpost, this.currentOutpost)
                    }
                }
            }?: let{
                RPGTeleporting.getPlugin().logger.warning("Outpost ${item.outpostIdentifier} does not exist! gui tried to load it.")
            }
        }
    }


    private fun teleport(player: Player, outpost: OutpostConfig, from: OutpostConfig){
        val resolverBuilder = TagResolver.builder().resolvers(
            Placeholder.component("outpost_name", outpost.name),
            Placeholder.component("from_name", from.name)
        )
        if(ConfigManager.getConfig().permissionPerOutpost && !player.hasPermission("rpgteleporting.outpost.${outpost.identifier}")){
            player.sendMessage(ConfigManager.getMessage().noPermissionPerOutpost.format(resolverBuilder.build()))
            ConfigManager.getSound().let{
                if(it.enabled){
                    player.playSound(it.noPermission)
                }
            }
            return
        }
        if(ConfigManager.getConfig().useEconomy) {
            outpost.getTotalCost(from.location)?.let { cost ->
                val resolver = resolverBuilder.resolver(Placeholder.parsed("cost", cost.toString())).build()
                if (RPGTeleporting.getEconomyAPI().getBalance(player) < cost) {
                    player.sendMessage(ConfigManager.getMessage().notEnoughMoney.format(resolver))
                    ConfigManager.getSound().let{
                        if(it.enabled){
                            player.playSound(it.insufficientFunds)
                        }
                    }
                    return
                }
                RPGTeleporting.getEconomyAPI().withdraw(player, cost)
                player.teleport(outpost.location.toLocation())
                player.sendMessage(ConfigManager.getMessage().ecoTeleportSuccess.format(resolver))
                ConfigManager.getSound().let{
                    if(it.enabled){
                        player.playSound(it.teleportSuccess)
                    }
                }
                return
            }
        }
        player.teleport(outpost.location.toLocation())
        player.sendMessage(ConfigManager.getMessage().teleportSuccess.format(resolverBuilder.build()))
        ConfigManager.getSound().let{
            if(it.enabled){
                player.playSound(it.teleportSuccess)
            }
        }
    }

}