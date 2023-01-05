package club.tesseract.rpgteleporting.config.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound

class SoundSerializer: KSerializer<Sound> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Sound", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Sound {
        return Sound.sound(Key.key(decoder.decodeString()), Sound.Source.PLAYER, 1f, 1f)
    }

    override fun serialize(encoder: Encoder, value: Sound) {
        encoder.encodeString(value.name().asString())
    }
}