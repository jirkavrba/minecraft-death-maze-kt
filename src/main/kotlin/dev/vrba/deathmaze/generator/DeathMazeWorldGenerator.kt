package dev.vrba.deathmaze.generator

import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
import org.bukkit.generator.ChunkGenerator
import java.util.*

object DeathMazeWorldGenerator
{
    fun generateWorld(players: Int): World
    {
        val maze = MazeGenerator.generateMaze(10 + (2 * players))
        val world = this.generateEmptyWorld() ?: throw Exception("Cannot generate an empty world.")

        this.buildMaze(world, maze)

        return world
    }

    private fun buildMaze(world: World, maze: MazeGenerator.Maze)
    {
        for (node in maze.nodes)
        {
            for (neighborNode in maze.neighbours(node))
            {
                // Trump says that we gotta build a wall between those two nodes
                if (!maze.hasDoorBetween(node, neighborNode))
                {
                    // TODO: Build a wall
                }
            }
        }
    }

    private fun generateEmptyWorld(): World?
    {
        val name = "dm_" + System.currentTimeMillis()
        val creator = WorldCreator(name)

        creator.type(WorldType.FLAT)
        creator.generator(EmptyWorldChunkGenerator())

        return creator.createWorld()
    }

    private class EmptyWorldChunkGenerator : ChunkGenerator()
    {
        override fun generateChunkData(world: World, random: Random, x: Int, z: Int, biome: BiomeGrid): ChunkData
        {
            return createChunkData(world)
        }
    }
}