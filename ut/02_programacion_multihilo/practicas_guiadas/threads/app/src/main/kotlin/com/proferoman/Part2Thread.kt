package com.proferoman

import kotlin.system.measureTimeMillis

fun threads2() {
    var num1 = 0
    var num2 = 0

    val t1 = Thread {
        println("Thread: ${Thread.currentThread().name}")
        Thread.sleep(GO_SLEEP)

        val number = 0..1000
        num1 = number.random()
    }

    val t2 = Thread {
        println("Thread: ${Thread.currentThread().name}")
        Thread.sleep(GO_SLEEP)

        val number = 0..1000
        num2 = number.random()
    }

    val timeMillis = measureTimeMillis {
        t1.start()
        t2.start()

        t1.join()
        t2.join()
    }

    println("Thread: ${Thread.currentThread().name}")
    println("Números generados por los hijos: $num1 y $num2")
    println("Ha tardado $timeMillis milisegundos")
}