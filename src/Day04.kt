data class BingoField(val number: Int, val marked: Boolean)

data class BingoBoard(private val fields: List<MutableList<BingoField>>) {

    fun mark(number: Int) =
        this.fields.forEach { row ->
            row.replaceAll { if (it.number == number) it.copy(marked = true) else it }
        }

    fun clear() =
        this.fields.forEach { row ->
            row.replaceAll { it.copy(marked = false) }
        }

    fun getFullRows() = fields.filter { row -> row.all { it.marked } }

    fun getFullColumns() = fields.transpose().filter { column -> column.all { it.marked } }

    val numberOfWins: Int
        get() = getFullRows().size + getFullColumns().size

    private val unmarkedNumbers: List<BingoField>
        get() = fields.flatten().filter { number -> !number.marked }

    val unmarkedNumbersSum: Int
        get() = unmarkedNumbers.sumOf { it.number }
}

private fun Sequence<String>.dropLineWithDrawnNumbers() = this.drop(1)
private fun Sequence<Iterable<String>>.dropEmptyLine() = this.map { it.drop(1) }
private fun Sequence<Iterable<String>>.cleanRows() = this.map {
    it.map { rows ->
        rows.split(" ").filter { value -> value.trim() != "" }.map { number -> number.toInt() }
    }
}
private fun Sequence<Iterable<Iterable<Int>>>.toBingoFields() =
    this.map { it.map { rows -> rows.map { number -> BingoField(number, false) } } }
        .map { it.map { rows -> rows.toMutableList() } }

private fun createBoards(input: Iterable<String>) =
    input
        .asSequence()
        .dropLineWithDrawnNumbers()
        .chunked(6)
        .dropEmptyLine()
        .cleanRows()
        .toBingoFields()
        .map { BingoBoard(it) }
        .toList()

private fun getDrawnNumbers(input: Iterable<String>) = input.first().split(",").map { it.toInt() }

fun part1(input: Iterable<String>): Int {
    val boards = createBoards(input)

    for (drawnNumber in getDrawnNumbers(input)) {
        for (board in boards) {
            board.mark(drawnNumber)

            if (board.getFullColumns().isNotEmpty() || board.getFullRows().isNotEmpty()) {
                return board.unmarkedNumbersSum * drawnNumber
            }
        }
    }

    throw IllegalStateException("None of the bingo boards has won!")
}


fun part2(input: Iterable<String>): Int {
    val boards = createBoards(input)

    val winningBoards = mutableListOf<BingoBoard>()

    for (drawnNumber in getDrawnNumbers(input)) {
        for (board in boards) {
            board.mark(drawnNumber)

            if (board.numberOfWins > 0 && !winningBoards.contains(board)) {
                winningBoards.add(board)
            }
        }
    }

    val lastWinningBoard = winningBoards.last()
    lastWinningBoard.clear()

    for (drawnNumber in getDrawnNumbers(input)) {
        lastWinningBoard.mark(drawnNumber)

        if (lastWinningBoard.numberOfWins > 0) {
            return lastWinningBoard.unmarkedNumbersSum * drawnNumber
        }
    }

    throw IllegalStateException("None of the bingo boards has won!")
}


fun main() {
    val input = readInput("Day04")

    println(part1(input)) // 54275
    println(part2(input))  // 13158
}