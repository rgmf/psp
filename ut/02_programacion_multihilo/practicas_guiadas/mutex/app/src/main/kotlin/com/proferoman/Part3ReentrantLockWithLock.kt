package com.proferoman

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock

fun countingWithLock() {
    var count = 0
    val mutex = ReentrantLock()

    val threads = List(200) { i ->
        thread {
            mutex.withLock {
                repeat(100) {
                    count++
                    Thread.sleep(1)
                }
            }
        }
    }

    threads.forEach { it.join() }

    // Esperamos que count sea igual a 200 * 100 = 20 000
    println("Terminado, este es el valor del contador: $count")
}