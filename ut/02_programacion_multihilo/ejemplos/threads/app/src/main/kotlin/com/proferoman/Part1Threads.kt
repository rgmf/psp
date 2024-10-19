package com.proferoman

fun threads1() {
    var num = 0

    val t1 = Thread {
        println("Hilo: ${Thread.currentThread().name}")
        Thread.sleep(3000)

        val numbers = 0..1000
        num = numbers.random()
    }

    t1.start()
    t1.join()

    println("Hilo: ${Thread.currentThread().name}")
    println("Número generado por el hijo: $num")
}