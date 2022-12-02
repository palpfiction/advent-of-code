package day02

import readInput

sealed interface Choice

object Rock : Choice
object Paper : Choice
object Scissors : Choice

sealed interface Outcome

object Win : Outcome
object Lose : Outcome
object Draw : Outcome

fun main() {

    fun Choice.score() = when (this) {
        Rock -> 1
        Paper -> 2
        Scissors -> 3
    }

    fun Choice.againstValue(other: Choice) = when {
        this == Rock && other == Paper -> 0
        this == Rock && other == Scissors -> 6
        this == Paper && other == Scissors -> 0
        this == Paper && other == Rock -> 6
        this == Scissors && other == Rock -> 0
        this == Scissors && other == Paper -> 6
        else -> 3
    }

    fun computeTurnScore(opponent: Choice, mine: Choice): Int = mine.againstValue(opponent) + mine.score()

    fun String.toOpponentChoice() = when (this) {
        "A" -> Rock
        "B" -> Paper
        "C" -> Scissors
        else -> throw Error("Error parsing opponent")
    }

    fun String.toMyChoice() = when (this) {
        "X" -> Rock
        "Y" -> Paper
        "Z" -> Scissors
        else -> throw Error("Error parsing my choice")
    }

    fun parseLine(line: String): Pair<Choice, Choice> {
        val (opponent, mine) = line.split(" ")
        return Pair(opponent.toOpponentChoice(), mine.toMyChoice())
    }

    fun part1(input: List<String>): Int =
        input
            .map { line -> parseLine(line) }
            .sumOf { (opponent, mine) -> computeTurnScore(opponent, mine) }


    fun String.toOutcome() = when (this) {
        "X" -> Lose
        "Y" -> Draw
        "Z" -> Win
        else -> throw Error("Error parsing outcome")
    }

    fun Outcome.opposite() = when (this) {
        Draw -> Draw
        Win -> Lose
        Lose -> Win
    }


    fun Choice.against(other: Choice): Outcome = when {
        this == other -> Draw
        this == Rock && other == Paper -> Lose
        this == Rock && other == Scissors -> Win
        this == Paper && other == Scissors -> Lose
        else -> other.against(this).opposite()
    }

    fun getChoice(opponent: Choice, outcome: Outcome) =
        listOf(Rock, Paper, Scissors)
            .find { it.against(opponent) == outcome }!!


    fun parseLine2(line: String): Pair<Choice, Outcome> {
        val (choice, outcome) = line.split(" ")
        return Pair(choice.toOpponentChoice(), outcome.toOutcome())
    }

    fun part2(input: List<String>): Int =
        input
            .map { parseLine2(it) }
            .sumOf { (choice, outcome) -> computeTurnScore(choice, getChoice(choice, outcome)) }


    val testInput = readInput("/day02/Day02_test")

    check(part1(testInput) == 15)
    check(part2(testInput) == 12)


    val input = readInput("/day02/Day02")
    println(part1(input))
    println(part2(input))
}
