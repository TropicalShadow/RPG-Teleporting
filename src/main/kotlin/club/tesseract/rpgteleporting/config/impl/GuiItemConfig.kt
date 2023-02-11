package club.tesseract.rpgteleporting.config.impl

import club.tesseract.rpgteleporting.config.ConfigManager
import club.tesseract.rpgteleporting.config.serializer.ComponentSerializer
import club.tesseract.rpgteleporting.utils.ComponentUtils.format
import club.tesseract.rpgteleporting.utils.ComponentUtils.stripItalic
import club.tesseract.rpgteleporting.utils.ComponentUtils.toMini
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

@Serializable
data class GuiItemConfig(
    val name: @Serializable(with= ComponentSerializer::class)Component = "<purple>Teleport".toMini(),
    val lore: List<@Serializable(with= ComponentSerializer::class)Component> = listOf("<gold>Click to teleport".toMini()),
    val outpostIdentifier: String = "unique_outpost",
    val material: String = "ENDER_PEARL",
    val headUUID: String? = null,
    val customModelData: Int = 0,
    val glow: Boolean = false,
    val amount: Int = 1,
){

    fun toItem(outpost: RandomLocationConfig, from: OutpostConfig, barrierOverride: Boolean = false): ItemStack{
        val tagResolver = TagResolver.builder()
            .resolvers(
                Placeholder.component("outpost_name", outpost.name),
                Placeholder.parsed("cost", outpost.cost?.toString()?: "0.0"),
                Placeholder.parsed("distance", outpost.radius.div(16).toString()),
                Placeholder.parsed("world", outpost.center.world),
                Placeholder.parsed("world_title",outpost.center.worldTitle()),
                Placeholder.parsed("x", outpost.center.x.toInt().toString()),
                Placeholder.parsed("y", outpost.center.y.toInt().toString()),
                Placeholder.parsed("z", outpost.center.z.toInt().toString()),
            )
            .build()

        val material = if(!barrierOverride) Material.getMaterial(material)?: let{
            return ItemStack(Material.STONE, amount).also{ item -> item.editMeta { meta -> meta.displayName(Component.text("Invalid Material Presented for ${outpost.name}")) }}
        }
        else Material.BARRIER

        val item = ItemStack(material, amount)
        val meta = item.itemMeta
        val name = if(ConfigManager.getConfig().configPerItemName) name else ConfigManager.getMessage().gui.randomItemName
        val lore = if(ConfigManager.getConfig().configPerItemName) lore else ConfigManager.getMessage().gui.randomItemLore

        meta.displayName(name.format(tagResolver))
        meta.lore(lore.map { it.format(tagResolver) })
        if (glow) {
            meta?.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 1, true)
        }
        meta.setCustomModelData(customModelData)

        if(headUUID != null){
            val skullMeta = meta as SkullMeta
            skullMeta.owningPlayer = Bukkit.getOfflinePlayer(headUUID)
        }

        item.itemMeta = meta
        return item
    }

    fun toItem(outpost: OutpostConfig, from: OutpostConfig, barrierOverride: Boolean = false): ItemStack {
        val tagResolver = TagResolver.builder()
            .resolvers(
                Placeholder.component("outpost_name", outpost.name),
                Placeholder.parsed("cost", outpost.cost?.cost?.toString()?: "0.0"),
                Placeholder.parsed("chunk_cost", outpost.cost?.chunkCost?.toString()?: "0.0"),
                Placeholder.parsed("world_cost", outpost.cost?.worldCost?.toString()?: "0.0"),
                Placeholder.parsed("total_cost", outpost.getTotalCost(from.location).toString()),
                Placeholder.parsed("distance", outpost.location.distanceToChunk(from.location).toString()),
                Placeholder.parsed("world", outpost.location.world),
                Placeholder.parsed("world_title",outpost.location.worldTitle()),
                Placeholder.parsed("x", outpost.location.x.toInt().toString()),
                Placeholder.parsed("y", outpost.location.y.toInt().toString()),
                Placeholder.parsed("z", outpost.location.z.toInt().toString()),
            )
            .build()

        val material = if(!barrierOverride) Material.getMaterial(material)?: let{
            return ItemStack(Material.STONE, amount).also{ item -> item.editMeta { meta -> meta.displayName(Component.text("Invalid Material Presented for ${outpost.name}")) }}
        }
        else Material.BARRIER

        val item = ItemStack(material, amount)
        val meta = item.itemMeta
        val name = if(ConfigManager.getConfig().configPerItemName) name else ConfigManager.getMessage().gui.itemName
        val lore = if(ConfigManager.getConfig().configPerItemName) lore else ConfigManager.getMessage().gui.itemLore
        meta.displayName(name.format(tagResolver).stripItalic())
        meta.lore(lore.map{ it.format(tagResolver).stripItalic()})
        meta.isUnbreakable = true
        meta.addItemFlags(*org.bukkit.inventory.ItemFlag.values())
        if (glow) {
            meta?.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 1, true)
        }
        meta.setCustomModelData(customModelData)

        if(meta is SkullMeta){
            headUUID?.let { meta.owningPlayer = Bukkit.getOfflinePlayer(java.util.UUID.fromString(it)) }
        }

        item.itemMeta = meta
        return item
    }

}