package com.proferoman

import kotlin.system.measureTimeMillis

fun sequential() {
    var num1 = 0
    var num2 = 0

    val l1 = {
        println("Hilo: ${Thread.currentThread().name}")
        Thread.sleep(5000)

        val numbers = 0..1000
        num1 = numbers.random()
    }

    val l2 = {
        println("Hilo: ${Thread.currentThread().name}")
        Thread.sleep(3000)

        val numbers = 0..1000
        num2 = numbers.random()
    }

    val timeMillis = measureTimeMillis {
        l1()
        l2()

        println("Hilo: ${Thread.currentThread().name}")
        println("Número generado por el hijo: $num1")
        println("Número generado por el hijo: $num2")
    }

    println("Se ha necesitado ${timeMillis / 1000f} segundos en hacer la operación")
}