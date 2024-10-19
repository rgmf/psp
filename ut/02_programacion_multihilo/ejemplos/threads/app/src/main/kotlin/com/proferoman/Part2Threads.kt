package com.proferoman

import kotlin.system.measureTimeMillis

fun threads2() {
    var num1 = 0
    var num2 = 0

    val t1 = Thread {
        println("Hilo: ${Thread.currentThread().name}")
        Thread.sleep(5000)

        val numbers = 0..1000
        num1 = numbers.random()
    }

    val t2 = Thread {
        println("Hilo: ${Thread.currentThread().name}")
        Thread.sleep(5000)

        val numbers = 0..1000
        num2 = numbers.random()
    }

    val timeMillis = measureTimeMillis {
        t1.start()
        t2.start()

        t1.join()
        t2.join()

        println("Hilo: ${Thread.currentThread().name}")
        println("Número generado por el hijo: $num1")
        println("Número generado por el hijo: $num2")
    }

    println("Se ha necesitado ${timeMillis / 1000f} segundos en hacer la operación")
}