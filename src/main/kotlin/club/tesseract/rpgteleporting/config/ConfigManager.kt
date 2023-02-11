package club.tesseract.rpgteleporting.config

import club.tesseract.rpgteleporting.RPGTeleporting
import club.tesseract.rpgteleporting.config.impl.*
import club.tesseract.rpgteleporting.config.impl.message.MessageConfig
import club.tesseract.rpgteleporting.utils.ComponentUtils.toMini
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.walk

object ConfigManager {

    private lateinit var configPath: Path
    private lateinit var outpostPath: Path
    private lateinit var guiPath: Path

    private lateinit var config: GeneralConfig
    private lateinit var message: MessageConfig
    private lateinit var sound: SoundConfig

    private val outposts: HashMap<String, OutpostConfig> = hashMapOf()
    private val random_outposts: HashMap<String, RandomLocationConfig> = hashMapOf()
    private val guis:  HashMap<String, TeleportMenuConfig> = hashMapOf()

    @OptIn(ExperimentalPathApi::class)
    fun loadConfigs(){
        configPath = RPGTeleporting.getPlugin().dataFolder.toPath()
        if(!configPath.exists()) configPath.createDirectories()
        outpostPath = configPath.resolve("outposts")
        guiPath = configPath.resolve("gui")
        if(!outpostPath.exists()){
            outpostPath.createDirectories()
            ConfigHelper.writeObjectToPath(outpostPath.resolve("ExampleOutpost.yml"), OutpostConfig("Example Outpost".toMini(), "example_outpost"))
            ConfigHelper.writeObjectToPath(outpostPath.resolve("ExampleOutpost2.yml"), OutpostConfig("Example Outpost 2".toMini(), "example_outpost_2"))
        }
        if(!guiPath.exists()){
            guiPath.createDirectories()
            ConfigHelper.writeObjectToPath(guiPath.resolve("ExampleGui.yml"), TeleportMenuConfig())
        }

        config = ConfigHelper.initConfigFile(configPath.resolve("config.yml"), GeneralConfig())
        message = ConfigHelper.initConfigFile(configPath.resolve("message.yml"), MessageConfig())
        sound = ConfigHelper.initConfigFile(configPath.resolve("sound.yml"), SoundConfig())

        outposts.clear()
        outpostPath.walk().filter { it.toString().endsWith(".yml") }.forEach {
            try {
                val outpost = ConfigHelper.readConfigFile<OutpostConfig>(it)?: return
                outposts[outpost.identifier] = outpost
            }catch (e: Exception){
                RPGTeleporting.getPlugin().logger.warning("Failed to load outpost config: ${it.fileName}")
            }
        }
        guis.clear()
        guiPath.walk().filter { it.toString().endsWith(".yml") }.forEach {
            try {
                val gui = ConfigHelper.readConfigFile<TeleportMenuConfig>(it)?: return
                guis[gui.identifier] = gui
            }catch (e: Exception){
                RPGTeleporting.getPlugin().logger.warning("Failed to load gui config: ${it.fileName}")
            }
        }

        config.randomTeleports.forEach {
            random_outposts[it.identifier] = it
        }

    }

    fun getConfig(): GeneralConfig {
        return config
    }

    fun getMessage(): MessageConfig {
        return message
    }

    fun getSound(): SoundConfig {
        return sound
    }

    fun getOutposts(): List<OutpostConfig> {
        return outposts.values.toList()
    }

    fun getOutpost(identifier: String): OutpostConfig? {
        return outposts[identifier]
    }

    fun getRandomOutpost(identifier: String): RandomLocationConfig? {
        return random_outposts[identifier]
    }

    fun getGuis(): List<TeleportMenuConfig> {
        return guis.values.toList()
    }

    fun getGui(identifier: String): TeleportMenuConfig? {
        return guis[identifier]
    }

}