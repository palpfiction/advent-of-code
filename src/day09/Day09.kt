package day09

import readInput
import java.lang.IllegalArgumentException
import kotlin.math.absoluteValue


enum class Direction {
    UP, DOWN, RIGHT, LEFT
}

fun String.toDirection(): Direction = when (this) {
    "U" -> Direction.UP
    "D" -> Direction.DOWN
    "R" -> Direction.RIGHT
    "L" -> Direction.LEFT
    else -> throw IllegalArgumentException()
}

data class Motion(val direction: Direction, val steps: Int)

data class Position(val x: Int, val y: Int) {
    override fun toString() = "($x, $y)"
}

fun Position.distanceFrom(position: Position): Distance {
    return this.x - position.x to this.y - position.y
}


data class State(val head: Position, val tail: Position) {
    override fun toString() = "head: $head; tail: $tail"
}

fun Position.move(direction: Direction): Position = when (direction) {
    Direction.UP -> this.copy(y = y + 1)
    Direction.DOWN -> this.copy(y = y - 1)
    Direction.RIGHT -> this.copy(x = x + 1)
    Direction.LEFT -> this.copy(x = x - 1)
}

typealias Distance = Pair<Int, Int>

fun Distance.isVertical(): Boolean = this.first == 0 && this.second != 0

fun Distance.isHorizontal(): Boolean = this.first != 0 && this.second == 0

fun main() {

    fun parseMotions(input: List<String>): List<Motion> =
        input.map { it.split(" ") }.map { (direction, steps) -> Motion(direction.toDirection(), steps.toInt()) }

    fun Position.shouldFollow(position: Position): Boolean {
        val distance = position.distanceFrom(this)

        return distance.first.absoluteValue >= 2 ||
                distance.second.absoluteValue >= 2
    }

    fun moveHead(state: State, direction: Direction) = state.copy(head = state.head.move(direction))

    fun Position.follow(position: Position): Position {
        if (!this.shouldFollow(position)) return this

        val distance = position.distanceFrom(this)

        val newTailPosition = when {
            distance.isHorizontal() -> {
                val (x) = distance
                if (x > 0) this.copy(x = this.x + 1)
                else this.copy(x = this.x - 1)
            }

            distance.isVertical() -> {
                val (_, y) = distance
                if (y > 0) this.copy(y = this.y + 1)
                else this.copy(y = this.y - 1)
            }
            // is diagonal
            else -> {
                val (x, y) = distance
                this.copy(x = this.x + x / x.absoluteValue, y = this.y + y / y.absoluteValue)
            }
        }

        return newTailPosition
    }

    fun moveTail(state: State) = state.copy(tail = state.tail.follow(state.head))

    fun part1(input: List<String>): Int {
        val initialState = State(Position(0, 0), Position(0, 0))
        val states = mutableListOf(initialState)

        parseMotions(input)
            .map { motion -> List(motion.steps) { motion.direction } }
            .flatten()
            .fold(initialState) { state, direction -> moveTail(moveHead(state, direction)).also { states.add(it) } }

        return states
            .distinctBy { it.tail }
            .count()
    }


    fun part2(input: List<String>): Int {
        val rope = MutableList(10) { Position(0, 0) }
        val tailPositions = mutableListOf(Position(0, 0))

        val directions = parseMotions(input)
            .map { motion -> List(motion.steps) { motion.direction } }
            .flatten()

        for (direction in directions) {
            rope.forEachIndexed { i, knot ->
                rope[i] = if (i == 0) knot.move(direction)
                else knot.follow(rope[i - 1])
            }

            tailPositions.add(rope[9])
        }

        return tailPositions
            .distinct()
            .count()

    }


    val testInput = readInput("/day09/Day09_test")

    println(part1(testInput))
    println(part2(testInput))

    //check(part1(testInput) == 157)
    //check(part2(testInput) == 70)


    val input = readInput("/day09/Day09")
    //println(part1(input))
    println(part2(input))
}
