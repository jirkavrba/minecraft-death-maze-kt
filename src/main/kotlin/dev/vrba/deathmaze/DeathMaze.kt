package dev.vrba.deathmaze

import dev.vrba.deathmaze.generator.DeathMazeWorldGenerator
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
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

        players.forEach {
            val location = Location(world, 25.0, 50.0, 25.0)

            location.yaw = 0F
            location.pitch =  90F

            it.teleport(location)
        }
    }
}