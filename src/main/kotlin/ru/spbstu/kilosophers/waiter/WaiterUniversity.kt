package ru.spbstu.kilosophers.waiter

import ru.spbstu.kilosophers.AbstractFork
import ru.spbstu.kilosophers.University

class WaiterUniversity(forkCount: Int) : University {

    private val waiter = Waiter(forkCount)

    override fun produce(left: AbstractFork, right: AbstractFork, vararg args: Any) =
            WaiterKilosopher(left, right, waiter, args[0] as Int).also {
                left.right = it
                right.left = it
            }
}