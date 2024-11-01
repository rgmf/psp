package com.proferoman

import kotlin.system.measureTimeMillis

class RandomIntRunnable(private val n1: Int, private val n2: Int) : Runnable {
    private var _randomInt: Int = 0
    val randomInt: Int
        get() = _randomInt

    override fun run() {
        println("Thread: ${Thread.currentThread().name}")
        Thread.sleep(GO_SLEEP)

        _randomInt = (n1..n2).random()
    }
}

fun runnable3() {
    val runnable1 = RandomIntRunnable(2000, 3000)
    val runnable2 = RandomIntRunnable(1, 100)

    val timeMillis = measureTimeMillis {
        val t1 = Thread(runnable1)
        val t2 = Thread(runnable2)

        t1.start()
        t2.start()

        t1.join()
        t2.join()
    }

    println("Thread: ${Thread.currentThread().name}")
    println("Números generados por los runnable: ${runnable1.randomInt} y ${runnable2.randomInt}")
    println("Ha tardado $timeMillis milisegundos")
}