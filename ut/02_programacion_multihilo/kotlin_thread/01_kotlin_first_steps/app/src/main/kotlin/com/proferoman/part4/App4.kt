package com.proferoman.part4

import kotlinx.coroutines.*

/**
 * Dispatchers:
 *
 * Por defecto las corrutinas son creados por un Dispatcher por defecto de Kotlin: lo veíamos en
 * el ejemplo anterior cuando mostrábamos el nombre del hilo actual. Es decir, por defecto, Kotlin, a
 * través de lo que se denomina Dispatcher crea los hilos que ejecutarán las corrutinas.
 *
 * No obstante, podemos crear nuestros propios Dispatchers para ejecutar nuestra corrutinas. Esto no
 * significa que tengamos el control de los hilos.
 */


fun program() = runBlocking {
    val dispatcher = newSingleThreadContext(name = "PspDispatcher")
    val task = launch(dispatcher) {
        println("Running in thread ${Thread.currentThread().name}")
    }
    task.join()
}