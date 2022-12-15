package day15

import readInput
import kotlin.math.abs

data class Coordinate(val x: Int, val y: Int) {
    override fun toString() = "($x, $y)"
}

infix fun Coordinate.distanceTo(other: Coordinate) =
    abs(this.x - other.x) + abs(this.y - other.y)

sealed class Object(val coordinate: Coordinate) {
    infix fun distanceTo(other: Object) = this.coordinate distanceTo other.coordinate
    override fun toString() = coordinate.toString()
}


class Sensor(coordinate: Coordinate, private val closestBeacon: Beacon) : Object(coordinate) {
    fun covers(coordinate: Coordinate) = this.coordinate distanceTo coordinate <= this distanceTo this.closestBeacon

    fun minColumnCovered(row: Int) =
        this.coordinate.x - abs(this.distanceTo(this.closestBeacon) - abs(row - coordinate.y))

    fun maxColumnCovered(row: Int) =
        this.coordinate.x + abs(this.distanceTo(this.closestBeacon) - abs(row - coordinate.y))
}

class Beacon(coordinate: Coordinate) : Object(coordinate)

typealias Grid = Map<Int, List<Object>>

fun emptyGrid() = mapOf<Int, List<Object>>()

fun main() {

    val itemRegex = """.* x=(.+), y=(.+)""".toRegex()

    fun parseCoordinates(input: String): Coordinate {
        assert(itemRegex.matches(input))
        val (_, x, y) = itemRegex.matchEntire(input)!!.groupValues

        return Coordinate(x.toInt(), y.toInt())
    }

    fun parseSensor(input: String, closestBeacon: Beacon) = Sensor(parseCoordinates(input), closestBeacon)

    fun parseBeacon(input: String) = Beacon(parseCoordinates(input))

    fun parseLine(input: String): Pair<Sensor, Beacon> {
        val (sensorFragment, beaconFragment) = input.split(":")

        val beacon = parseBeacon(beaconFragment)

        return parseSensor(sensorFragment, beacon) to beacon

    }

    operator fun Grid.plus(obj: Object): Grid = this.plus(
        obj.coordinate.y to (this[obj.coordinate.y]?.plus(obj) ?: listOf(obj))
    )

    fun parseGrid(input: List<String>) =
        input
            .map { parseLine(it) }
            .fold(emptyGrid()) { grid, (sensor, beacon) -> grid.plus(sensor).plus(beacon) }

    fun Coordinate.isCoveredBySensor(sensors: List<Sensor>) = sensors.find { it.covers(this) }


    fun part1(input: List<String>): Int {
        val row = 2000000

        val grid = parseGrid(input)
        val sensors = grid
            .map { it.value }
            .flatten()
            .filterIsInstance<Sensor>()
        val beacons = grid
            .map { it.value }
            .flatten()
            .filterIsInstance<Beacon>()


        val minX = sensors.minOf { it.minColumnCovered(row) }
        val maxX = sensors.maxOf { it.maxColumnCovered(row) }

        var count = 0

        for (x in minX..maxX) {
            val coordinate = Coordinate(x, row)

            if (coordinate.isCoveredBySensor(sensors) != null &&
                sensors.find { it.coordinate == coordinate } == null &&
                beacons.find { it.coordinate == coordinate } == null
            ) {
                count++
            }

        }

        return count
    }


    fun part2(input: List<String>): Long {
        val max = 4_000_000

        val grid = parseGrid(input)
        val sensors = grid
            .map { it.value }
            .flatten()
            .filterIsInstance<Sensor>()


        for (row in 0..max) {
            var column = 0
            while (column <= max) {
                val coordinate = Coordinate(column, row)
                val sensor = coordinate.isCoveredBySensor(sensors)
                    ?: return coordinate.x.toLong() * 4_000_000 + coordinate.y.toLong()
                // we skip until the coordinate that sensor no longer covers (rest is covered)
                column = sensor.maxColumnCovered(row) + 1
            }
        }

        throw Error()
    }


    val testInput = readInput("/day15/Day15_test")


//    println(part1(testInput))
    //println(part2(testInput))

    //check(part1(testInput) == 2)
    //check(part2(testInput) == 4)


    val input = readInput("/day15/Day15")
    //println(part1(input))
    println(part2(input))
}
