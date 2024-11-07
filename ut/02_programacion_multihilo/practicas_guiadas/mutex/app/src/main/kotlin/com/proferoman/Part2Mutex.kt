package com.proferoman

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

fun namingWithMutex() {
    var names = ""
    val mutex = ReentrantLock()

    val threads = List(20) { i ->
        thread {
            mutex.lock()
            try {
                names += "Hilo número $i con nombre '${Thread.currentThread().name}'\n"
            } finally {
                mutex.unlock()
            }
        }
    }

    threads.forEach { it.join() }

    println("Terminado, este es el valor de names:\n$names")
}

fun countingWithMutex() {
    var count = 0
    val mutex = ReentrantLock()

    // Forzamos la situación para ver las inconsistencias y, por tanto, la
    // necesidad de sincronizar y proteger la variable count.
    val threads = List(200) { i ->
        thread {
            mutex.lock()
            try {
                repeat(100) {
                    count++
                    Thread.sleep(1)
                }
            } finally {
                mutex.unlock()
            }
        }
    }

    threads.forEach { it.join() }

    // Esperamos que count sea igual a 200 * 100 = 20 000
    println("Terminado, este es el valor del contador: $count")
}