package com.proferoman

const val GO_SLEEP: Long = 2000

fun main() {
    println("Threads1")
    println("--------------------------------------------")
    threads1()
    println()
    println()

    println("Threads2")
    println("--------------------------------------------")
    threads2()
    println()
    println()

    println("Sequential")
    println("--------------------------------------------")
    sequential()
    println()
    println()

    println("Process")
    println("--------------------------------------------")
    process()
    println()
    println()

    println("Runnable1 (sequential)")
    println("--------------------------------------------")
    runnable1()
    println()
    println()

    println("Runnable2 (threads)")
    println("--------------------------------------------")
    runnable2()
    println()
    println()

    println("Runnable3 (class)")
    println("--------------------------------------------")
    runnable3()
    println()
    println()
}
