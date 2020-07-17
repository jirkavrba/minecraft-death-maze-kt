package dev.vrba.deathmaze.generator

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

object LootGenerator
{
    private data class WeightedItem(val stack: ItemStack, val weight: Int)

    private infix fun ItemStack.withWeight(weight: Int) = WeightedItem(this, weight)
    private infix fun ItemStack.withProbability(probability: Int): ItemStack =
        if (Random.nextInt(100) <= probability) this
        else ItemStack(Material.AIR)

    private fun weightedRandom(vararg items: WeightedItem): ItemStack
    {
        val weight = items.sumBy { it.weight }
        val selected = Random.nextInt(weight)

        var accumulator = 0

        for (item in items)
        {
            accumulator += item.weight

            if (selected <= accumulator)
                return item.stack
        }

        // Return default item stack, shouldn't happen if the given items array is not empty
        return ItemStack(Material.AIR)
    }

    fun generateLootBox(): Array<ItemStack>
    {
        return arrayOf(
            this.weightedRandom(ItemStack(Material.AIR) withWeight 10 ),
            ItemStack(Material.AIR) withProbability 20 // 20% chance to get em diamonds
        )
    }
}