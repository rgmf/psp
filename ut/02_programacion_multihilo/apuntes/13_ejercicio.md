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

# Ejercicio propuesto: problema central en la criptografía RSA

En este ejercicio te propongo que escribas un programa en Kotlin para la **factorización de números compuestos grandes**, un problema exigente computacionalmente en el que podrás aprovechar el uso de corrutinas para mejorar los tiempos.

> Como se adelanta en el enunciado de este ejercicio, este es un problema central en la criptografía y, en concreto, en el algoritmo RSA.

El objetivo será descomponer varios números grandes en sus factores primos, mostrando dicha composición por pantalla.

Para descomponer cada número usarás el enfoque llamado *Trial Division*, que es simple pero computacionalmente costoso para números grandes.

Puedes encontrar información sobre el algoritmo *Trial Division* [en la wikipedia](https://en.wikipedia.org/wiki/Trial_division), donde verás un ejemplo en Python. El pseudocódigo del algoritmo es el siguiente:

```c
Función TrialDivision(n)
    Crear un conjunto vacío de factores: factores = {}

    Si n es divisible por 2
        Mientras n sea divisible por 2
            Agregar 2 a factores
            Dividir n entre 2

    Para i desde 3 hasta la raíz cuadrada de n, con paso de 2
        Mientras n sea divisible por i
            Agregar i a factores
            Dividir n entre i

    Si n es mayor que 1
        Agregar n a factores (n es primo)

    Retornar factores
Fin
```

Haz una **versión secuencial**, y sin uso de corrutinas, y **otra version con corrutinas**, y compara los tiempos. En la versión con corrutinas lo tienes "fácil": solo tienes que usar una corrutina para cada número del que haya que calcular sus factores primos.

Por ejemplo, con el siguiente conjunto de números:

```kotlin
var numbers = setOf(
    987_654_321_987_654_321L,
    1_234_567_890_123_456_789L,
    2_345_678_901_234_567_890L,
    3_456_789_012_345_678_901L
)
```

En mi ordenador, obtengo el siguiente resultado con los siguientes tiempos:

```text
Opción secuencial:
------------------------------------------------------------------------
Terminó en 27222 ms

987654321987654321 = 3 x 3 x 7 x 11 x 13 x 17 x 17 x 19 x 52579 x 379721
1234567890123456789 = 3 x 3 x 101 x 3541 x 3607 x 3803 x 27961
2345678901234567890 = 2 x 5 x 41 x 433 x 13212859242013
3456789012345678901 = 299170853 x 11554564817


Opción corrutinas:
-------------------------------------------------------------------------
Terminó en 11675 ms

987654321987654321 = 3 x 3 x 7 x 11 x 13 x 17 x 17 x 19 x 52579 x 379721
1234567890123456789 = 3 x 3 x 101 x 3541 x 3607 x 3803 x 27961
2345678901234567890 = 2 x 5 x 41 x 433 x 13212859242013
3456789012345678901 = 299170853 x 11554564817
```

# Solución

El resultado de este ejercicio lo tiene [Aquí](./workout/rsa_factors/).
