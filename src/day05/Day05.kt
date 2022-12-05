package day05

import readInput
import java.util.Stack

typealias CrateStack = Stack<Char>
typealias Arrangement = List<CrateStack>
typealias Command = Triple<Int, Int, Int>

inline fun <reified T> transpose(matrix: List<List<T>>): List<List<T>> {
    return List(matrix[0].size) { col ->
        List(matrix.size) { row ->
            matrix[row][col]
        }
    }
}

fun main() {

    val arrangementRegex = """.(.).\s?""".toRegex()
    val commandRegex = """move (\d*) from (\d*) to (\d*)""".toRegex()

    fun <T> List<T>.toStack(): Stack<T> {
        val stack = Stack<T>()

        this.forEach { stack.push(it) }

        return stack
    }

    fun parseLine(line: String): List<Char> {
        return arrangementRegex.findAll(line)
            .map { it.groupValues[1] }
            .map { it[0] }
            .toList()
    }


    fun parseStacks(input: List<String>): Arrangement {
        val arrangement = input.takeWhile { it.isNotEmpty() }
            .dropLast(1)
            .map { parseLine(it) }

        return transpose(arrangement)
            .map {
                it.filter { char -> char != ' ' }
                    .asReversed()
                    .toStack()
            }
    }

    fun parseCommands(input: List<String>): List<Command> =
        input
            .dropWhile { it.isNotEmpty() }
            .drop(1)
            .map { commandRegex.find(it)!!.groupValues }
            .map { (_, x, y, z) -> Command(x.toInt(), y.toInt(), z.toInt()) }


    fun Command.execute(stacks: Arrangement): Arrangement {
        repeat(this.first) {
            stacks[this.third - 1].push(stacks[this.second - 1].pop())
        }
        return stacks
    }

    fun part1(input: List<String>): String {
        val stacks = parseStacks(input)
        val commands = parseCommands(input)

        var currentArrangement = stacks
        for (command in commands) {
            currentArrangement = command.execute(currentArrangement)
        }





        return currentArrangement
            .map { it.last() }
            .joinToString("", "", "")
    }


    fun part2(input: List<String>): Int =
        TODO()

    val testInput = readInput("/day05/Day05_test")

    parseCommands(testInput)

    println(part1(testInput))
    //println(part2(testInput))

    check(part1(testInput) == "CMZ")
    //check(part2(testInput) == 4)


    val input = readInput("/day05/Day05")
    println(part1(input))
    //println(part2(input))
}
