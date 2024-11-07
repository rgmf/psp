# Resultados de aprendizaje y criterios de evaluación

- **RA2**. Desarrolla aplicaciones compuestas por varios procesos reconociendo y aplicando principios de programación paralela.
  - **CE2a**. Se han identificado situaciones en las que resulte útil la utilización de varios hilos en un programa.
  - **CE2b**. Se han reconocido los mecanismos para crear, iniciar y finalizar hilos.
  - **CE2c**. Se han programado aplicaciones que implementen varios hilos.
  - **CE2d**. Se han identificado los posibles estados de ejecución de un hilo y programado aplicaciones que los gestionen.
  - **CE2e**. Se han utilizado mecanismos para compartir información entre varios hilos de un mismo proceso.
  - **CE2h**. Se han depurado y documentado los programas desarrollados.

# Ejercicio propuesto

Escribe un programa en Kotlin que reciba **por la línea de comandos** un número entero.

A partir de ahí, el programa creará dos hilos que busquen los números primos que hay entre el 2 y dicho número pasado por la línea de comandos. El primer hilo se encargará de buscar en la primara mitad del número y el segundo el resto.

Por ejemplo, si el programa recibe el número 1000, entonces el programa creará dos hilos que buscarán estos primos:

- El hilo 1 buscará entre 2 y 500 (que es 1000 / 2).
- El hilo 2 buscará entre 501 y 1000.

Al final, el proceso que creó los dos hilos mostrará por pantalla la lista de números primos.

En este fragmento de código tienes una función que devuelve `true` o `false` en función de si el número dado por parámetro es primo o no:

```kotlin
fun isPrime(n: Int): Boolean {
    for (i in 2..Math.sqrt(n.toDouble()).toInt()) {
        if (n % i == 0) {
            return false
        }
    }
    return true
}
```