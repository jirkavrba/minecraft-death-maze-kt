package dev.vrba.deathmaze

import dev.vrba.deathmaze.generator.DeathMazeWorldGenerator
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class DeathMaze : JavaPlugin()
{
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
    {
        if (command.name == "death-maze")
        {
            this.startNewDeathMazeSession()
            return true
        }

        return false
    }

    private fun startNewDeathMazeSession()
    {
        val players = this.server.onlinePlayers

        val world = DeathMazeWorldGenerator.generateWorld(players.size)

        players.forEach { it.teleport(world.spawnLocation) }
    }
}