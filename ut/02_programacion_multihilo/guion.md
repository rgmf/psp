# Teoría de hilos
Apartados:
1. Hilos: [Aquí](https://github.com/rgmf/psp/blob/main/ut/02_programacion_multihilo/apuntes/01_hilos.md)
2. Hilos vs Procesos: [Aquí](https://github.com/rgmf/psp/blob/main/ut/02_programacion_multihilo/apuntes/02_hilos_vs_procesos.md)
3. Medir tiempos de ejecución en Kotlin: [Aquí](https://github.com/rgmf/psp/blob/main/ut/02_programacion_multihilo/apuntes/03_medir_tiempos.md)

# Práctica guiada: programación de hilos con las clases Thread y Runnable
Escribimos, a través de una práctica guiada y dirigida por mí, un programa usando diferentes versiones: hilo, secuencia, con procesos, con runnables, con runnables/hilos, generalizando runnables con funciones y generalizando runnables con clases.

Medimos tiempos y nos vamos acercando, poco a poco, al estilo que deberíamos usar.

Esta práctica guiada está hecha en la carpeta `ejemplos/threads`.

# Ninja trainings: Runnable y Thread
Hacemos los ejercicios y *Ninja Trainings* que se tienen en los apartados 6 y 7.

- [Primer Ninja Training](https://github.com/rgmf/psp/blob/main/ut/02_programacion_multihilo/apuntes/06_ejercicio.md).
- [Segundo Ninja Training](https://github.com/rgmf/psp/blob/main/ut/02_programacion_multihilo/apuntes/07_ejercicio.md).

# Práctica guiada: race conditions / mutex / deadlocks
Entramos en los conceptos de "race conditions" y "deadlocks". Hacemos práctica guiada que tengo en `ejemplos/mutex` en el que podemos ver dos ejemplos donde se producen problemas al acceder a la misma memoria compartida y cómo solucionarlo con mutex (ReentrantLock).

# Práctica guiada: programación de hilos en aplicaciones con UI
Escribimos, a través de una práctica guiada y dirigida por mí, un programa que obtiene información de la API de Rick and Morty información. Dicho programa lo escribismo con Kotlin Multiplatform (KMP).

Esta práctica está hecha en la carpeta `ejemplos/multiplatform`. Estos son los pasos que vamos a llevar:

## Paso 1: 01_RickAndMortyFreeze: programa donde se bloquea el hilo principal
Está en `ejemplos/multiplatform/01_RickAndMortyFreeze` y está hecha. La abrimos, la analizamos y vemos/experimentamos el problema (como se bloquea el hilo principal y no podemos interactuar con la UI cuando hacemos clic en el botón de *refresh*).

## Paso 2: 02_RickAndMortyPrintResult: solución con Runnable/Thread sin actualizar la UI
Aquí usamos hilos como hemos estudiado en clase para evitar que el hilo principal se bloquee. Está hecho en `ejemplos/multiplatform/02_RickAndMortyPrintResult`.

Hay un problema: solo *printeamos* por pantalla los resultados obtenidos en la API, pero no actualizamos la UI.

Se puede probar a hacer clic muchas veces en el botón que lanza el hilo para ver cómo se crear varios hilos si bloquear el hilo principal.

## Paso 3: presentación donde explico cómo funciona el patrón observer y cómo usar MutableStateFlow en Kotlin para modificar los Composable de forma reactiva
TENGO UNA PRESENTACIÓN DONDE EXPLICAR TODA ESTA PARTE DE FORMA GRÁFICA QUE VA A SER MÁS FÁCIL DE ENTENDER:

`presentacion/mutable_state_flow.odp`.

Tras la presentación podemos pasar a completar el ejemplo anterior, que nos quedó pendiente.

## Paso 4: 03_RickAndMortyUpdateUi: actualizamos la UI con los datos que llegan de la API
Está hecho en `ejemplos/multiplatform/03_RickAndMortyUpdateUi`.

Antes de entrar de lleno en esta parte, es necesario saber, y tengo que explicar, qué es un **StateFlow** de Kotlin. Básicamente se trata del **Patrón Observer** en el que los componentes se pueden subscribir a cambios de estado (de un objeto) y recibir notificaciones automáticamente cuando el valor cambia.

En Kotlin, por tanto, basta con crear un objeto del tipo `MutableStateFlow<T>` y, donde se quiera observar los cambios, usar el método `collectAsState()`.

```kotlin
val data = MutableStateFlow("") // Crea un MutableStateFlow para un String cuyo valor inicial es ""

// En algún otro lado:
val state = data.asStateFlow().collectAsState() // Si esto se usa en un Composable (sería lo normal en UI apps)
                                                // entonces, dicho composable se reconstruirá si cambia el valor
                                                // de data.
```

El uso habitual es emplearlo en una clase (típicamente ViewModel) tal que así:
```kotlin
class SomeClass {
    private val _data = MutableStateFlow("")
    val data = _data.asStateFlow()

    // Continua...
}
```

Y, luego, en un composable:
```kotlin
@Composable
fun SomeComposable() {
    val someObject = SomeClass()
    val state = someObject.data.collectAsState()

    // Continua...
}
```

# Ninja trainings: Runnable, Thread y StateFlow en Kotlin Multiplatform (y Jetpack Compose)
