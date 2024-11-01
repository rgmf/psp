package com.proferoman

fun threads1() {
    var num = 0

    val t1 = Thread {
        println("Thread: ${Thread.currentThread().name}")
        Thread.sleep(GO_SLEEP)

        val number = 0..1000
        num = number.random()
    }

    t1.start()
    t1.join()

    println("Thread: ${Thread.currentThread().name}")
    println("Número generado por el hijo: $num")
}