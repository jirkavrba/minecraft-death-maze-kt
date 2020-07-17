package dev.vrba.deathmaze.generator

object MazeGenerator
{
    data class Direction(val x: Int, val y: Int)

    data class MazeNodeDoor(val from: MazeNode, val to: MazeNode)

    data class MazeNode(val x: Int, val y: Int)
    {
        operator fun plus(direction: Direction): MazeNode
        {
            return MazeNode(this.x + direction.x, this.y + direction.y)
        }
    }

    data class Maze(val width: Int, val height: Int, val nodes: Set<MazeNode>, val doors: Set<MazeNodeDoor>)
    {
        private val directions = setOf(
                Direction(-1, 0),
                Direction(0, -1),
                Direction(1, 0),
                Direction(0, 1)
        )

        fun neighbours(node: MazeNode): List<MazeNode> = this.directions.map { node + it }.filter { this.isInBounds(it) }

        fun unvisitedNeighbours(node: MazeNode): List<MazeNode> = (this.neighbours(node) - this.nodes).shuffled()

        private fun isInBounds(node: MazeNode) =
                node.x in (0 until this.width) && node.y in (0 until this.height)

        fun hasDoorBetween(first: MazeNode, second: MazeNode) =
                this.doors.contains(MazeNodeDoor(first, second)) || this.doors.contains(MazeNodeDoor(second, first))
    }


    private fun resolveNode(node: MazeNode, maze: Maze): Maze
    {
        var extended = Maze(maze.width, maze.height, maze.nodes + node, maze.doors)

        extended.unvisitedNeighbours(node).forEach {
            // Recurse with each neighbour
            // This should be probably optimized as tail recursive call, but as the recursion depth is max size ^ 2 in
            // the worst case scenario, I think it's not that relevant anyway
            if (!extended.nodes.contains(it))
            {
                extended = resolveNode(
                        it,
                        Maze(
                                extended.width,
                                extended.height,
                                extended.nodes + node,
                                extended.doors + MazeNodeDoor(node, it)
                        )
                )
            }
        }

        return extended
    }

    /**
     * Generates a maze containing size x size nodes
     */
    fun generateMaze(size: Int): Maze
    {
        // Create an empty maze with no doors or nodes
        val base = Maze(size, size, emptySet(), emptySet())
        // Start in the center for better gaussian distance distribution
        val start = MazeNode(size / 2, size / 2)

        return resolveNode(start, base)
    }
}