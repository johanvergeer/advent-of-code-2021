data class BingoBoard(private val numbers: List<List<Int>>) {
    private val markedNumbers = numbers.map { row -> row.map { false }.toMutableList() }.toMutableList()

    fun mark(number: Int) {
        for (i in numbers.indices) {
            for (j in numbers[0].indices) {
                if (numbers[i][j] == number) {
                    markedNumbers[i][j] = true
                    return
                }
            }
        }
    }

    fun clear() {
        for (i in markedNumbers.indices) {
            for (j in markedNumbers[0].indices) {
                markedNumbers[i][j] = false
            }
        }
    }

    fun getFullRows(): List<List<Int>> = numbers.filterIndexed { index, _ -> markedNumbers[index].all { it } }

    fun getFullColumns(): List<List<Int>> {
        val markedNumbersTransposed = markedNumbers.transpose()
        return numbers.transpose().filterIndexed { index, _ -> markedNumbersTransposed[index].all { it } }
    }

    val numberOfWins: Int
        get() = getFullRows().size + getFullColumns().size

    private val unmarkedNumbers: List<Int>
        get() {
            val flattenedMarkedNumbers = markedNumbers.flatten()
            return numbers.flatten().filterIndexed { index, _ -> !flattenedMarkedNumbers[index] }
        }

    val unmarkedNumbersSum: Int
        get() = unmarkedNumbers.sum()
}

private fun createBoards(input: List<String>): List<BingoBoard> {
    val boards = mutableListOf<BingoBoard>()

    var numbersForBoard = mutableListOf<List<Int>>()
    for (line in input.subList(2, input.size - 1)) {
        if (line == "") {
            boards.add(BingoBoard(numbersForBoard))
            numbersForBoard = mutableListOf()
            continue
        }

        numbersForBoard.add(line.split(" ").filter { it.trim() != "" }.map { it.toInt() })
    }

    return boards.toList()
}

private fun getDrawnNumbers(input: List<String>) = input[0].split(",").map { it.toInt() }

fun part1(input: List<String>): Int {
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


fun part2(input: List<String>): Int {
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
    println(part2(input))
}