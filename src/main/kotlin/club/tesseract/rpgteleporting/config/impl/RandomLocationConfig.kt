package club.tesseract.rpgteleporting.config.impl

import club.tesseract.rpgteleporting.config.serializer.ComponentSerializer
import club.tesseract.rpgteleporting.utils.ComponentUtils.toMini
import com.charleskorn.kaml.YamlComment
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable
data class RandomLocationConfig(
    @YamlComment("A unique string used to distinguish between random locations (Just in-case you want one per world?), allowed characters: [a-z0-9_]")
    val identifier: String = "random_location",
    @YamlComment("Used for the display name of the item in the GUI, if configPerItemName is set to true. also used for display purposes in messages")
    val name: @Serializable(with=ComponentSerializer::class) Component = "Random Location".toMini(),
    @YamlComment("Cost of teleporting to this outpost","If you don't have an economy plugin, you can set this to null")
    val cost: Double? = 0.0,
    @YamlComment("Centered location of the random teleport")
    val center: LocationConfig = LocationConfig("world", 0.0, 0.0, 0.0),
    @YamlComment("The radius in which the player can be teleported to")
    val radius: Int = 1000,
)