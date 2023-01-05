package club.tesseract.rpgteleporting.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

object ComponentUtils {

    private val mini = MiniMessage.miniMessage()

    fun String.toMini(vararg resolvers: TagResolver): Component {
        return mini.deserialize(this, *resolvers)
    }

    fun Component.format(vararg resolvers: TagResolver): Component {
        return mini.serialize(this).replace(Regex("\\\\<"), "<").toMini(*resolvers)
    }

    fun Component.stripItalic(): Component {
        return if(this.hasDecoration(TextDecoration.ITALIC)) this else this.decoration(TextDecoration.ITALIC, false)
    }

}