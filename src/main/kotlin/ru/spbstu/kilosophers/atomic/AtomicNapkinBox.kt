package ru.spbstu.kilosophers.atomic

import ru.spbstu.kilosophers.NapkinBox

object AtomicNapkinBox : NapkinBox {
    override fun produce(napkinCount: Int) = AtomicNapkinSet(napkinCount)
}