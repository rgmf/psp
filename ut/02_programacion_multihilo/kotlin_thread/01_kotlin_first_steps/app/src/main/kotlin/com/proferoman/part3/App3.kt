package com.proferoman.part3

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 * Para crear hilos tenemos estas posibilidades:
 * - Se crean hilos llamando a: newSingleThreadContext()
 * - Se crean pools de hilos con: newFixedThreadPoolContext()
 * - Se crean un número de hilos igual al número de núcleos menos uno con: CommonPool
 *
 * Cuando creas un hilo se pueden ejecutar las corutinas en ellos aunque Kotlin puede modificar el hilo que
 * ejecutará las corutinas.
 *
 * Para la comunicación entre hilos tenemos:
 * - Channels: son pipes que pueden ser usadas con seguridad para enviar y recibir datos entre corutinas.
 * - Worker pools: un pool de corutinas que pueden ser usadaas para dividir el procesamiento de un conjunto de
 * operaciones in muchos hilos.
 * - Actors: es un envoltorio para mantener el estado que usa corutinas como mecanismo para ofrecer la modificación
 * segura de ese estado desde muchos hilos.
 * - Mutual exclusions (Mutexes): un mecanismo de sincronización que permite definir "zonas críticas" donde solo
 * puede entrar un hilo cada vez. Cualquier corutina que intente acceder a esta "zona crítica" quedará suspendida
 * si ya hay otra ahí.
 * - Thread confinement: la posibilidad de limitar la ejecución de corutinas de tal manera que siempre ocurran en
 * un hilo especificado.
 * - Generators (iterators y secuencias): fuentes de datos que producen información a demanda y es suspendida cuando
 * no hay nueva información requerida.
 */

/**
 * Coroutines Builders
 *
 * Son funciones que toman una suspending lambda y crea una corutina para ejecutarla. En Kotlin tenemos estos
 * builders:
 *
 * - async: se usa para inciar una corutina cuando se va a esperar un resultado. Hay que usarla con cuidado porque
 * las excepciones lanzadas dentro son colocadas como resultado. Devuelve un objeto de tipo Deferred<T> que contiene
 * el resultado o la excepción.
 *
 * - launch: se usa para iniciar una corutina que no devuelve resultado, es decir, no esperas ni necesitas resultado.
 * Lo que sí devuelve es un objeto de tipo Job que puedes usar para cancelar su ejecución y la de sus hijos.
 *
 * - runBlocking: usada para convertir el código bloqueante en código suspendible. Se usa comúnmente en las funciones
 * main y pruebas unitarias. Bloque el hilo actual hasta que se complete la ejecución de la corutina.
 */

/**
 * join vs await
 *
 * Cuando lanzas un corrutina con async obtienes un Deferred<T>. Con ese objeto puedes esperar que termine
 * la corrutina con join o await. La diferencia está en que join no devuelve nada y await devuelve el objeto
 * de tipo T de ese Deferred<T>.
 *
 * En ambos casos si hay excepciones en la corrutina se propagarán. Además, se espera a que termine el hilo.
 *
 * Se suele usar await con async y join con launch.
 *
 * En cualquier caso, hay algunos métodos interesantes de Deferred<T>:
 * - isCancelled
 * - isActive
 * - isCompleted
 */


suspend fun doSomething(): Int {
    println("Running in thread ${Thread.currentThread().name}")
    delay(2000)
    return 10
}

fun program() = runBlocking {
    val task = async {
        doSomething()
    }
    val n: Int = task.await()
    println(task.isCancelled)
    println(task.isActive)
    println(task.isCompleted)
    println(n)
    println("Completed")
}