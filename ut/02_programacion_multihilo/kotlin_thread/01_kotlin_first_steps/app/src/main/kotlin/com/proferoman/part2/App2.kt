package com.proferoman.part2

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

suspend fun getName(): String {
    delay(1000)
    return "Susan"
}

suspend fun getLastName(): String {
    delay(1000)
    return "Calvin"
}

/**
 * En este caso usamos una ejecución asíncrona porque podemos obtener
 * el nombre y apellidos de forma asíncrona. Luego esperamos a que finalicen
 * ambas funciones con await antes de seguir.
 *
 * Lo que tenemos en name y lastName es una variable de tipo Deferred (es un
 * valor futuro).
 */
fun program() = runBlocking {
    val time = measureTimeMillis {
        val name = async { getName() }
        val lastName = async { getLastName() }
        println("Hello, ${name.await()} ${lastName.await()}")
    }
    println("Execution took $time ms")
}