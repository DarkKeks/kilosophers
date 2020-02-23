package ru.spbstu.kilosophers

enum class ActionKind {
    THINK,
    TAKE_LEFT,
    TAKE_RIGHT,
    DROP_LEFT,
    DROP_RIGHT,
    TAKE_NAPKIN,
    DROP_NAPKIN,
    EAT;

    operator fun invoke(duration: Int) = Action(this, duration)
}

data class Action(val kind: ActionKind, val duration: Int)