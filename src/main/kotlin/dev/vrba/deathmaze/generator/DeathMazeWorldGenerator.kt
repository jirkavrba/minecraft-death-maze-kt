package dev.vrba.deathmaze.generator

import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
import org.bukkit.generator.ChunkGenerator
import java.util.*

object DeathMazeWorldGenerator
{
    fun generateWorld(): World
    {
        val world = this.generateEmptyWorld() ?: throw Exception("Cannot generate an empty world.")

        // Disable mob spawning
        world.setSpawnFlags(false, false)

        return world
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