data class SimplePosition(val depth: Int, val horizontalPosition: Int) {
    private fun moveUp(units: Int) = SimplePosition(depth - units, horizontalPosition)
    private fun moveDown(units: Int) = SimplePosition(depth + units, horizontalPosition)
    private fun moveForward(units: Int) = SimplePosition(depth, horizontalPosition + units)

    fun applyMovement(movement: Movement): SimplePosition {
        return when (movement.direction) {
            Direction.FORWARD -> this.moveForward(movement.units)
            Direction.DOWN -> this.moveDown(movement.units)
            Direction.UP -> this.moveUp(movement.units)
        }
    }
}

data class AimingPosition(val depth: Int, val horizontalPosition: Int, val aim: Int) {
    private fun rotateUp(units: Int) = AimingPosition(depth, horizontalPosition, aim - units)
    private fun rotateDown(units: Int) = AimingPosition(depth, horizontalPosition, aim + units)
    private fun moveForward(units: Int) = AimingPosition(depth - aim * units, horizontalPosition + units, aim)

    fun applyMovement(movement: Movement): AimingPosition {
        return when (movement.direction) {
            Direction.FORWARD -> this.moveForward(movement.units)
            Direction.DOWN -> this.rotateUp(movement.units)
            Direction.UP -> this.rotateDown(movement.units)
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
        var position = SimplePosition(0, 0)

        parseMovements(input).forEach { position = position.applyMovement(it) }

        return position.depth * position.horizontalPosition
    }

    fun part2(input: List<String>): Int {
        var position = AimingPosition(0, 0, 0)

        parseMovements(input).forEach { position = position.applyMovement(it) }

        return position.depth * position.horizontalPosition
    }

    val input = readInput("Day02")

    println(part1(input))
    println(part2(input))
}
