/**
 * La función main2 que ves en este fichero recibe un texto y tiene que crear un proceso que
 * cuente las vocales (sin acentos) que hay en dicho texto y le comunique dicho resultado a
 * través de un fichero que contendrá el número de vocales, sin más. Este fichero estará
 * ubicado en la ruta ./files/part2 y el nombre será dado por la función main2, es decir, por
 * el proceso padre.
 *
 * Así, el proceso padre tiene que pasar al hijo: el texto y el nombre del fichero donde
 * guardará el resultado.
 *
 * El padre tendrá que esperar que termine el proceso hijo para leer del fichero el resultado
 * y mostrar por pantalla un mensaje como este:
 *
 * "Hay un total de 13 vocales en el texto"
 */
package com.proferoman.part2

import java.io.File

class VowelsSearcher {
    companion object {
        const val PATH = "./files/part2/"

        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size < 2) {
                return
            }

            try {
                val text = args[0]
                val fileName = args[1]

                val vowels = setOf('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U')
                val count = text.count { it in vowels }

                File("$PATH$fileName.txt").writeText(count.toString())
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
    }
}

fun main2(text: String) {
    val className = "com.proferoman.part2.VowelsSearcher"
    val classPath = System.getProperty("java.class.path")

    val javaHome = System.getProperty("java.home")
    val javaBin = "$javaHome/bin/java"

    val fileName = "file"

    val processBuilder = ProcessBuilder(
        javaBin, "-cp", classPath, className,
        text, fileName
    )

    val childProcess = processBuilder
        .inheritIO()
        .start()

    childProcess.waitFor()

    try {
        val count = File("${VowelsSearcher.PATH}$fileName.txt").readText().toInt()
        println("Hay un total de $count vocales")
    } catch (e: NumberFormatException) {
        e.printStackTrace()
    }
}
