package com.proferoman

import kotlin.system.measureTimeMillis

fun runnable1() {
    var num1 = 0
    var num2 = 0

    val task1 = Runnable {
        println("Thread: ${Thread.currentThread().name}")
        Thread.sleep(GO_SLEEP)

        val number = 0..1000
        num1 = number.random()
    }

    val task2 = Runnable {
        println("Thread: ${Thread.currentThread().name}")
        Thread.sleep(GO_SLEEP)

        val number = 0..1000
        num2 = number.random()
    }

    val timeMillis = measureTimeMillis {
        task1.run()
        task2.run()
    }

    println("Thread: ${Thread.currentThread().name}")
    println("Números generados por los runnable: $num1 y $num2")
    println("Ha tardado $timeMillis milisegundos")
}