package com.proferoman

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

data class Point(val dimensions: List<Double>)

fun randomPoint(nd: Int, range: IntRange = -1000..1000): Point {
    val dimensions = mutableListOf<Double>()
    for (j in 0..<nd) {
        dimensions.add(range.random().toDouble())
    }
    return Point(dimensions)
}

fun distanceBetween(p1: Point, p2: Point): Double {
    var times2Additions = 0.0

    for (i in 0..<p1.dimensions.size) {
        times2Additions += Math.pow(p1.dimensions[i] - p2.dimensions[i], 2.0)
    }

    return Math.sqrt(times2Additions)
}

suspend fun computeDistances(refPoint: Point, totalPoints: Int, chunkedSize: Int): List<Double> = coroutineScope {
    val jobs = mutableListOf<Deferred<List<Double>>>()

    (0 until totalPoints).chunked(chunkedSize).forEach {
        jobs.add(
            async(Dispatchers.Default) {
                val distances = mutableListOf<Double>()
                for (i in it) {
                    distances.add(distanceBetween(refPoint, randomPoint(refPoint.dimensions.size)))
                }
                distances
            }
        )
    }

    jobs.awaitAll().flatten()
}

fun main() = runBlocking {
    val nDimensions = 3
    val referencePoint = randomPoint(nDimensions, 0..0)
    val totalPoints = 50_000_000
    val chunkedSize = 1_000_000
    var distances: List<Double>

    val timeMillis = measureTimeMillis {
        distances = computeDistances(referencePoint, totalPoints, chunkedSize)
    }

    println("Terminado de calcular las ${distances.size} distancias de los puntos en un tiempo de ${timeMillis} ms")
    println("La distancia media es de ${distances.sum() / distances.size}")
}
