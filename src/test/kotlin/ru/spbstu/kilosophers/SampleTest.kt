package ru.spbstu.kilosophers

import kotlinx.coroutines.*
import org.junit.Rule
import ru.spbstu.kilosophers.atomic.AtomicForkBox
import ru.spbstu.kilosophers.atomic.AtomicNapkinBox
import ru.spbstu.kilosophers.concurrent.ConcurrentForkBox
import ru.spbstu.kilosophers.concurrent.ConcurrentNapkinBox
import ru.spbstu.kilosophers.sample.SampleUniversity
import ru.spbstu.kilosophers.waiter.WaiterUniversity
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class SampleTest {

    @Rule
    @JvmField
    var expectedFailure = ExpectedFailure()

    private fun doTest(university: University, forkBox: ForkBox, napkinBox: NapkinBox, kilosopherCount: Int, napkinCount: Int, duration: Int) {
        val forks = MutableList(kilosopherCount) { forkBox.produce() }
        val napkins = napkinBox.produce(napkinCount)
        val kilosophers = mutableListOf<AbstractKilosopher>()
        for (index in 0 until kilosopherCount) {
            val leftFork = forks[index]
            val rightFork = forks[(index + 1) % kilosopherCount]
            val kilosopher = university.produce(leftFork, rightFork, napkins, index)
            kilosophers.add(kilosopher)
        }

        val jobs = kilosophers.map { it.act(duration) }
        var owners: List<AbstractKilosopher> = emptyList()

        val controllerJob = GlobalScope.launch {
            do {
                delay(maxOf(100, minOf(duration / 50, 1000)).toLong())
                owners = forks.mapNotNull { it.owner }.distinct()
            } while (owners.size < kilosopherCount)
        }

        runBlocking {
            jobs.forEach { it.join() }
            controllerJob.cancelAndJoin()
        }

        assertNotEquals(kilosopherCount, owners.size, "Deadlock detected, fork owners: $owners")

        for (kilosopher in kilosophers) {
            assertTrue(kilosopher.eatDuration > 0, "Eat durations: ${kilosophers.map { it.eatDuration }}")
        }

    }

    private val KILOSOPHER_COUNT = 10
    private val NAPKIN_COUNT = 3
    private val DURATION = 20000

    @Test
    @Fail
    fun testSampleKilosopherWithConcurrentFork() {
        doTest(SampleUniversity, ConcurrentForkBox, ConcurrentNapkinBox, kilosopherCount = KILOSOPHER_COUNT, napkinCount = NAPKIN_COUNT, duration = DURATION)
    }

    @Test
    @Fail
    fun testSampleKilosopherWithAtomicFork() {
        doTest(SampleUniversity, AtomicForkBox, AtomicNapkinBox, kilosopherCount = KILOSOPHER_COUNT, napkinCount = NAPKIN_COUNT, duration = DURATION)
    }

    @Test
    fun testWaiterKilosopherWithConcurrentFork() {
        doTest(WaiterUniversity(KILOSOPHER_COUNT), ConcurrentForkBox, ConcurrentNapkinBox, kilosopherCount = KILOSOPHER_COUNT, napkinCount = NAPKIN_COUNT, duration = DURATION)
    }

    @Test
    fun testWaiterKilosopherWithAtomicFork() {
        doTest(WaiterUniversity(KILOSOPHER_COUNT), AtomicForkBox, AtomicNapkinBox, kilosopherCount = KILOSOPHER_COUNT, napkinCount = NAPKIN_COUNT, duration = DURATION)
    }
}