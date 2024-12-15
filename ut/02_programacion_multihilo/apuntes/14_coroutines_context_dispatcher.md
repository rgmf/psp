# Resultados de aprendizaje y criterios de evaluación

- **RA2**. Desarrolla aplicaciones compuestas por varios procesos reconociendo y aplicando principios de programación paralela.
  - **CE2a**. Se han identificado situaciones en las que resulte útil la utilización de varios hilos en un programa.
  - **CE2b**. Se han reconocido los mecanismos para crear, iniciar y finalizar hilos.
  - **CE2c**. Se han programado aplicaciones que implementen varios hilos.
  - **CE2d**. Se han identificado los posibles estados de ejecución de un hilo y programado aplicaciones que los gestionen.
  - **CE2e**. Se han utilizado mecanismos para compartir información entre varios hilos de un mismo proceso.
  - **CE2f**. Se han desarrollado programas formados por varios hilos sincronizados mediante técnicas específicas.
  - **CE2g**. Se ha establecido y controlado la prioridad de cada uno de los hilos de ejecución.
  - **CE2h**. Se han depurado y documentado los programas desarrollados.

# Corrutinas en acción: context y dispatcher

Las corrutinas se ejecutan siempre dentro de un contexto representado por el valor de un `CoroutineContext`, que es un conjunto de varios elementos. De estos elementos, el principal son los `Job` de las corrutinas.

Como ya hemos trabajado con los `Job` anteriormente, y ya sabemos, pues, qué es un un `CoroutineContext`, vamos a pasar directamente a ver qué son los denominados *dispatchers*.

## Dispatchers e hilos

Las corrutinas son ejecutas por hilos que crea Kotlin para ello.

Los denominados contextos de corrutina (*coroutine contexts*) incluyen *dispatchers* que determinan qué hilo o hilos ejecutarán la correspondiente corrutina.

Los *coroutine builders* como son `launch` y `async` aceptan un parámetro opcional en el que indicar el *dispatcher* que se usará para ejecutar la corrutina.

Estos *coroutine dispatchers* son los encargados de asignar la ejecución de la corrutina a un hilo, asignarlo a un *thread pool* o dejarlo en el hilo principal.

Aquí tienes un ejemplo en el que se lanzan una serie de corrutinas con el *coroutine builder* `launch` en el que, como ves, se usan diferentes *dispatchers*. El código está comentado para que se entienda qué hace cada instrucción y cómo usar diferentes contextos:

```kotlin
fun severalCoroutines() = runBlocking {
    // Como no se indica dispatcher, se usa el contexto del padre: runBlocking
    launch {
        println("main runBlocking        : Me está ejecutando el hilo ${Thread.currentThread().name}")
    }

    // El dispatcher Unconfined no está confinado a un hilo específico
    launch(Dispatchers.Unconfined) {
        println("Unconfined antes delay  : Me está ejecutando el hilo ${Thread.currentThread().name}")
        delay(100)
        println("Unconfined después delay: Me está ejecutando el hilo ${Thread.currentThread().name}")
    }

    // Usamos el dispatcher por defecto, ideal para tareas intensivas en CPU
    launch(Dispatchers.Default) {
        println("Default                 : Me está ejecutando el hilo ${Thread.currentThread().name}")
    }

    // Creamos nuestro propio contexto al que ponemos un nombre
    val myContext = newSingleThreadContext("MiPropioContext")
    try {
        launch(myContext) {
            println("newSingleThreadContext  : Me está ejecutando el hilo ${Thread.currentThread().name}")
        }
    } finally {
        myContext.close()
    }
}

fun main() {
    severalCoroutines()
}
```

## CoroutineScope: un caso de uso en Android

Cuando trabajas con aplicaciones en las que pueden haber objetos con un **Lifecycle** como es el **caso de aplicaciones para Android** se pueden producir pérdidas de memoria o *memory leaks* debido al mal uso de las corrutinas.

Por ejemplo, estamos escribiendo una aplicación para Android y lanza varias corrutinas en el contexto de una Actividad de Android para realizar operaciones asíncronas (obtener datos de la red, hacer animaciones, etc). Todas estas corrutinas deben ser canceladas cuando la Actividad es destruida para evitar esos *memory leaks*.

Nosotros, podemos manipular los contextos manualmente para asociarlos al ciclo de vida de la Actividad y sus corrutinas. En Kotlin lo **podemos hacer con las `CoroutineScope`**.

Lo que haremos será crear una instancia de `CoroutineScope` que esté asociada al ciclo de vida de la Actividad como te muestro en este ejemplo:

> Podemos crear instancias del CoroutineScope con `CoroutineScope()` o `MainScope()`.

```kotlin
class MyActivity {
    private val mainScope = MainScope()

    fun destroy() {
        mainScope.cancel()
    }
    
    fun doSomething() {
        // Se lanzan 10 corrutinas en el mainScope asociado al ciclo de vida
        // de esta actividad
        repeat(10) { i ->
            mainScope.launch {
                delay((i + 1) * 200L)
                println("Coroutine $i is done")
            }
        }
    }
}
```

## LaunchedEffect: un caso de uso en Jetpack Compose

En Jetpack Compose, las recomposiciones pueden ocurrir frecuentemente y de manera impredecible. Si queremos ejecutar una tarea que no debe reiniciarse con cada recomposición (como iniciar un flujo de datos, realizar una solicitud de red, o escuchar eventos), podemos usar `LaunchedEffect`. Este es un **componente clave para integrar corrutinas en composables** de manera segura y eficiente.

`LaunchedEffect` recibe dos argumentos:

- El primero es una clave (valor de una variable/objeto) que determinará cuándo se lanza la corrutina que se especifica en el segundo argumento

- El segundo argumento es la corrutina a ejecutar cuando la clave cambia de valor o hay una nueva recomposición

Es necesario lanzar corrutinas dentro de `LaunchedEffect` en los *composables* que escribas porque si dicho composable se elimina, las corrutinas se cancelarán y no continuarán ejecutándose.

Aquí te muestro un ejemplo básico de un `Composable` en el que se cargan datos en una corrutina y dicha carga se hace dentro de una corrutina. En este caso hemos pasado como clave `Unit` porque solo queremos lanzar la corrutina una sola vez, cuando se produce la recomposición:

```kotlin
@Composable
fun DataScreen(viewModel: DataViewModel) {
    val data by viewModel.data.collectAsState()

    // Se ejecuta una sola vez, cuando se recompone el composable
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Column {
        if (data.isEmpty()) {
            Text("Cargando datos...")
        } else {
            data.forEach { item ->
                Text(item)
            }
        }
    }
}
```

En este otro ejemplo te muestro cómo ejecutar la corrutina cada vez que hay un cambio de valor en una variable u objeto, que en este caso es el valor de la variable `userId`. Así pues, se ejecutará la corrutina cada vez que haya recomposición o que cambie el valor de `userId`:

```kotlin
@Composable
fun UserDetailsScreen(userId: String, viewModel: UserViewModel) {
    val user by viewModel.user.collectAsState()

    // Se lanza en cada recomposición o cambio de valor en userId
    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    Column {
        if (user == null) {
            Text("Cargando usuario...")
        } else {
            Text("Nombre: ${user.name}")
            Text("Email: ${user.email}")
        }
    }
}
```
