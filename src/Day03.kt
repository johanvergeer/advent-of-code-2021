fun <T> Iterable<Collection<T>>.transpose(): List<List<T>> {
    val ret: MutableList<List<T>> = ArrayList()
    val rowSize: Int = this.first().size

    for (i in 0 until rowSize) {
        val col: MutableList<T> = ArrayList()
        mapTo(col) { it.elementAt(i) }
        ret.add(col)
    }

    return ret
}

private fun String.invertBits() = map { if (it == '1') '0' else '1' }.joinToString(separator = "")

private fun gammaRateBits(input: List<List<Int>>) = input
    .map { row -> row.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key }
    .joinToString(separator = "")

private fun gammaRate(input: List<List<Int>>) = gammaRateBits(input).toInt(2)

private fun epsilonRate(input: List<List<Int>>) = gammaRateBits(input)
    .invertBits()
    .toInt(2)

private data class BitCounts(val zeroes: Int, val ones: Int) {
    val valueWithHighestCount = if (ones >= zeroes) 1 else 0
    val valueWithLowestCount = if (ones > zeroes) 0 else 1
}

private fun Map<Int, Int>.toBitCounts() = BitCounts(this[0] ?: 0 , this[1] ?: 0)

private fun airRating(input: List<List<Int>>, selector: (BitCounts) -> Int): Int {
    val rowSize = input[0].size
    var rows = input.map { row -> row.map { it } }

    for (i in 0 until rowSize) {
        val itemsCountsInColumn = rows.map { it[i] }.groupingBy { it }.eachCount().toBitCounts()

        rows = rows.filter { it[i] == selector(itemsCountsInColumn) }

        if (rows.size == 1)
            return rows[0].joinToString("").toInt(2)
    }

    throw IllegalStateException("Only one row should remain")
}

private fun valueWithHighestCount(itemsCountsInColumn: BitCounts) = itemsCountsInColumn.valueWithHighestCount

private fun valueWithLowestCount(itemsCountsInColumn: BitCounts) = itemsCountsInColumn.valueWithLowestCount

private fun oxygenGeneratorRating(input: List<List<Int>>) = airRating(input, ::valueWithHighestCount)

private fun co2ScrubberRating(input: List<List<Int>>) = airRating(input, ::valueWithLowestCount)

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