fun getIncreasesCount(input: List<Int>, windowSize: Int = 2): Int {
    return input
        .windowed(windowSize, 1)
        .filter { p -> p.first() < p.last() }
        .size
}

fun main() {
    fun part1(input: List<String>) = getIncreasesCount(input.map { it.toInt() })

    fun part2(input: List<String>) = getIncreasesCount(input.map { it.toInt() }, 4)

    val input = readInput("Day01")

    println(part1(input))
    println(part2(input))
}
