package day03

import readInput

fun main() {

    val priorities =
        (('a'..'z')
            .mapIndexed { index, it -> it to index + 1 } +
                ('A'..'Z')
                    .mapIndexed { index, it -> it to index + 27 }).toMap()


    fun part1(input: List<String>): Int =
        input
            .asSequence()
            .map { it.toList() }
            .map { it.chunked(it.size / 2) }
            .map { (compartment1, compartment2) -> compartment1.intersect(compartment2.toSet()) }
            .map { it.single() }
            .sumOf { priorities[it]!! }


    fun part2(input: List<String>): Int =
        input
            .chunked(3)
            .map { (x, y, z) -> x.toSet().intersect(y.toSet()).intersect(z.toSet()) }
            .map { it.single() }
            .sumOf { priorities[it]!! }


    val testInput = readInput("/day03/Day03_test")

    println(part1(testInput))
    println(part2(testInput))

    check(part1(testInput) == 157)
    check(part2(testInput) == 70)


    val input = readInput("/day03/Day03")
    println(part1(input))
    println(part2(input))
}
