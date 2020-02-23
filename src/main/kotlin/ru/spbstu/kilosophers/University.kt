package ru.spbstu.kilosophers

// Produces kilosophers
interface University {
    fun produce(left: AbstractFork, right: AbstractFork, napkins: NapkinSet, vararg args: Any): AbstractKilosopher
}