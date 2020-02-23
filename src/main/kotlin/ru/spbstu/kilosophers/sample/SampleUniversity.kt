package ru.spbstu.kilosophers.sample

import ru.spbstu.kilosophers.AbstractFork
import ru.spbstu.kilosophers.AbstractKilosopher
import ru.spbstu.kilosophers.NapkinSet
import ru.spbstu.kilosophers.University

object SampleUniversity : University {
    override fun produce(left: AbstractFork, right: AbstractFork, napkins: NapkinSet, vararg args: Any): AbstractKilosopher {
        val kilosopher = SampleKilosopher(left, right, napkins, args[0] as Int)
        left.right = kilosopher
        right.left = kilosopher
        return kilosopher
    }
}