package com.proferoman

import kotlin.system.measureTimeMillis

class RandomInteger {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Thread.sleep(GO_SLEEP)
            print((1..1000).random())
        }
    }
}

fun launchProcess(): Process {
    val className = "com.proferoman.RandomInteger"
    val javaPath = System.getProperty("java.class.path")

    val pb = ProcessBuilder("kotlin", "-cp", javaPath, className)

    return pb.start()
}

fun process() {
    var num1 = 0
    var num2 = 0

    val timeMillis = measureTimeMillis {
        val p1 = launchProcess()
        val p2 = launchProcess()

        num1 = p1.inputStream.bufferedReader().readText().toInt()
        num2 = p2.inputStream.bufferedReader().readText().toInt()
    }

    println("Process: ${ProcessHandle.current().pid()}")
    println("Números generados por los hijos: $num1 y $num2")
    println("Ha tardado $timeMillis milisegundos")
}