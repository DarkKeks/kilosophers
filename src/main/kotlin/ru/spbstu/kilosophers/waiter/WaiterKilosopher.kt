package ru.spbstu.kilosophers.waiter

import ru.spbstu.kilosophers.*
import ru.spbstu.kilosophers.waiter.WaiterKilosopher.State.*

class WaiterKilosopher(left: Fork, right: Fork, napkins: NapkinSet, private val waiter: Waiter)
    : AbstractKilosopher(left, right, napkins) {

    internal enum class State {
        WAITS_BOTH,
        WAITS_RIGHT,
        WAITS_NAPKIN,
        EATS,
        HOLDS_NAPKIN,
        HOLDS_BOTH,
        HOLDS_RIGHT,
        THINKS
    }

    private var state = WAITS_BOTH

    private fun tryTakeFork(action: Action) = if (waiter.takeFork()) action else ActionKind.THINK(100)

    override fun nextAction(): Action {
        return when (state) {
            WAITS_BOTH -> tryTakeFork(ActionKind.TAKE_LEFT(10))
            WAITS_RIGHT -> ActionKind.TAKE_RIGHT(10)
            WAITS_NAPKIN -> ActionKind.TAKE_NAPKIN(10)
            EATS -> ActionKind.EAT(50)
            HOLDS_NAPKIN -> ActionKind.DROP_NAPKIN(10)
            HOLDS_BOTH -> ActionKind.DROP_LEFT(10)
            HOLDS_RIGHT -> ActionKind.DROP_RIGHT(10)
            THINKS -> ActionKind.THINK(100)
        }
    }

    override fun handleResult(action: Action, result: Boolean) {
        when (action.kind) {
            ActionKind.DROP_LEFT, ActionKind.DROP_RIGHT -> if (result) waiter.dropFork()
            ActionKind.TAKE_LEFT -> if (!result) waiter.dropFork()
            else -> {}
        }

        state = when (action.kind) {
            ActionKind.TAKE_LEFT -> if (result) WAITS_RIGHT else WAITS_BOTH
            ActionKind.TAKE_RIGHT -> if (result) WAITS_NAPKIN else WAITS_RIGHT
            ActionKind.TAKE_NAPKIN -> if (result) EATS else WAITS_NAPKIN
            ActionKind.EAT -> HOLDS_NAPKIN
            ActionKind.DROP_NAPKIN -> if (result) HOLDS_BOTH else HOLDS_NAPKIN
            ActionKind.DROP_LEFT -> if (result) HOLDS_RIGHT else HOLDS_BOTH
            ActionKind.DROP_RIGHT -> if (result) THINKS else HOLDS_RIGHT
            ActionKind.THINK -> WAITS_BOTH
        }
    }

}