package club.tesseract.rpgteleporting.config.impl

import club.tesseract.rpgteleporting.config.NoPermissionType
import com.charleskorn.kaml.YamlComment
import kotlinx.serialization.Serializable

@Serializable
data class GeneralConfig(
    @YamlComment("Should the plugin use displayname & lore from the outpost config file or the global display and lore from Message config", "default: false")
    val configPerItemName: Boolean = false,
    @YamlComment("should the plugin use an economy plugin? false if no economy plugin is being used", "default: false")
    val useEconomy: Boolean = false,
    @YamlComment("Require player to have a permission to teleport to each outpost", "Example permission: rpgteleporting.outpost.{unique_identifier}", "default: false")
    val permissionPerOutpost: Boolean = false,
    @YamlComment("What should the plugin do if a player doesn't have permission to teleport to an outpost","Examples: ", "BARRIER - changes item type to barrier", "NONE - Does nothing to the item visually", "HIDDEN - hides the outpost in the gui so the player can't see it", "default: BARRIER")
    val noPermissionType: NoPermissionType = NoPermissionType.BARRIER,
    @YamlComment("Random Teleport Config")
    val randomTeleports: List<RandomLocationConfig> = listOf(RandomLocationConfig())

)