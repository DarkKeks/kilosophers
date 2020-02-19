package ru.spbstu.kilosophers

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.test.fail

annotation class Fail

class ExpectedFailure : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return statement(base, description)
    }

    private fun statement(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                val expectedToFail = description.getAnnotation(Fail::class.java) != null
                try {
                    base.evaluate()
                } catch (e: Throwable) {
                    if (expectedToFail) {
                        return
                    } else {
                        throw e
                    }
                }
                if (expectedToFail) {
                    fail("Test was expected to fail, but passed")
                }
            }
        }
    }
}