package com.proferoman

import kotlin.concurrent.thread

fun namingNotSync() {
    var names = ""

    val threads = List(20) { i ->
        thread {
            names += "Hilo número $i con nombre '${Thread.currentThread().name}'\n"
        }
    }

    threads.forEach { it.join() }

    println("Terminado, este es el valor de names:\n$names")
}

fun countingNotSync() {
    var count = 0

    // Forzamos la situación para ver las inconsistencias y, por tanto, la
    // necesidad de sincronizar y proteger la variable count.
    val threads = List(200) { i ->
        thread {
            repeat(100) {
                count++
                Thread.sleep(1)
            }
        }
    }

    threads.forEach { it.join() }

    // Esperamos que count sea igual a 200 * 100 = 20 000
    println("Terminado, este es el valor del contador: $count")
}