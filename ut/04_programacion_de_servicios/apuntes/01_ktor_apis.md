# Resultados de aprendizaje y criterios de evaluación

- **RA4**. Desarrolla aplicaciones que ofrecen servicios en red, utilizando librerías de clases y aplicando criterios de eficiencia y disponibilidad.
  - **CE4a**. Se han analizado librerías que permitan implementar protocolos estándar de comunicación en red.
  - **CE4b**. Se han programado clientes de protocolos estándar de comunicaciones y verificado su funcionamiento.
  - **CE4c**. Se han desarrollado y probado servicios de comunicación en red.
  - **CE4d**. Se han analizado los requerimientos necesarios para crear servicios capaces de gestionar varios clientes concurrentes.
  - **CE4e**. Se han incorporado mecanismos para posibilitar la comunicación simultánea de varios clientes con el servicio.
  - **CE4f**. Se ha verificado la disponibilidad del servicio.
  - **CE4g**. Se han depurado y documentado las aplicaciones desarrolladas.
  

# Ktor para creación de RESTful APIs

Con Ktor se pueden escribir RESTful APIs usando Kotlin. Tiene la ventaja de haber sido escrita por el equipo de Kotlin, que es el lenguaje de base que hemos estado usando.

## Comenzar proyectos con Ktor

Puedes generar un proyecto desde cero con la herramienta web [Start Ktor](https://start.ktor.io/settings).

## Añadir MongoDB

Existe una librería oficial de MongoDB para Kotlin que es la que vamos a usar nosotros. Para añadirla a tu proyecto tienes que añadir esta dependencia a tu Gradle:

```kotlin
implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")
```
