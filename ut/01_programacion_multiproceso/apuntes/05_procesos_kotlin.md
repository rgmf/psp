# Resultados de aprendizaje y criterios de evaluación

- **RA1**. Desarrolla aplicaciones compuestas por varios procesos reconociendo y aplicando principios de programación paralela.
  - **CE1e**. Se han utilizado clases para programar aplicaciones que crean subprocesos.
  - **CE1f**. Se han utilizado mecanismos para sincronizar y obtener el valor devuelto por los subprocesos iniciados.
  - **CE1g**. Se han desarrollado aplicaciones que gestionen y utilicen procesos para la ejecución de varias tareas en paralelo.
  - **CE1h**. Se han depurado y documentado las aplicaciones desarrolladas.

# Procesos en Kotlin

La JVM (*Java Virtual Machine*) se ha diseñado para soportar programación concurrente:

- La funcionalidad básica se proporciona a través de la clase `Process`, que es abstracta y no se pueden crear objetos de ella. Hay que usar subclases como veremos más adelante.

- La funcionalidad básica para los hilos se proporciona a través de la clse `Thread` (que abordaremos en el tema siguiente).

- El paquete `java.util.concurrent` proporciona funcionalidd de alto nivel para programación concurrente.

Centrándonos en los procesos (y la clase `Process`) tenemos dos métodos, de sendas clases, que nos permiten crear procesos, a saber: `ProcessBuilder.start()` y `Runtime.exec()`.

# La clase Runtime

Esta clase porporiona información del entorno de ejecución del proceso y nos permite interactura con él. Además, el método `exec()` permite lanzar nuevos procesos (que serán hijos del actual, del que hace la llamada).

Un ejemplo de uso lo tienes en el siguiente ejemplo, comentado para que se entienda cada instrucción:

```kotlin
// Obtenemos el objeto Runtime del entorno de ejecución actual.
val runtime = Runtime.getRuntime()

// Se crear un proceso hijo que ejecuta el comando "ls -l".
val childProcess = runtime.exec(arrayOf("ls", "-l"))

// Se imprimen por pantalla los PID de los procesos padre e hijo.
// El PID del proceso padre lo obtenemos con la clase ProcessHandle.
// El PID del proceso hijo directamente del objeto Process que crea exec.
println("PID parent process: ${ProcessHandle.current().pid()}")
println("PID child process: ${childProcess.pid()}")
```

Si deseas obtener el resultado del proceso hijo, lo tiens que hacer a través de la entrada/salida estándar. Lo que imprime por pantalla el proceso hijo puede ser capturado por el proceso padre. Para ello hay que utilizar *input stream* como se ve en este ejemplo, donde he completado el anterior para recuperar la salida del proceso hijo:

```kotlin
// Obtenemos el objeto Runtime del entorno de ejecución actual.
val runtime = Runtime.getRuntime()

// Se crear un proceso hijo que ejecuta el comando "ls -l".
val childProcess = runtime.exec(arrayOf("ls", "-l"))

// Se imprimen por pantalla los PID de los procesos padre e hijo.
// El PID del proceso padre lo obtenemos con la clase ProcessHandle.
// El PID del proceso hijo directamente del objeto Process que crea exec.
println("PID parent process: ${ProcessHandle.current().pid()}")
println("PID child process: ${childProcess.pid()}")

// Capturamos, desde aquí (el padre), la salida del proceso hijo.
// Esta instrucción es bloqueante: el proceso padre queda detenido aquí
// a la espera de que termine el proceso hijo.
val output = childProcess.inputStream.bufferedReader().readText()

// Imprimimos por pantalla, aquí (en el proceso padre), la salida.
println(output)
```

# La clase Process

Como ya he comentado, la clase `Process` es abstracta y no se puede instanciar. Pero tenemos clases derivadas como `ProcessBuilder` que podemos usar para crear procesos.

Veamos, directamente, cómo usar `ProcessBuilder` para escribir el mismo programa que el anterior con `Runtime`:

```kotlin
// Creamos objeto ProcessBuilder.
// Los comandos se indican en una lista de "vargs".
val processBuilder = ProcessBuilder("ls", "-l")

// Se crea y ejectua el proceso hijo.
val childProcess = processBuilder.start()

// Se imprimen por pantalla los PID de los procesos padre e hijo.
// El PID del proceso padre lo obtenemos con la clase ProcessHandle.
// El PID del proceso hijo directamente del objeto Process que crea exec.
println("PID parent process: ${ProcessHandle.current().pid()}")
println("PID child process: ${childProcess.pid()}")

// Capturamos, desde aquí (el padre), la salida del proceso hijo.
// Es bloqueante: el proceso padre se bloquea aquí hasta que termine el hijo.
val output = childProcess.inputStream.bufferedReader().readText()

// Imprimimos por pantalla, aquí (en el proceso padre), la salida.
println(output)
```

¿Qué diferencia hay entre `Runtime` y `ProcessBuilder`? En ejemplos sencillos, como estos, ninguna. Pero si queremos configurar, por ejemplo, el entorno de ejecución tendremos que usar `ProcessBuilder`.

## Configurar entorno de ejecución

Cuando ejecutamos un proceso, a veces, será necesario hacerlo desde un directorio de trabajo concreto; o necesitaremos cargar las variables de entorno; o un ejecutable conreto; etc.

Todas este entorno lo podemos obtener con la el método `System.getProperty`. Por ejemplo:

- `System.getProperty("java.class.path")` para obtener el `PATH` de la *Java Virtual Machine*.
- `System.getProperty("java.home")` para obtener el directorio donde está instalado la *Java Virtual Machine*.
- `System.getProperty("user.dir")` para obtener el directorio personal del usuario que ejecuta el proceso.

Puedes ver todas las opciones [en esta web](https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html).

Si quieres conocer todo el entorno en el que se ejecuta un proceso, ejecuta este programa:

```kotlin
val pb = ProcessBuilder("ls")
val childProcess = pb.start()

pb.environment().forEach { k, v ->
    println("$k: $v")
}
```

Para ver cómo configurar, en la práctica, un entorno concreto, veamos cómo ejecutar el comando `ls` en el directorio del usuario que lo ejecuta:

```kotlin
val userDir = System.getProperty("user.home")
val processBuilder = ProcessBuilder("ls", userDir)

val childProcess = processBuilder.start()
val output = childProcess.inputStream.bufferedReader().readText()

println(output)
```

## Cómo ejecutar una clase como un proceso

Hasta ahora hemos creado procesos que ejecutan comandos del sistema. Veamos, ahora, cómo crear un proceso para ejecutar un ejecutable de Kotlin/Java (un `class` o código compilado para la máquina virtual de java o *JVM*).

Para ejecutar un `class` vamos a necesitar que dicho `class` tenga el punto de entrada `main`. De ahí que, como ves en el siguiente código, tengamos la función `main` como entrada al programa principal (hilo principal del proceso padre) y el método `main` en la clase que queremos ejecutar en un nuevo proceso hijo:

> La *anotación* `@JvmStatic` convierte ese método en un método estático en el sentido tradicional de Java.

```kotlin
package com.proferoman

import java.time.Duration
import kotlin.random.Random

class Addition {
    fun add(n1: Int, n2: Int): Int {
        var result = 0
        for (i in n1..n2) {
            result += i
        }
        return result
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.isEmpty() || args.size < 2) {
                println("Two numbers are needed")
                return
            }

            val addition = Addition()
            val n1 = Integer.parseInt(args[0])
            val n2 = Integer.parseInt(args[1])
            val result = addition.add(n1, n2);
            print(result);
        }
    }
}

fun launchAdder(n1: Int, n2: Int): Int {
    val className = "com.proferoman.Addition"
    val classPath = System.getProperty("java.class.path")

    try {
        val processBuilder = ProcessBuilder(
            "kotlin", "-cp", classPath, className,
            n1.toString(), n2.toString()
        )
        val addProcess = processBuilder
            .redirectErrorStream(true)
            .start()

        // Equivale a un .waitFor() y bloquea el hilo principal
        val addOutput = addProcess.inputStream.bufferedReader().readText()

        return addOutput.toInt()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0
}

fun main() {
    val result1 = launchAdder(1, 51)
    val result2 = launchAdder(51, 100)

    println("Result 1: $result1")
    println("Result 2: $result2")
    println("Addition: ${result1 + result2}")
}
```

## Sincronización padre/hijo
