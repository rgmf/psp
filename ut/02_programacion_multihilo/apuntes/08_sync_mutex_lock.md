# Resultados de aprendizaje y criterios de evaluación

- **RA2**. Desarrolla aplicaciones compuestas por varios procesos reconociendo y aplicando principios de programación paralela.
  - **CE2a**. Se han identificado situaciones en las que resulte útil la utilización de varios hilos en un programa.
  - **CE2b**. Se han reconocido los mecanismos para crear, iniciar y finalizar hilos.
  - **CE2c**. Se han programado aplicaciones que implementen varios hilos.
  - **CE2d**. Se han identificado los posibles estados de ejecución de un hilo y programado aplicaciones que los gestionen.
  - **CE2e**. Se han utilizado mecanismos para compartir información entre varios hilos de un mismo proceso.
  - **CE2f**. Se han desarrollado programas formados por varios hilos sincronizados mediante técnicas específicas.

# Sincronización de hilos

En las aplicaciones multihilo, dos o más hilos pueden necesitar compartir recursos al mismo tiempo, resultando en comportamientos inesperados. Ejemplos de recursos compartidos son los datos, variables, estructuras de datos, dispositivos de entrada/salida, ficheros y conexiones de red.

Esto obliga a los desarrolladores de aplicaciones multihilo a introducir algún tipo de sincronización.

# Race conditions y sección crítica

Cuando dos o más hilos intentan acceder al mismo recurso nos encontramos con lo que se denomina una **race condition**: dos o más hilos compitiendo por los mismos recursos.

El fragmento de código en el que se accede a un recurso compartido por varios hilos se le denomina **sección crítica**.

El ejemplo típico es el de dos hilos intentando escribir al mismo tiempo en una variable (posición de memoria). ¿Cuál será el resultado de dicho acceso? El resultado es lo peor que nos puede pasar como desarrolladores: **inesperado**.

Esto nos va a obligar a sincronizar los hilos para proporcionar los recursos compartidos de manera ordenada, de forma que el resultado de los accesos a estos recursos sean previsibles y correctos.

A continuación te muestro un ejemplo en el que 200 hilos acceden a la misma variable `count` para incrementar su valor (cada uno lo hace 200 veces). Aquí la sección crítica es el lugar donde se incrementa la variable `count` en 1. Puedes probar a ejecutar este programa y verás que el valor de `count` no es el esperado:

```kotlin
fun main() {
    var count = 0

    // Forzamos la situación para ver las inconsistencias y, por tanto, la
    // necesidad de sincronizar y proteger la variable count.
    val threads = List(200) { i ->
        thread {
            repeat(100) {
                count++
                Thread.sleep(1)
            }
        }
    }

    threads.forEach { it.join() }

    // Esperamos que count sea igual a 200 * 100 = 20 000
    println("Terminado, este es el valor del contador: $count")
}
```

La funcion `thread` crea el hilo y comienza su ejecución, es el equivalente a crear el hilo por un lado y comenzar la tarea llamando al método `start`:

```kotlin
val t = Thread {
    // Tarea a realizar por el hilo...
}

t.execute()
```

# Mutex o exclusión mutua

Para evitar una *race condition* necesitamos sincronizar el acceso a la sección crítica.

Un **mutex** (o **exclusión mutua**) es la forma más sencilla de sincronización, asegura que uno y solo un hilo pueda ejecutar el código de la sección crítica al mismo tiempo.

La idea es sencilla: cada vez que un hilo quiere acceder a la sección crítica pide el *mutex*. Si está cogido se bloquea hasta que esté disponible. Cuando está disponible coge el *mutex* y entra a la sección crítica. Al terminar tiene que liberar el *mutex* para dejar paso al siguiente hilo que lo necesite.

En Kotlin (y por extensión en Java) tenemos varias formas de crear esos *mutex*:

- `synchronized`: un método o bloque de código declarado como `synchronized`, es un bloque de código exlusivo para un hilo, es decir, solo un hilo puede estar dentro de ese bloque y, hasta que no salga, no puede entrar otro. Por tanto, el hilo que entra bloquea el bloque de código. Es una manera sencilla de sincronizar casos simples:

```kotlin
fun main() {
    var counter = 0

    // Objeto de bloqueo para sincronizar el acceso
    val lock = Any()

    val threads = List(1000) {
        thread {
            repeat(100) {
                // Solo un thread a la vez puede ejecutar este bloque
                synchronized(lock) {
                    counter++
                }
            }
        }
    }

    threads.forEach { it.join() }

    println("Valor final del contador: $counter")
}
```

- `ReentrantLock`: para casos más complejos esta es una buena solución porque ofrece opciones adicionales, como: bloqueos no bloqueantes para permitir a un hilo seguir si no está disponible el *mutex*; bloqueos interrumpibles para permitir que un hio sea interrumpido mientras espera adquirir el *mutex*; o condiciones de espera que permiten hacer esperar a los hilos hasta que ciertas condiciones se cumplan. Aquí, el mismo ejemplo de arriba pero con `ReentrantLock`. Verás que se usa un bloque `try-finally` para asegurar que se desbloque el *mutex* ya que con `ReentrantLock` tienes que indicar cuándo bloquear y desbloquear (explícitamente):

```kotlin
fun main() {
    var counter = 0

    // Crear un ReentrantLock
    val lock = ReentrantLock()

    val threads = List(1000) {
        thread {
            repeat(100) {
                // Adquirir el bloqueo antes de modificar el contador
                lock.lock()
                try {
                    counter++
                } finally {
                    // Liberar el bloqueo, incluso si ocurre una excepción
                    lock.unlock()
                }
            }
        }
    }

    threads.forEach { it.join() }

    println("Valor final del contador: $counter")
}
```

Puedes usar la extensión `withLock` de `ReentrantLock` para no olvidar desbloquear el *mutex*. De esta manera el código queda más claro y se evitan descuidos que pueden resultar fatales:

```kotlin
fun main() {
    var counter = 0

    // Crear un ReentrantLock
    val lock = ReentrantLock()

    val threads = List(1000) {
        thread {
            repeat(100) {
                // Usar withLock para adquirir y liberar el lock automáticamente
                lock.withLock {
                    counter++
                }
            }
        }
    }

    threads.forEach { it.join() }

    println("Valor final del contador: $counter")
}

```

- `Semaphore`: existen otras opciones como son los `Semaphore` que permiten acceder a la sección crítica a un número determinado de hilos. Por ejemplo, si te encuentras en un escenario en el que quieres limitar el número de hilos que están al mismo tiempo en la sección crítica a tres, entonces tendrás que usar un `Semaphore`:

```kotlin
fun main() {
    // Crear un semáforo que permita hasta 3 hilos en la sección crítica
    val semaphore = Semaphore(3)

    val threads = List(10) { i ->
        thread {
            try {
                // Adquirir el semáforo (espera si hay más de 3 hilos en la sección crítica)
                semaphore.acquire()
                // Simular el trabajo en la sección crítica
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                println("Hilo $i fue interrumpido.")
            } finally {
                // Liberar el semáforo, permitiendo que otro hilo entre
                semaphore.release()
            }
        }
    }

    threads.forEach { it.join() }

    println("Todos los hilos han terminado.")
}
```

# Deadlock o bloqueo mutuo

Un deadlock (bloqueo mutuo) es una situación en la que dos o más hilos se bloquean mutuamente al esperar por recursos que los otros hilos tienen bloqueados. Como resultado, ninguno de los hilos puede continuar su ejecución, lo que lleva a que el sistema quede atascado.

Un deadlock ocurre típicamente cuando:

- Hilo A tiene bloqueado el recurso 1 y espera por el recurso 2.
- Hilo B tiene bloqueado el recurso 2 y espera por el recurso 1.

Ninguno de los hilos puede avanzar porque cada uno espera por el recurso que tiene el otro.

En este ejemplo, dos hilos intentan bloquear dos recursos diferentes (representados por ReentrantLock), pero de forma que se crea un deadlock:

```kotlin
fun main() {
    val lock1 = ReentrantLock()
    val lock2 = ReentrantLock()

    // Hilo 1 intenta bloquear lock1 y luego lock2
    val thread1 = thread {
        println("Hilo 1 esperando por lock1...")
        lock1.lock()
        println("Hilo 1 ha bloqueado lock1.")

        // Simula trabajo con lock1
        Thread.sleep(100)

        println("Hilo 1 esperando por lock2...")
        lock2.lock()  // Deadlock aquí
        println("Hilo 1 ha bloqueado lock2.")

        lock2.unlock()
        lock1.unlock()
    }

    // Hilo 2 intenta bloquear lock2 y luego lock1
    val thread2 = thread {
        println("Hilo 2 esperando por lock2...")
        lock2.lock()
        println("Hilo 2 ha bloqueado lock2.")

        // Simula trabajo con lock2
        Thread.sleep(100)

        println("Hilo 2 esperando por lock1...")
        lock1.lock()  // Deadlock aquí
        println("Hilo 2 ha bloqueado lock1.")

        lock1.unlock()
        lock2.unlock()
    }

    // Esperar que los dos hilos terminen
    thread1.join()
    thread2.join()

    println("Ambos hilos han terminado.")
}
```