package club.tesseract.rpgteleporting.config.impl

import club.tesseract.rpgteleporting.config.serializer.SoundSerializer
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound

@Serializable
data class SoundConfig(
    val enabled: Boolean = true,
    val openMenu: @Serializable(with= SoundSerializer::class) Sound = Sound.sound(Key.key("minecraft:block.chest.open"), Sound.Source.PLAYER, 1.0f, 1.0f),
    val teleportSuccess: @Serializable(with= SoundSerializer::class) Sound = Sound.sound(Key.key("minecraft:entity.enderman.teleport"), Sound.Source.PLAYER, 1.0f, 1.0f),
    val insufficientFunds: @Serializable(with= SoundSerializer::class) Sound = Sound.sound(Key.key("minecraft:entity.villager.no"), Sound.Source.PLAYER, 1.0f, 1.0f),
    val noPermission: @Serializable(with= SoundSerializer::class) Sound = Sound.sound(Key.key("minecraft:entity.villager.no"), Sound.Source.PLAYER, 1.0f, 1.0f),
)
