package day04

import readInput

fun main() {

    fun expand(assignment: String) =
        with(assignment.split("-")) {
            (this.component1().toInt()..this.component2().toInt()).toSet()
        }


    fun part1(input: List<String>): Int =
        input.map { it.split(",") }
            .map { (first, second) -> expand(first) to expand(second) }
            .count { (first, second) -> first.subtract(second).isEmpty() || second.subtract(first).isEmpty() }


    fun part2(input: List<String>): Int =
        input.map { it.split(",") }
            .map { (first, second) -> expand(first) to expand(second) }
            .count { (first, second) -> first.intersect(second).isNotEmpty() || second.intersect(first).isNotEmpty() }


    val testInput = readInput("/day04/Day04_test")

    println(part1(testInput))
    println(part2(testInput))

    check(part1(testInput) == 2)
    check(part2(testInput) == 4)


    val input = readInput("/day04/Day04")
    println(part1(input))
    println(part2(input))
}
