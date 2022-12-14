package day14

import readInput


data class Coordinate(val x: Int, val y: Int) {
    override fun toString() = "($x, $y)"
}

fun main() {

    val sandSource = Coordinate(500, 1)

    fun getRangeOf(first: Int, second: Int): IntProgression =
        if (first < second) first.rangeTo(second) else second.rangeTo(first).reversed()


    fun Coordinate.traceLineTo(other: Coordinate): List<Coordinate> =
        if (this.x == other.x) {
            getRangeOf(this.y, other.y)
                .drop(1)
                .map { this.copy(y = it) }
        } else {
            getRangeOf(this.x, other.x)
                .drop(1)
                .map { this.copy(x = it) }
        }


    fun parseRockPath(input: String): List<Coordinate> =
        input
            .replace(" ", "")
            .split("->")
            .map { it.split(",") }
            .map { (x, y) -> Coordinate(x.toInt(), y.toInt()) }
            .fold(listOf()) { acc, coordinate ->
                acc + (acc.lastOrNull()?.traceLineTo(coordinate) ?: listOf(coordinate))
            }


    fun parseRocks(input: List<String>): List<Coordinate> =
        input
            .map { parseRockPath(it) }
            .flatten()

    fun Coordinate.isOutOfBounds(bounds: Pair<Coordinate, Coordinate>): Boolean =
        this.x !in bounds.first.x..bounds.second.x || this.y !in bounds.first.y..bounds.second.y

    fun dropSand(items: List<Coordinate>, shouldStop: (Coordinate) -> Boolean): Coordinate? {
        var currentPosition = sandSource

        while (!shouldStop(currentPosition)) {
            val downwards = currentPosition.copy(y = currentPosition.y + 1)
            println("testing downwards $downwards")
            currentPosition = if (!items.contains(downwards)) {
                downwards
            } else {
                val leftDiagonal = currentPosition.copy(x = currentPosition.x - 1, y = currentPosition.y + 1)
                println("nope, testing left diagonal $leftDiagonal")
                if (!items.contains(leftDiagonal)) {
                    leftDiagonal
                } else {
                    val rightDiagonal = currentPosition.copy(x = currentPosition.x + 1, y = currentPosition.y + 1)
                    println("nope, testing right diagonal $rightDiagonal")
                    if (!items.contains(rightDiagonal)) {
                        rightDiagonal
                    } else {
                        // rest
                        println("rest at $currentPosition")

                        return currentPosition
                    }
                }
            }
        }

        println("out of bounds")
        return null
    }

    fun computeBounds(rocks: List<Coordinate>): Pair<Coordinate, Coordinate> =
        Coordinate(rocks.minBy { it.x }.x, 0) to Coordinate(rocks.maxBy { it.x }.x, rocks.maxBy { it.y }.y)

    fun part1(input: List<String>): Int {
        val rocks = parseRocks(input)
        val bounds = computeBounds(rocks)
        val items = rocks.toMutableList()

        println(bounds)
        do {
            val lastSandAdded = dropSand(items) { it.isOutOfBounds(bounds) }
            if (lastSandAdded != null) items.add(lastSandAdded).also { println("added $lastSandAdded") }
        } while (lastSandAdded != null)

        return items.size - rocks.size
    }


    fun part2(input: List<String>): Int {
        val rocks = parseRocks(input)
        val bounds = Coordinate(-Int.MAX_VALUE, 0) to Coordinate(Int.MAX_VALUE, rocks.maxBy { it.y }.y + 2)
        val items = rocks.toMutableList()

        println(bounds)
        do {
            val lastSandAdded = dropSand(items) { it.y }
            if (lastSandAdded != null) items.add(lastSandAdded).also { println("added $lastSandAdded") }
        } while (lastSandAdded != null)

        return items.size - rocks.size


        TODO()
    }


    val testInput = readInput("/day14/Day14_test")

    //println(part1(testInput))
    //println(part2(testInput))

    //check(part1(testInput) == 2)
    //check(part2(testInput) == 4)


    val input = readInput("/day14/Day14")
    println(part1(input))
    //println(part2(input))
}
