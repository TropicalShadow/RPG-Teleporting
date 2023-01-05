package club.tesseract.rpgteleporting.config.serializer

import club.tesseract.rpgteleporting.utils.ComponentUtils.toMini
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

class ComponentSerializer: KSerializer<Component> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Component", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Component {
        return decoder.decodeString().toMini()
    }

    override fun serialize(encoder: Encoder, value: Component) {
        encoder.encodeString(MiniMessage.miniMessage().serialize(value).replace(Regex("\\\\<"), "<"))
    }


}