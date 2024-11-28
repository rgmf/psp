package org.example

import kotlinx.coroutines.*
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

data class Factors(val number: Long, val factors: List<Long>) {
    override fun toString(): String {
        return "$number = ${factors.joinToString(" x ")}"
    }
}

fun computeFactors(number: Long): List<Long> {
    var n = number
    val factors = mutableListOf<Long>()
    val limit = sqrt(n.toDouble()).toLong()

    while (n % 2 == 0L) {
        factors.add(2)
        n /= 2
    }

    for (i in 3..limit step 2) {
        while (n % i == 0L) {
            factors.add(i)
            n /= i
        }
    }

    if (n > 1) {
        factors.add(n)
    }

    return factors
}

suspend fun withCoroutines(numbers: List<Long>): List<Factors> = coroutineScope {
    val factors = mutableListOf<Factors>()
    val jobs = mutableListOf<Deferred<Factors>>()

    numbers.forEach { number ->
        jobs.add(
            async(Dispatchers.Default) {
                Factors(number, computeFactors(number))
            }
        )
    }

    jobs.awaitAll().forEach { factors.add(it) }

    factors
}

fun sequential(numbers: List<Long>): List<Factors> {
    val factors = mutableListOf<Factors>()
    numbers.forEach { factors.add(Factors(it, computeFactors(it))) }
    return factors
}

fun main() = runBlocking {
    var numbers = listOf(
        987_654_321_987_654_321L,
        1_234_567_890_123_456_789L,
        2_345_678_901_234_567_890L,
        3_456_789_012_345_678_901L
    )
    var factors: List<Factors>

    val tms = measureTimeMillis {
        factors = sequential(numbers)
    }
    println("Opción secuencial:")
    println("----------------------------------------------------------------")
    println("Terminó en $tms ms")
    factors.forEach { println(it) }
    println()

    val tmc = measureTimeMillis {
        factors = withCoroutines(numbers)
    }
    println("Opción corrutinas:")
    println("----------------------------------------------------------------")
    println("Terminó en $tmc ms")
    factors.forEach { println(it) }
}
