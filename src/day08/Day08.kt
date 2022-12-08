package day08

import readInput

typealias Matrix<T> = MutableList<MutableList<T>>

fun emptyMatrix(x: Int, y: Int) = MutableList(x) { MutableList(y) { 0 } }

fun <T> Matrix<T>.dimensions() = this.size to this[0].size

fun <T> Matrix<T>.print() {
    val (rows, columns) = this.dimensions()
    for (j in 0 until columns) {
        for (i in 0 until rows) {
            print(this[i][j])
        }
        println()
    }
}


fun main() {
    fun dimensions(input: List<String>): Pair<Int, Int> = input[0].toCharArray().size to input.size

    fun parseGrid(input: List<String>): Matrix<Int> {
        val (rows, columns) = dimensions(input)

        val grid = emptyMatrix(rows, columns)
        input
            .forEachIndexed { y, line ->
                line.toCharArray()
                    .forEachIndexed { x, tree ->
                        grid[x][y] = tree.digitToInt()

                    }
            }

        return grid
    }

    fun Matrix<Int>.maxTop(x: Int, y: Int): Int {
        var max = 0

        for (j in y - 1 downTo 0) {
            with(this[x][j]) {
                if (this > max) max = this
            }
        }

        return max
    }

    fun Matrix<Int>.maxBottom(x: Int, y: Int): Int {
        val (_, columns) = this.dimensions()
        var max = 0

        for (j in y + 1 until columns) {
            with(this[x][j]) {
                if (this > max) max = this
            }
        }

        return max
    }

    fun Matrix<Int>.maxLeft(x: Int, y: Int): Int {
        var max = 0

        for (i in x - 1 downTo 0) {
            with(this[i][y]) {
                if (this > max) max = this
            }
        }

        return max
    }

    fun Matrix<Int>.maxRight(x: Int, y: Int): Int {
        val (rows) = this.dimensions()

        var max = 0

        for (i in x + 1 until rows) {
            with(this[i][y]) {
                if (this > max) max = this
            }
        }

        return max
    }

    fun Matrix<Int>.isVisible(x: Int, y: Int): Boolean {
        val (rows, columns) = this.dimensions()

        if (x == 0 || y == 0 || x == rows - 1 || y == columns - 1) return true

        return this[x][y] > maxTop(x, y) ||
                this[x][y] > maxBottom(x, y) ||
                this[x][y] > maxRight(x, y) ||
                this[x][y] > maxLeft(x, y)
    }

    fun part1(input: List<String>): Int {
        val grid = parseGrid(input)
        val (rows, columns) = grid.dimensions()

        var visibleTrees = 0

        for (x in 0 until rows) {
            for (y in 0 until columns) {
                if (grid.isVisible(x, y)) visibleTrees++
            }
        }

        return visibleTrees
    }

    fun Matrix<Int>.viewsTop(x: Int, y: Int): Int {
        val currentHeight = this[x][y]
        var trees = 0

        for (j in y - 1 downTo 0) {
            with(this[x][j]) {
                if (this >= currentHeight) {
                    return ++trees
                } else trees++
            }
        }

        return trees
    }

    fun Matrix<Int>.viewsBottom(x: Int, y: Int): Int {
        val (_, columns) = this.dimensions()
        val currentHeight = this[x][y]
        var trees = 0

        for (j in y + 1 until columns) {
            with(this[x][j]) {
                if (this >= currentHeight) {
                    return ++trees
                } else trees++
            }
        }

        return trees
    }

    fun Matrix<Int>.viewsLeft(x: Int, y: Int): Int {
        val currentHeight = this[x][y]
        var trees = 0

        for (i in x - 1 downTo 0) {
            with(this[i][y]) {
                if (this >= currentHeight) {
                    return ++trees
                } else trees++
            }
        }

        return trees
    }

    fun Matrix<Int>.viewsRight(x: Int, y: Int): Int {
        val (rows) = this.dimensions()
        val currentHeight = this[x][y]
        var trees = 0

        for (i in x + 1 until rows) {
            with(this[i][y]) {
                if (this >= currentHeight) {
                    return ++trees
                } else {
                    trees++
                }
            }
        }

        return trees
    }

    fun Matrix<Int>.scenicScore(x: Int, y: Int): Int =
        this.viewsTop(x, y) * this.viewsBottom(x, y) * this.viewsLeft(x, y) * this.viewsRight(x, y)

    fun part2(input: List<String>): Int {
        val grid = parseGrid(input)
        val (rows, columns) = grid.dimensions()

        var maxScenicScore = 0

        for (x in 0 until rows) {
            for (y in 0 until columns) {
                with(grid.scenicScore(x, y)) {
                    if (this > maxScenicScore) maxScenicScore = this
                }
            }
        }

        return maxScenicScore
    }


    val testInput = readInput("/day08/Day08_test")


    //  println(part1(testInput))
    println(part2(testInput))

    check(part1(testInput) == 21)
    check(part2(testInput) == 8)


    val input = readInput("/day08/Day08")
    //println(part1(input))
    println(part2(input))
}
