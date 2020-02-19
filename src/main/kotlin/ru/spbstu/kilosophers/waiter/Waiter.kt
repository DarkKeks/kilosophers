package ru.spbstu.kilosophers.waiter

import java.util.concurrent.atomic.AtomicInteger

class Waiter(private val forkCount: Int) {

    private var forksInUse = AtomicInteger()

    fun takeFork(): Boolean {
        if (forksInUse.incrementAndGet() >= forkCount) {
            forksInUse.decrementAndGet()
            return false
        }
        return true
    }

    fun dropFork() {
        forksInUse.getAndDecrement()
    }
}