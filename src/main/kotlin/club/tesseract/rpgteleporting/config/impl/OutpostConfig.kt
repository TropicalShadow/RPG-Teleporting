package club.tesseract.rpgteleporting.config.impl

import club.tesseract.rpgteleporting.config.serializer.ComponentSerializer
import club.tesseract.rpgteleporting.utils.ComponentUtils.toMini
import com.charleskorn.kaml.YamlComment
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable
data class OutpostConfig(
    @YamlComment("Used for the display name of the item in the GUI, if configPerItemName is set to true. also used for display purposes in messages")
    val name: @Serializable(with=ComponentSerializer::class)Component = "Example Outpost".toMini(),
    @YamlComment("A unique string used to distinguish between outposts, allowed characters: [a-z0-9_]")
    val identifier: String = "example_outpost",
    @YamlComment("Location player will be teleported to")
    val location: LocationConfig = LocationConfig("world", 0.0, 0.0, 0.0),
    @YamlComment("Cost of teleporting to this outpost","If you don't have an economy plugin, you can set this to null")
    val cost: CostConfig? = CostConfig(),
){

    fun getTotalCost(other: LocationConfig): Double? {
        val cost = this.cost?: return null
        return cost.cost + (cost.chunkCost * other.distanceToChunk(location)) + (cost.worldCost * if (other.world == location.world) 0 else 1)
    }

}
