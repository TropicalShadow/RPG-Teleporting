package club.tesseract.rpgteleporting.config.impl

import kotlinx.serialization.Serializable
import java.util.*
import kotlin.math.abs
import kotlin.math.ceil

@Serializable
data class LocationConfig(
    val world: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float = 90f,
    val pitch: Float = 0f,
){
    fun toLocation() = org.bukkit.Location(org.bukkit.Bukkit.getWorld(world), x, y, z, yaw, pitch)

    fun distanceToChunk(other: LocationConfig): Int {
        val x = abs(this.x - other.x)
        val z = abs(this.z - other.z)
        return ceil(x / 16).toInt() + ceil(z / 16).toInt()
    }


    fun worldTitle() = world.replace("_", " ")
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    companion object{
        fun fromLocation(location: org.bukkit.Location) = LocationConfig(location.world.name, location.x, location.y, location.z, location.yaw, location.pitch)
    }
}
