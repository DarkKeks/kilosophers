package ru.spbstu.kilosophers.waiter

import ru.spbstu.kilosophers.AbstractKilosopher
import ru.spbstu.kilosophers.Action
import ru.spbstu.kilosophers.ActionKind
import ru.spbstu.kilosophers.Fork
import ru.spbstu.kilosophers.waiter.WaiterKilosopher.State.*

class WaiterKilosopher(left: Fork, right: Fork, private val waiter: Waiter, val id: Int) : AbstractKilosopher(left, right) {

    internal enum class State {
        WAITS_BOTH,
        WAITS_RIGHT,
        EATS,
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
            EATS -> ActionKind.EAT(50)
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
            ActionKind.TAKE_RIGHT -> if (result) EATS else WAITS_RIGHT
            ActionKind.EAT -> HOLDS_BOTH
            ActionKind.DROP_LEFT -> if (result) HOLDS_RIGHT else HOLDS_BOTH
            ActionKind.DROP_RIGHT -> if (result) THINKS else HOLDS_RIGHT
            ActionKind.THINK -> WAITS_BOTH
        }
    }

}