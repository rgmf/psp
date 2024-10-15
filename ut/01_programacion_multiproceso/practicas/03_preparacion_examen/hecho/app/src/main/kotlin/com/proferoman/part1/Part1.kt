/**
 * En este fichero tienes un programa que comienza en main1 y que calcula el número primo más
 * grande hasta un número dado (que en este ejemplo es 10000).
 *
 * Modifica este programa para que el cálculo del mayor número primo se haga en un proceso
 * separado.
 *
 * Para comunicar el proceso hijo el resultado al padre se usará la salida estándar. Es decir,
 * el proceso hijo imprimirá el resultado por pantalla para que desde el padre se pueda recoger.
 *
 * Recuerda que para recoger lo que un proceso hijo escribe por pantalla se usa un InputStream:
 *
 * val result = childProcess.inputStream.bufferedReader().readText()
 *
 * Donde:
 * - childProcess es el proceso iniciado con ProcessBuilder.
 *
 * Cuando ejecutes la función main1 deberá mostrar por pantalla el resultado.
 */
package com.proferoman.part1

fun isPrime(n: Int): Boolean {
    for (i in 2..Math.sqrt(n.toDouble()).toInt()) {
        if (n % i == 0) {
            return false
        }
    }
    return true
}

fun biggerPrime(n: Int): Int {
    var lastPrime = 1

    for (i in 1..n) {
        if (isPrime(i)) {
            lastPrime = i
        }
    }

    return lastPrime
}

fun main1() {
    val result = biggerPrime(10000)
    print("El mayor número primo hasta el 10000 es el número $result")
}
