package com.proferoman

import kotlin.system.measureTimeMillis

fun sequential() {
    var num1 = 0
    var num2 = 0

    val lambda1 = {
        println("Thread: ${Thread.currentThread().name}")
        Thread.sleep(GO_SLEEP)

        val number = 0..1000
        num1 = number.random()
    }

    val lambda2 = {
        println("Thread: ${Thread.currentThread().name}")
        Thread.sleep(GO_SLEEP)

        val number = 0..1000
        num2 = number.random()
    }

    val timeMillis = measureTimeMillis {
        lambda1()
        lambda2()
    }

    println("Thread: ${Thread.currentThread().name}")
    println("Números generados por los hijos: $num1 y $num2")
    println("Ha tardado $timeMillis milisegundos")
}