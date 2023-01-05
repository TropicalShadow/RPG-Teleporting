package club.tesseract.rpgteleporting.config.impl.message

import club.tesseract.rpgteleporting.config.serializer.ComponentSerializer
import club.tesseract.rpgteleporting.utils.ComponentUtils.toMini
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable
data class GuiMessageConfig(
    val itemName: @Serializable(with = ComponentSerializer::class) Component = "<gold><outpost_name><reset>".toMini(),
    val itemLore: List<@Serializable(with = ComponentSerializer::class) Component> = listOf(
        "<gray>Cost:<gold> <cost><reset>".toMini(),
        "<gray>Chunk Cost:<gold> <chunk_cost><reset>".toMini(),
        "<gray>Total Cost:<gold> <total_cost><reset>".toMini(),
        "<gray>Distance (chunks):<gold> <distance><reset>".toMini(),
        "<gray>World:<gold> <world><reset>".toMini(),
        "<gray>Location:<gold> <x>, <y>, <z><reset>".toMini(),
    ),
)