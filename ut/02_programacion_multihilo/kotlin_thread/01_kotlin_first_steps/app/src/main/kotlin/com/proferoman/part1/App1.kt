package com.proferoman.part1

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * Las corutinas son ejecutadas en hilos. El framework de Kotlin crea los hilos necsarios
 * para ejecutar estas corutinas que pueden ir saltando de un hilo a otro.
 *
 * Veremos más adelante cómo crear hilos de forma explícita.
 */

/**
 * Las funciones que empiezan por "suspend" son coroutines.
 *
 * Las funciones suspend son corutinas que pueden suspender su ejecución pero sin bloquear el hilo
 * que las ejecuta. Este hilo, podrá entonces, ir a ejecutar otras corutinas que estén activas. Esto
 * lo maneja el framework de Kotlin por defecto.
 *
 * Las funciones suspend solo pueden ser llamadas desde otras funciones suspend o desde corutinas, en
 * general.
 */
suspend fun getName(): String {
    delay(1000)
    return "Susan"
}

suspend fun getLastName(): String {
    delay(1000)
    return "Calvin"
}

/**
 * Se ejecuta en secuencia, una función detrás de otra: tarda, pues, sobre 2 segundos.
 */
fun program() = runBlocking {
    val time = measureTimeMillis {
        val name = getName()
        val lastName = getLastName()
        println("Hello, $name $lastName")
    }
    println("Execution took $time ms")
}