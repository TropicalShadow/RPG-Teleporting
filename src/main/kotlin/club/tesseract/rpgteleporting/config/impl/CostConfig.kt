package club.tesseract.rpgteleporting.config.impl

import com.charleskorn.kaml.YamlComment
import kotlinx.serialization.Serializable

@Serializable
data class CostConfig(
    @YamlComment("The base cost of teleporting to this outpost", "default: 0.0")
    val cost: Double = 0.0,
    @YamlComment("The cost per each chunk the player gets teleported to this outpost", "default: 0.0")
    val chunkCost: Double = 0.0,
    @YamlComment("The cost for teleporting between worlds","default: 0.0")
    val worldCost: Double = 0.0,
)
