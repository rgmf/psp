package com.proferoman

import java.io.File

class TemperatureReader(private val filePath: String) : Runnable {

    private val _temperatures: MutableList<Float> = mutableListOf()
    val temperatures
        get() = _temperatures

    override fun run() {
        println("Hilo: ${Thread.currentThread().name}")
        Thread.sleep((2000..5000).random().toLong())
        File(filePath).forEachLine {
            if (it.isNotEmpty()) {
                _temperatures.add(it.toFloat())
            }
        }
    }
}

fun main(args: Array<String>) {
    if (args.size != 1 || !File(args[0]).isDirectory) {
        println("Tienes que indicar a través de la línea de entrada de comandos la ruta del directorio donde están los ficheros con las temperaturas.")
        return
    }

    val threads: MutableList<Thread> = mutableListOf()
    val tasks: MutableList<TemperatureReader> = mutableListOf()

    File(args[0]).listFiles()?.toList()?.forEach {
        val task = TemperatureReader(it.toString())
        val t = Thread(task)
        t.start()

        threads.add(t)
        tasks.add(task)
    }

    threads.forEach { it.join() }

    val maxTemperatures = tasks.maxOfOrNull { it.temperatures.size } ?: 0
    val addition: MutableList<Float> = MutableList<Float>(maxTemperatures) { 0f }
    tasks.forEachIndexed { index, temperatureReader ->
        temperatureReader.temperatures.forEachIndexed { i, t ->
            if (addition.size > i) {
                addition[i] += t
            }
        }
    }

    val averages = addition.map { it / 3f }

    println("Temperaturas medias de los días de la semana:")
    averages.forEach {
        println(it)
    }

    println()
    println("Temperatura media de la semana: ${averages.fold(0f) { acc, i -> i + acc } / averages.size.toFloat()}")
}
