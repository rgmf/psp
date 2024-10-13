# Resultados de aprendizaje y criterios de evaluación

- **RA2**. Desarrolla aplicaciones compuestas por varios procesos reconociendo y aplicando principios de programación paralela.
  - **CE2b**. Se han reconocido los mecanismos para crear, iniciar y finalizar hilos.
  - **CE2c**. Se han programado aplicaciones que implementen varios hilos.
  - **CE2d**. Se han identificado los posibles estados de ejecución de un hilo y programado aplicaciones que los gestionen.

# Ejercicio propuesto

Diseña un programa en Kotlin que calcule el promedio de temperaturas de una ciudad en los últimos 7 días. El programa deberá obtener las temperaturas de tres fuentes diferentes de datos: tres ficheros de texto diferentes. La tarea se dividirá en tres hilos:

- Hilo 1: Leerá las temperaturas del primer fichero.
- Hilo 2: Leerá las temperaturas del segundo fichero.
- Hilo 3: Leerá las temperaturas del tercer fichero.

Cada hilo debe recuperar las temperaturas para los mismos 7 días y almacenar los valores en una lista. Cuando todos los hilos terminen, el programa principal deberá calcular el promedio de todas las temperaturas obtenidas y mostrar el resultado en pantalla.

Requisitos:

- El programa debe usar tres hilos para recuperar los datos simultáneamente, de modo que no se bloqueen entre sí.
- El hilo principal debe esperar a que los tres hilos terminen antes de calcular el promedio.
- Usa las funciones de la clase `Thread` para crear y gestionar los hilos.
- Crea una clase que herede de `Runnable` para reutilizar el código de lectura de ficheros. El constructor de la clase recibirá la ruta del fichero a leer y la lista de números de tipo `Float` donde se insertarán las 7 temperaturas leídas.
- Añade un `Thread.sleep()` en los hilos para simular que las tareas tardan más de lo que realmente tardan en leer del fichero.

Salida esperada: El programa debe **imprimir las temperaturas medias de cada día** y la **temperatura promedio de la semana**.
