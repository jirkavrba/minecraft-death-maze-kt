package dev.vrba.deathmaze.generator

import dev.vrba.deathmaze.generator.DeathMazeWorldGenerator.Builder.nodeSize
import org.bukkit.*
import org.bukkit.generator.ChunkGenerator
import java.util.*
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

object DeathMazeWorldGenerator
{
    private object Builder
    {
        const val nodeSize = 4.0

        const val height = 5.0

        val buildingBlocks = listOf(
                Material.STONE,
                Material.STONE_BRICKS,
                Material.DEAD_BRAIN_CORAL_BLOCK,
                Material.CRACKED_STONE_BRICKS,
                Material.MOSSY_STONE_BRICKS,
                Material.MOSSY_COBBLESTONE,
                Material.COBBLESTONE,
                Material.CHISELED_STONE_BRICKS,
                Material.AIR
        )

        fun fill(from: Location, to: Location, materials: List<Material>? = null)
        {
            // Refuse to fill locations on different worlds
            if (from.world != to.world) return

            val start = Location(from.world, min(from.x, to.x), min(from.y, to.y), min(from.z, to.z))
            val end = Location(from.world, max(from.x, to.x), max(from.y, to.y), max(from.z, to.z))
            val blocks = abs(end.x - start.x) * abs(end.y - start.y) * abs(end.z - start.z)

            // Do not fill regions bigger than 2M blocks as the server basically commits suicide
            if (blocks > 2000000.0) return

            for (x in start.blockX..end.blockX)
            {
                for (y in start.blockY..end.blockY)
                {
                    for (z in start.blockZ..end.blockZ)
                    {
                        from.world?.getBlockAt(x, y, z)?.type = (materials ?: buildingBlocks).random()
                    }
                }
            }
        }
    }

    fun generateWorld(players: Int): World
    {
        val world = this.generateEmptyWorld() ?: throw Exception("Cannot generate an empty world.")
        val maze = MazeGenerator.generateMaze(10 + (2 * players))

        this.buildMaze(world, maze)

        return world
    }

    private fun buildMaze(world: World, maze: MazeGenerator.Maze)
    {
        // Build the floor
        Builder.fill(
                Location(world, -nodeSize, 0.0, -nodeSize),
                Location(world, maze.width * nodeSize, 0.0, maze.height * nodeSize),
                Builder.buildingBlocks - Material.AIR
        )

        // Build walls around the maze
        // TODO: Refactor this shit
        Builder.fill(Location(world, -nodeSize, 1.0, -nodeSize), Location(world, maze.width * nodeSize, Builder.height, -nodeSize))
        Builder.fill(Location(world, -nodeSize, 1.0, -nodeSize), Location(world, -nodeSize, Builder.height, maze.height * nodeSize))
        Builder.fill(Location(world, maze.width * nodeSize, 1.0, maze.height * nodeSize), Location(world, maze.width * nodeSize, Builder.height, -nodeSize))
        Builder.fill(Location(world, maze.width * nodeSize, 1.0, maze.height * nodeSize), Location(world, -nodeSize, Builder.height, maze.height * nodeSize))

        for (node in maze.nodes)
        {
            for (neighborNode in maze.neighbours(node))
            {
                // Trump says that we gotta build a wall between those two nodes
                if (maze.hasDoorBetween(node, neighborNode))
                {
                    Builder.fill(
                            Location(world, node.x * nodeSize, 1.0, node.y * nodeSize),
                            Location(world, neighborNode.x * nodeSize, Builder.height, neighborNode.y * nodeSize)
                    )
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

        val world = creator.createWorld()

        // Disable both monster and animal spawning
        world?.setSpawnFlags(false, false)
        world?.difficulty = Difficulty.HARD

        return world
    }

    private class EmptyWorldChunkGenerator : ChunkGenerator()
    {
        override fun generateChunkData(world: World, random: Random, x: Int, z: Int, biome: BiomeGrid): ChunkData
        {
            return createChunkData(world)
        }
    }
}