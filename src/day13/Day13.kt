package day13

import readInput

sealed interface Item : Comparable<Item>
class ListItem(val items: List<Item> = listOf()) : Item {

    override fun toString(): String {
        return "$items"
    }

    override infix fun compareTo(other: Item): Int =
        when (other) {
            is IntItem -> this compareTo ListItem(listOf(other))
            is ListItem -> items.zip(other.items)
                .map { it.first compareTo it.second }
                .filterNot { it == 0 }
                .firstOrNull()
                ?: (this.items.size compareTo other.items.size)
        }
}

class IntItem(val value: Int) : Item {
    override fun toString(): String {
        return "$value"
    }

    override infix fun compareTo(other: Item) =
        when (other) {
            is IntItem -> this.value compareTo other.value
            is ListItem -> ListItem(listOf(this)) compareTo other
        }
}

fun main() {


    fun parseItem(input: String): ListItem {

        input.toIntOrNull()?.let { return ListItem(listOf(IntItem(it))) }

        val inside = input.removeSurrounding("[", "]")
        if (inside.isEmpty()) return ListItem()

        return ListItem(
            buildList {
                var current = ""
                var brackets = 0

                for (char in inside) {
                    if (char == '[') brackets++
                    if (char == ']') brackets--
                    if (char == ',' && brackets == 0) {
                        add(parseItem(current))
                        current = ""
                        continue
                    }
                    current += char
                }

                add(parseItem(current))
            }
        )
    }

    fun parsePairs(input: List<String>): List<Pair<ListItem, ListItem>> =
        input
            .filter { it != "" }
            .map { parseItem(it) }
            .chunked(2)
            .map { (first, second) -> first to second }

    fun part1(input: List<String>): Int =
        parsePairs(input)
            .mapIndexed { index, (first, second) -> if (first < second) index + 1 else 0 }
            .sum()


    fun part2(input: List<String>): Int {
        val firstDivider = ListItem(listOf(IntItem(2)))
        val secondDivider = ListItem(listOf(IntItem(6)))

        val sortedPackets = (parsePairs(input)
            .flatMap { it.toList() } + listOf(firstDivider, secondDivider))
            .sortedWith(ListItem::compareTo)

        return (sortedPackets.indexOf(firstDivider) + 1) * (sortedPackets.indexOf(secondDivider) + 1)
    }


    val testInput = readInput("/day13/Day13_test")

    println(part1(testInput))
    println(part2(testInput))

//check(part1(testInput) == 2)
//check(part2(testInput) == 4)


    val input = readInput("/day13/Day13")
    println(part1(input))
    println(part2(input))
}
