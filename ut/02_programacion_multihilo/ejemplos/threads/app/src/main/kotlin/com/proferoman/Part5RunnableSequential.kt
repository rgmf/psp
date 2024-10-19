package com.proferoman

import kotlin.system.measureTimeMillis

fun runnable1() {
    var num = 0

    val task = Runnable {
        println("Hilo: ${Thread.currentThread().name}")
        Thread.sleep(3000)

        val numbers = 0..1000
        num = numbers.random()
    }

    val timeMillis = measureTimeMillis {
        task.run()
        println("Primer número generado en el Runnable: $num")
        task.run()
        println("Segundo número generado en el Runnable: $num")
    }

    println("Se ha necesitado ${timeMillis / 1000f} segundos en hacer la operación")
}