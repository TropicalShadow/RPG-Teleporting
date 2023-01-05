package club.tesseract.rpgteleporting.config.impl.message

import club.tesseract.rpgteleporting.config.serializer.ComponentSerializer
import club.tesseract.rpgteleporting.utils.ComponentUtils.toMini
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable
data class MessageConfig(
    val prefix: @Serializable(with = ComponentSerializer::class)Component = "<gray>[<gold>RPGTeleporting<gray>]<reset> ".toMini(),
    val notEnoughMoney: @Serializable(with = ComponentSerializer::class)Component = "<red>You do not have enough money to teleport to this outpost!".toMini(),
    val ecoTeleportSuccess: @Serializable(with = ComponentSerializer::class)Component = "<green>You have been teleported to <gold><outpost_name><green> for <gold><cost><green>!".toMini(),
    val teleportSuccess: @Serializable(with = ComponentSerializer::class)Component = "<green>You have been teleported to <gold><outpost_name><green>!".toMini(),
    val noPermission: @Serializable(with = ComponentSerializer::class)Component = "<red>You do not have permission to use this command!".toMini(),
    val noPermissionPerOutpost: @Serializable(with = ComponentSerializer::class)Component = "<red>You do not have permission to teleport to this outpost!".toMini(),
    val gui: GuiMessageConfig = GuiMessageConfig(),
)
