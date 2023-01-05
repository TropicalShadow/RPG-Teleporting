package club.tesseract.rpgteleporting.command

import club.tesseract.rpgteleporting.config.ConfigManager
import club.tesseract.rpgteleporting.gui.TeleportGui
import club.tesseract.rpgteleporting.utils.ComponentUtils.toMini
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object RPGTeleportCommand: TabExecutor {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String> {
        val results = mutableListOf<String>()
        if(!sender.hasPermission("rpgteleporting.command"))return results
        if(args.isEmpty()||args.size==1){
            if(sender.hasPermission("rpgteleporting.command.open"))results.add("open")
            if(sender.hasPermission("rpgteleporting.command.reload"))results.add("reload")
        }else if(args.size == 2 && sender.hasPermission("rpgteleporting.command.open")){
            when(args.first()){
                "open" -> {
                    ConfigManager.getGuis().forEach { results.add(it.identifier) }
                }
            }
        }else if(args.size == 3 && sender.hasPermission("rpgteleporting.command.open")){
            when(args.first()){
                "open" ->{
                    val gui = ConfigManager.getGui(args[1])?: return results
                    gui.guiItems.forEach { results.add(it.outpostIdentifier) }
                }
            }
        }
        return results
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(!sender.hasPermission("rpgteleporting.command")){
            sender.sendMessage(ConfigManager.getMessage().noPermission)
            return true
        }
        if(sender !is Player)return false
        if(args.isEmpty()) return false
        when(args.first()){
            "reload" -> {
                if(!sender.hasPermission("rpgteleporting.command.reload")){
                    sender.sendMessage(ConfigManager.getMessage().noPermission)
                    return true
                }
                ConfigManager.loadConfigs()
                sender.sendMessage("<green>RPGTeleporting reloaded!".toMini())
            }
            "open" ->{
                if(!sender.hasPermission("rpgteleporting.command.open")){
                    sender.sendMessage(ConfigManager.getMessage().noPermission)
                    return true
                }
                if(args.size < 3){
                    sender.sendMessage("<red>Usage: /rpgteleport open <gui_identifier> <outpost_identifier> [player_name]".toMini())
                    return true
                }
                val gui = ConfigManager.getGui(args[1])
                val outpost = ConfigManager.getOutpost(args[2])
                if (outpost == null) {
                    sender.sendMessage("<red>Outpost ${args[2]} does not exist!".toMini())
                    return false
                }
                if(gui == null){
                    sender.sendMessage("<red>GUI ${args[1]} does not exist!".toMini())
                    return false
                }
                if(args.size > 3 && sender.hasPermission("rpgteleporting.command.open.others")){
                    val target = sender.server.getPlayer(args[3])
                    if(target == null){
                        sender.sendMessage("<red>Player ${args[3]} does not exist!".toMini())
                        return false
                    }
                    TeleportGui(outpost.identifier, gui).open(target)
                }else {
                    TeleportGui(outpost.identifier, gui).open(sender)
                }
            }
        }
        return true
    }
}