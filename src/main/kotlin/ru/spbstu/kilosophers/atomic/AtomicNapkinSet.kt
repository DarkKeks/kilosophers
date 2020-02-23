package ru.spbstu.kilosophers.atomic

import ru.spbstu.kilosophers.NapkinSet
import java.util.concurrent.atomic.AtomicInteger

class AtomicNapkinSet(override val count: Int) : NapkinSet {
    override val used: Int
        get() = usedCount.get()

    private val usedCount = AtomicInteger(0)

    override suspend fun tryTake(): Boolean {
        if (usedCount.incrementAndGet() > count) {
            usedCount.decrementAndGet()
            return false
        }
        return true
    }

    override suspend fun tryDrop(): Boolean {
        if (usedCount.decrementAndGet() < 0) {
            usedCount.incrementAndGet()
            return false
        }
        return true
    }
}