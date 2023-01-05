package club.tesseract.rpgteleporting.config.impl

import club.tesseract.rpgteleporting.config.serializer.ComponentSerializer
import club.tesseract.rpgteleporting.utils.ComponentUtils.toMini
import com.charleskorn.kaml.YamlComment
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable
data class TeleportMenuConfig(
    @YamlComment("A unique string used to distinguish between menus, allowed characters: [a-z0-9_]")
    val identifier: String = "default",
    @YamlComment("Used for the display name of the item in the GUI")
    val title: @Serializable(with= ComponentSerializer::class) Component = "Current Outpost: <outpost_name>".toMini(),

    val guiItems: List<GuiItemConfig> = listOf(
        GuiItemConfig("Example Outpost".toMini(), listOf("Click to teleport to Example Outpost".toMini()), "example_outpost", "ENDER_PEARL", null, 0, false, 1),
        GuiItemConfig("Example Outpost 2".toMini(), listOf("Click to teleport to Example Outpost 2".toMini()), "example_outpost_2", "ENDER_PEARL", null, 0, false, 1),
    ),
)
