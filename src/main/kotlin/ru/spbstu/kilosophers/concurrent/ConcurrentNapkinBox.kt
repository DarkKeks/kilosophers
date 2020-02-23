package ru.spbstu.kilosophers.concurrent

import ru.spbstu.kilosophers.NapkinBox

object ConcurrentNapkinBox : NapkinBox {
    override fun produce(napkinCount: Int) = ConcurrentNapkinSet(napkinCount)
}