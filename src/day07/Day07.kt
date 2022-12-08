package day07

import readInput
import java.lang.IllegalArgumentException

sealed class File(val name: String, val parent: Directory?) {
    abstract val size: Int
}

class Directory(
    name: String,
    parent: Directory? = null,
) : File(name, parent) {
    var contents: MutableList<File> = mutableListOf()
    override val size
        get() = contents.sumOf { it.size }
}

class RegularFile(name: String, parent: Directory?, override val size: Int) : File(name, parent)

sealed class Command

class ChangeDirectory(
    val destination: String
) : Command()

object ListDirectory : Command()

fun main() {

    val commandRegex = """\$ (\w+).?(.+)?""".toRegex()
    val regularFileRegex = """(\d+) (.+)""".toRegex()
    val directoryRegex = """dir (.+)""".toRegex()

    fun isCommand(line: String) = line.matches(commandRegex)

    fun parseCommand(line: String): Command {
        if (!isCommand(line)) throw IllegalArgumentException()

        val (_, type, argument) = commandRegex.find(line)!!.groupValues

        return when (type) {
            "cd" -> ChangeDirectory(argument)
            "ls" -> ListDirectory
            else -> throw IllegalArgumentException()
        }
    }


    fun changeDirectory(root: Directory, current: Directory, destination: String) = when (destination) {
        "/" -> root
        ".." -> current.parent
        else -> current.contents.filterIsInstance<Directory>().first { it.name == destination }
    } ?: throw IllegalArgumentException()

    fun parseRegularFile(file: String, current: Directory): RegularFile {
        val (_, size, name) = regularFileRegex.find(file)!!.groupValues
        return RegularFile(name, current, size.toInt())
    }

    fun parseDirectory(file: String, current: Directory): Directory {
        val (_, name) = directoryRegex.find(file)!!.groupValues
        return Directory(name, current)
    }

    fun parseFile(file: String, current: Directory): File = when {
        file.matches(regularFileRegex) -> parseRegularFile(file, current)
        file.matches(directoryRegex) -> parseDirectory(file, current)
        else -> throw IllegalArgumentException()
    }


    fun parseFileSystem(input: List<String>): Directory {
        val root = Directory("/")
        var currentDirectory = root
        val directories = mutableListOf(root)
        var currentCommand: Command

        var index = 0

        while (index < input.size) {
            currentCommand = parseCommand(input[index])
            index++

            when (currentCommand) {
                is ChangeDirectory -> currentDirectory =
                    changeDirectory(root, currentDirectory, currentCommand.destination)

                is ListDirectory ->
                    currentDirectory.contents.addAll(
                        input
                            .drop(index)
                            .takeWhile { !isCommand(it) }
                            .map { parseFile(it, currentDirectory) }
                            .also { index += it.size }
                            .also {
                                directories.addAll(it.filterIsInstance<Directory>())
                            }
                    )
            }
        }

        return root
    }

    fun getDirectories(directory: Directory): List<Directory> {
        val directories: List<Directory> = directory.contents.filterIsInstance<Directory>()

        return directories.plus(
            directories
                .map { getDirectories(it) }
                .flatten()
        )
    }


    fun part1(input: List<String>): Int =
        getDirectories(parseFileSystem(input))
            .filter { it.size <= 100_000 }
            .sumOf { it.size }


    fun part2(input: List<String>): Int {
        val root = parseFileSystem(input)
        val directories = getDirectories(root)
        val unusedSpace = 70_000_000 - root.size
        val neededSpace = 30_000_000 - unusedSpace

        return directories
            .filter { it.size >= neededSpace }
            .minByOrNull { it.size }!!
            .size
    }


    val testInput = readInput("/day07/Day07_test")


    println(part1(testInput))
    println(part2(testInput))

//check(part1(testInput) == 11)
//check(part2(testInput) == 70)


    val input = readInput("/day07/Day07")

    println(part1(input))
    println(part2(input))
}
