package com.proferoman

import kotlin.system.measureTimeMillis

/**
 * ¡¡¡Cuidado con ejecutar este fichero en vez de App.kt!!!
 *
 * Si ejecutas este fichero se lanzará la clase main de esta clase.
 */
class RandomNumber {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Thread.sleep(3000)

            val numbers = 0..1000
            val randomNum = numbers.random()

            print(randomNum)
        }
    }
}

fun launchProcess(): Process {
    val className = "com.proferoman.RandomNumber"
    val classPath = System.getProperty("java.class.path")

    val pb = ProcessBuilder(
        "kotlin", "-cp", classPath, className
    )
    return pb.start()
}

fun process() {
    val timeMillis = measureTimeMillis {
        val p1 = launchProcess()
        val p2 = launchProcess()

        val num1 = p1.inputStream.bufferedReader().readText()
        val num2 = p2.inputStream.bufferedReader().readText()

        println("Número generado por el proceso: $num1")
        println("Número generado por el proceso: $num2")
    }

    println("Se ha necesitado ${timeMillis / 1000f} segundos en hacer la operación")
}