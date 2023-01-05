package club.tesseract.rpgteleporting

import club.tesseract.rpgteleporting.command.RPGTeleportCommand
import club.tesseract.rpgteleporting.config.ConfigManager
import club.tesseract.rpgteleporting.economy.EconomyAPI
import club.tesseract.rpgteleporting.economy.VaultEconomy
import fr.mrmicky.fastinv.FastInvManager
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


@Suppress("unused")
class RPGTeleporting: JavaPlugin() {

    override fun onEnable() {
        FastInvManager.register(this);
        ConfigManager.loadConfigs()

        if(ConfigManager.getConfig().useEconomy) {
            economyAPI = getEconomyPlugin()
            if (economyAPI == null) {
                logger.severe("Plugin is configured to use economy, but no economy plugin was found! Change this in the config.yml or install an economy plugin i.e [Vault]")
                Bukkit.getPluginManager().disablePlugin(this)
                return
            }
        }

        registerCommand()

        logger.info("RPGTeleporting has been enabled!")
    }

    override fun onDisable() {

        logger.info("RPGTeleporting has been disabled!")
    }

    private fun registerCommand(){
        getCommand("rpgteleporting")?.setExecutor(RPGTeleportCommand)
        getCommand("rpgteleporting")?.tabCompleter = RPGTeleportCommand
        getCommand("rpgteleporting")?.aliases = listOf("rpgtp", "rpgteleport")
    }

    private fun getEconomyPlugin(): EconomyAPI? {
        return when {
            server.pluginManager.getPlugin("Vault") != null -> {
                logger.info("Vault found, Hooking Vault economy")
                val rsp = server.servicesManager.getRegistration(Economy::class.java)
                if (rsp == null) {
                    logger.info("Vault economy not found")
                    null
                }else {
                    VaultEconomy(rsp.provider)
                }
            }
            else -> {
                logger.info("No economy plugin found, economy feature disabled")
                null
            }
        }
    }

    companion object{
        private var economyAPI: EconomyAPI? = null

        fun getEconomyAPI(): EconomyAPI {
            return economyAPI?: throw IllegalStateException("EconomyAPI is null!")
        }

        fun getPlugin(): RPGTeleporting{
            return getPlugin(RPGTeleporting::class.java)
        }

    }

}