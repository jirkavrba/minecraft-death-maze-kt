package dev.vrba.deathmaze

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class DeathMaze : JavaPlugin()
{
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
    {
        if (command.name == "death-maze" && sender is Player)
        {
            this.startNewDeathMazeSession()
            return true
        }

        return false
    }

    private fun startNewDeathMazeSession()
    {
        println("Starting")
    }
}