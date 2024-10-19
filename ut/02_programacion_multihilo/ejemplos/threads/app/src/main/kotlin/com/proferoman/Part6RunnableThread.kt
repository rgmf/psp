package com.proferoman

import kotlin.system.measureTimeMillis

fun runnable2() {
    var num = 0

    val task = Runnable {
        println("Hilo: ${Thread.currentThread().name}")
        Thread.sleep(3000)

        val numbers = 0..1000
        num = numbers.random()
    }

    val t1 = Thread(task)
    val t2 = Thread(task)

    val timeMillis = measureTimeMillis {
        t1.start()
        t2.start()

        t1.join()
        t2.join()
    }

    println("Se ha necesitado ${timeMillis / 1000f} segundos en hacer la operación")
}