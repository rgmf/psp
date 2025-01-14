# Resultados de aprendizaje y criterios de evaluación

- **RA3**. Programa mecanismos de comunicación en red empleando sockets y analizando el escenario de ejecución.
  - **CE3c**. Se han reconocido librerías y mecanismos del lenguaje de programación que permiten programar aplicaciones en red.

# Bonus track: serialización con Kotlin

Aquí te resumo qué librerías y plugins puedes usar para serializar y deserializar objetos con Kotlin.

En la [página web oficial](https://kotlinlang.org/docs/serialization.html) puedes leer todos los detalles.

¿Qué es serializar? En este contexto, serializar es el proceso de convertir los datos que queremos enviar por la red a un formato que lo permita. Básicamente, convertir objetos de Kotlin a un `String`.

¿Qué es deserializar? El proceso inverso, es decir, coger un `String` que representa a un objeto de Kotlin y convertirlo a dicho objeto.

En los servicios web se usa el **formato JSON** para transferir datos. Es habitual, por tanto, que los servidores transfieran la información en dicho formato JSON y así lo vamos a hacer con nuestors sockets del lado del servidor.

Al final de este apartado te muestro un ejemplo de cómo convertir un objeto de Kotlin a un `String` en formato JSON y cómo, a partir de un JSON, volver a obtener el objeto de Kotlin original.

## Configurar Gradle

Lo primero es añadir a **Gradle** el plugin:

```kotlin
plugins {
    alias(libs.plugins.kotlin.jvm)
    kotlin("plugin.serialization") version "1.9.0"
    application
}
```

Y la siguiente dependencia:

```kotlin
dependencies {
    // El resto de dependencias
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}
```

## Uso de la librería

A partir de aquí podrías crear `data classes` de Kotlin con la anotación `@Serializable`:

```kotlin
import kotlinx.serialization.Serializable

@Serializable
data class Point(
    val id: Int?,
    val latitude: Double,
    val longitude: Double
)
```

Y ya podrías obtener el JSON en formato `String` del `data class`:

```kotlin
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

val point = Point(-0.84, 96.01)
val json: String = Json.encodeToString(point)
```

Y la operación inversa: decodificar un `String` para obtener la clase `Point`:

```kotlin
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

val stringJson = """{"latitude": -0.84, "longitude": 96.01}"""
val point = Json.decodeFromString<Point>(stringJson)
```
