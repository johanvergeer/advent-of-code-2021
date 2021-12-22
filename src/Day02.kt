data class SimplePosition(val depth: Int, val horizontalPosition: Int) {
    fun applyMovement(movement: Movement): SimplePosition {
        return when (movement.direction) {
            Direction.FORWARD -> copy(horizontalPosition = horizontalPosition + movement.units)
            Direction.DOWN -> copy(depth = depth + movement.units)
            Direction.UP -> copy(depth = depth - movement.units)
        }
    }
}

data class AimingPosition(val depth: Int, val horizontalPosition: Int, val aim: Int) {
    fun applyMovement(movement: Movement): AimingPosition {
        return when (movement.direction) {
            Direction.FORWARD -> copy(
                depth = depth + aim * movement.units,
                horizontalPosition = horizontalPosition + movement.units
            )
            Direction.DOWN -> copy(aim = aim + movement.units)
            Direction.UP -> copy(aim = aim - movement.units)
        }
    }
}


enum class Direction {
    FORWARD,
    DOWN,
    UP
}

data class Movement(val direction: Direction, val units: Int)

fun parseMovements(input: List<String>) =
    input
        .map { it.split(" ") }
        .map { Movement(Direction.valueOf(it[0].uppercase()), it[1].toInt()) }

fun main() {
    fun part1(input: List<String>): Int {
        val finalPosition = parseMovements(input)
            .fold(
                SimplePosition(0, 0)
            ) { position, movement -> position.applyMovement(movement) }

        return finalPosition.depth * finalPosition.horizontalPosition
    }

    fun part2(input: List<String>): Int {
        val finalPosition = parseMovements(input)
            .fold(
                AimingPosition(0, 0, 0)
            ) { position, movement -> position.applyMovement(movement) }

        return finalPosition.depth * finalPosition.horizontalPosition
    }

    val input = readInput("Day02")

    println(part1(input))
    println(part2(input))
}
