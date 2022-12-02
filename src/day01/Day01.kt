fun main() {
    data class Elf(val calories: Int)

    fun parseElves(input: List<String>): List<Elf> {
        val elves = mutableListOf<Elf>()
        var currentElf = Elf(0)

        input.forEach {
            when {
                it.isEmpty() -> elves.add(currentElf.copy()).also { currentElf = Elf(0) }
                else -> currentElf = currentElf.copy(calories = currentElf.calories + it.toInt())
            }
        }

        elves.add(currentElf)

        return elves
    }

    fun part1(input: List<String>): Int =
        parseElves(input).maxBy { it.calories }.calories


    fun part2(input: List<String>): Int =
        parseElves(input).sortedByDescending { it.calories }.take(3).sumOf { it.calories }


    // test if implementation meets criteria from the description, like:

    val testInput = readInput("/day01/Day01_test")

    check(part1(testInput) == 24000)

    val input = readInput("/day01/Day01")
    println(part1(input))
    println(part2(testInput))
    println(part2(input))
}
