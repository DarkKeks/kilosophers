package ru.spbstu.kilosophers.waiter

import ru.spbstu.kilosophers.AbstractFork
import ru.spbstu.kilosophers.NapkinSet
import ru.spbstu.kilosophers.University

class WaiterUniversity(forkCount: Int) : University {

    private val waiter = Waiter(forkCount)

    override fun produce(left: AbstractFork, right: AbstractFork, napkins: NapkinSet, vararg args: Any) =
            WaiterKilosopher(left, right, napkins, waiter).also {
                left.right = it
                right.left = it
            }
}