/**
 * Modifica la parte 2 para que la función principal, en este caso main3, divida la tarea de
 * contar el número de vocales en diferentes procesos. Cada proceso se encargará de contar las
 * vocales que hay en un rango.
 *
 * El proceso padre dividirá el texto en trozos del tamaño indicado por el argumento chunkSize.
 *
 * El proceso padre tendrá que indicar al hijo: el texto sobre el que contar y el nombre del
 * fichero donde dejará su resultado. Por ejemplo, si el texto fuera:
 *
 * "Este es un texto tal cual, sin nada más que una serie de palabras."
 *
 * Y el chunkSize es igual a 10, entonces repartirá la tarea tal que así:
 *
 * - Primer proceso: "Este es un"
 * - Segundo proceso: " texto tal"
 * - Tercer proceso: " cual, sin"
 * - Cuarto proceso: " nada más "
 * - Quinto proceso: "que una se"
 * - Sexto proceso: "rie de pal"
 * - Séptimo proceso: "abras."
 *
 * Cada proceso escribirá el resultado en un fichero de texto, dentro de la carpeta ./files/part3
 * que ya está creada. Así, siguiendo con el ejemplo anterior:
 *
 * - Primer proceso: escribirá el resultado en ./files/part3/file1.txt
 * - Segundo proceso: escribirá el resultado en ./files/part3/file2.txt
 * - Tercer proceso: escribirá el resultado en ./files/part3/file3.txt
 * - Cuarto proceso: escribirá el resultado en ./files/part3/file4.txt
 * - Quinto proceso: escribirá el resultado en ./files/part3/file5.txt
 * - Sexto proceso: escribirá el resultado en ./files/part3/file6.txt
 * - Séptimo proceso: escribirá el resultado en ./files/part3/file7.txt
 *
 * Consideraciones a tener en cuenta:
 *
 * - Tiene que ser el proceso padre el que indique al hijo el nombre del fichero donde tiene que
 *   guardar el resulto.
 * - El proceso padre tiene que esperar a que todos los hijos terminen para poder leer de todos
 *   los ficheros y sumar los resultados para tener la cuenta total de vocales.
 * - El programa finalmente tendrá que mostrar cuántas vocales hay en el texto.
 * - Te recomiendo que imprimas, además, la información de cada proceso que te resultará útil
 *   para cuestiones de depuración y validación.
 *
 * Imagina que llamamos al programa con los valores anteriores. Un ejemplo de salida
 * podría ser:
 *
 * Proceso con PID 169319: buscando vocales en "Este es un"
 * Proceso con PID 169330: buscando vocales en " texto tal"
 * Proceso con PID 169320: buscando vocales en " cual, sin"
 * Proceso con PID 169322: buscando vocales en " nada más"
 * Proceso con PID 169324: buscando vocales en "que na se"
 * Proceso con PID 168323: buscando vocales en "rie de pal"
 * Proceso con PID 168326: buscando vocales en "abras."
 * Hay un total de 23 vocales en el texto dado.
 */
package com.proferoman.part3

import java.io.File

class VowelsSearcher {
    companion object {
        const val PATH = "./files/part3/"

        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size < 2) {
                return
            }

            try {
                val text = args[0]
                val fileName = args[1]

                println("Proceso con PID ${ProcessHandle.current().pid()}: buscando vocales en \"$text\"")

                val vowels = setOf('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U')
                val count = text.count { it in vowels }

                File("$PATH$fileName.txt").writeText(count.toString())
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
    }
}

fun launchProcess(text: String, fileName: String): Process {
    val className = "com.proferoman.part3.VowelsSearcher"
    val classPath = System.getProperty("java.class.path")

    val javaHome = System.getProperty("java.home")
    val javaBin = "$javaHome/bin/java"

    val processBuilder = ProcessBuilder(
        javaBin, "-cp", classPath, className,
        text, fileName
    )

    return processBuilder.inheritIO().start()
}

fun main3(text: String, chunkSize: Int) {
    val processes = mutableListOf<Process>()

    // Launch all needed processes
    var count = 1
    var from = 0
    var to = if (from + chunkSize < text.length) from + chunkSize - 1 else text.length - 1
    do {
        processes.add(launchProcess(text.substring(from, to + 1), "file$count"))

        from = to + 1
        to = if (from + chunkSize < text.length) from + chunkSize - 1 else text.length - 1

        count++
    } while (from < text.length - 1)

    // Wait for all processes to be finished
    for (p in processes) {
        p.waitFor()
    }

    // Compute total primes
    try {
        var vowels = 0
        for (i in 1..count - 1) {
            vowels += File("${VowelsSearcher.PATH}file$i.txt").readText().toInt()
        }
        println("Hay un total de $vowels vocales en el texto.")
    } catch (e: NumberFormatException) {
        e.printStackTrace()
    }
}
