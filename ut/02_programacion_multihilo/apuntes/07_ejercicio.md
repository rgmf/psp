# Resultados de aprendizaje y criterios de evaluación

- **RA2**. Desarrolla aplicaciones compuestas por varios procesos reconociendo y aplicando principios de programación paralela.
  - **CE2b**. Se han reconocido los mecanismos para crear, iniciar y finalizar hilos.
  - **CE2c**. Se han programado aplicaciones que implementen varios hilos.
  - **CE2d**. Se han identificado los posibles estados de ejecución de un hilo y programado aplicaciones que los gestionen.

# Ejercicio propuesto

Escribe un programa en Kotlin que calcule el promedio de temperaturas de una ciudad en los últimos 7 días. El programa deberá obtener las temperaturas de dos fuentes diferentes de datos: dos ficheros de texto que recibirá por la línea de entrada de comandos.

Cada fichero tendrá 7 temperaturas que corresponden a las temperaturas de lunes, martes, miércoles, jueves, viernes, sábado y domingo. Cada temperatura se tiene en una línea separada. Por ejemplo, este podría ser el contenido de uno de los ficheros:

```text
25.5
27
27.75
28.0
26.45
30
29.3
```

> Para simplificar las cosas y no distraernos con la validación de la entrada, **vamos a dar por sentado que los ficheros que recibimos tienen exactamente 7 números, uno por línea, como se ve en el ejemplo de arriba**.

A partir de ahí la tarea se dividirá en dos hilos:

- Hilo 1: Leerá las temperaturas del primer fichero.
- Hilo 2: Leerá las temperaturas del segundo fichero.

Requisitos:

- El programa debe usar dos hilos para recuperar los datos simultáneamente, de modo que no se bloqueen entre sí.
- El hilo principal debe esperar a que los dos hilos terminen antes de calcular el promedio.
- Usa las funciones de la clase `Thread` para crear y gestionar los hilos.
- Crea una clase que herede de `Runnable` para reutilizar el código de lectura de ficheros. Este será la signatura de la clase:

```kotlin
class TemperatureReader(private val filePath: String) : Runnable {

    private val _temperatures: MutableList<Float> = mutableListOf()
    val temperatures
        get() = _temperatures

    override fun run() {
        TODO()
    }
}
```

- Añade un `Thread.sleep()` en el método `run()` del `Runnable` para simular que las tareas tardan más de lo que realmente tardan en leer del fichero. Por ejemplo, para hacer que se duerman un tiempo al azar entre 2 y 5 segundos puedes hacer esto:

```kotlin
val millisRange = 2000..5000
Thread.sleep(millisRange.random())
```

Salida esperada: El programa debe **imprimir las temperaturas medias de cada día** y la **temperatura promedio de la semana**.

```shell
Temperaturas medias de los días de la semana:
25.6666665
27.0
28.0
25.6666665
24.6666665
25.3333335
26.6666665

Temperatura media de la semana: 26,1428570
```

# Ejercicio avanzado

Modifica el programa anterior para aceptar cualquier número de ficheros de texto. En vez de recibir por la línea de entrada de comandos dos ficheros, recibirá un directorio del que leerá todos los ficheros que hay y hará lo mismo pero con N ficheros, los que encuentre dentro de dicha carpeta.

El resultado de este ejercicio lo tiene [Aquí](./workout/temperatures/) y puedes descargarlo en formato comprimido para abrirlo en tu editor de código favorito [aquí](./workout/temperatures.zip).
