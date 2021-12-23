fun <T> List<List<T>>.transpose(): List<List<T>> {
    val ret: MutableList<List<T>> = ArrayList()
    val rowSize: Int = this[0].size

    for (i in 0 until rowSize) {
        val col: MutableList<T> = ArrayList()
        mapTo(col) { it[i] }
        ret.add(col)
    }

    return ret
}

private fun String.invertBits() = map { if (it == '1') '0' else '1' }.joinToString(separator = "")

private fun gammaRateBits(input: List<List<Int>>) = input
    .map { row -> row.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key }
    .joinToString(separator = "")

fun gammaRate(input: List<List<Int>>) = gammaRateBits(input).toInt(2)

fun epsilonRate(input: List<List<Int>>) = gammaRateBits(input)
    .invertBits()
    .toInt(2)

private fun Map<Int, Int>.areValuesEqual() = values.all { it == values.first() }

private fun airRating(input: List<List<Int>>, selector: (Map<Int, Int>) -> Int): Int {
    val rowSize = input[0].size
    var rows = input.map { row -> row.map { it } }

    for (i in 0 until rowSize) {
        val itemsCountsInColumn = rows.map { it[i] }.groupingBy { it }.eachCount()

        rows = rows.filter { it[i] == selector(itemsCountsInColumn) }

        if (rows.size == 1)
            return rows[0].joinToString("").toInt(2)
    }

    throw IllegalStateException("Only one row should remain")
}

private fun valueWithHighestCount(itemsCountsInColumn: Map<Int, Int>) =
    if (itemsCountsInColumn.areValuesEqual()) 1
    else itemsCountsInColumn.maxByOrNull { it.value }?.key!!

private fun valueWithLowestCount(itemsCountsInColumn: Map<Int, Int>) =
    if (itemsCountsInColumn.areValuesEqual()) 0
    else itemsCountsInColumn.minByOrNull { it.value }?.key!!

fun oxygenGeneratorRating(input: List<List<Int>>) = airRating(input, ::valueWithHighestCount)

fun co2ScrubberRating(input: List<List<Int>>) = airRating(input, ::valueWithLowestCount)

fun part1(input: List<List<Int>>) = gammaRate(input) * epsilonRate(input)

fun part2(input: List<List<Int>>) = oxygenGeneratorRating(input) * co2ScrubberRating(input)

fun main() {

    val input = readInput("Day03")
        .map { it.toList().map { digit -> digit.digitToInt() } }
    val transposedInput = input.transpose()

    println(oxygenGeneratorRating(input))
    println(co2ScrubberRating(input))

    println(part1(transposedInput)) // 3901196
    println(part2(input)) // 4412188
}