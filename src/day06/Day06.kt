package day06

import readInput

fun main() {


    fun part1(input: String): Int =
        input
            .toCharArray()
            .asSequence()
            .windowed(4)
            .indexOfFirst { (a, b, c, d) -> setOf(a, b, c, d).size == 4 } + 4


    fun part2(input: String): Int =
        input
            .toCharArray()
            .asSequence()
            .windowed(14)
            .indexOfFirst { it.toSet().size == 14 } + 14


    val testInput = readInput("/day06/Day06_test").single()


    println(part1(testInput))
    println(part2(testInput))

    //check(part1(testInput) == 11)
    //check(part2(testInput) == 70)


    val input = readInput("/day06/Day06").single()

    println(part1(input))
    println(part2(input))
}
