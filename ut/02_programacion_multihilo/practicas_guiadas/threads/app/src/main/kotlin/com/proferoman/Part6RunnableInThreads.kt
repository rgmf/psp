package com.proferoman

import kotlin.system.measureTimeMillis

fun randomIntTask(num: MutableList<Int>): Runnable {
    return Runnable {
        println("Thread: ${Thread.currentThread().name}")
        Thread.sleep(GO_SLEEP)

        val number = 0..1000
        num.add(number.random())
    }
}

fun runnable2() {
    val numberList1 = mutableListOf<Int>()
    val numberList2 = mutableListOf<Int>()

    val timeMillis = measureTimeMillis {
        val t1 = Thread(randomIntTask(numberList1))
        val t2 = Thread(randomIntTask(numberList2))

        t1.start()
        t2.start()

        t1.join()
        t2.join()
    }

    println("Thread: ${Thread.currentThread().name}")
    println("Números generados por los runnable: ${numberList1[0]} y ${numberList2[0]}")
    println("Ha tardado $timeMillis milisegundos")
}