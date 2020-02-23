package ru.spbstu.kilosophers.concurrent

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.spbstu.kilosophers.NapkinSet
import java.util.concurrent.Executors

class ConcurrentNapkinSet(override val count: Int) : NapkinSet {
    override val used: Int
        get() = usedCount

    private var usedCount = 0

    private val napkinContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher() + CoroutineName("NapkinSet")

    override suspend fun tryTake(): Boolean {
        if (used >= count) return false
        return withContext(napkinContext) {
            if (used < count) {
                usedCount++
                true
            } else {
                false
            }
        }
    }

    override suspend fun tryDrop(): Boolean {
        if (used == 0) return false
        return withContext(napkinContext) {
            if (used > 0) {
                usedCount--
                true
            } else {
                false
            }
        }
    }
}