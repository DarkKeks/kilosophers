package ru.spbstu.kilosophers

interface NapkinSet {

    val count: Int

    val used: Int

    suspend fun tryTake(): Boolean

    suspend fun tryDrop(): Boolean
}

