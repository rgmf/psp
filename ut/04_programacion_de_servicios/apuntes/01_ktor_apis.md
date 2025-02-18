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

Nosotros vamos a elegir los siguientes plugins/dependencias:

- `kotlinx.serializatin`
- `Content Negotiation`
- `Routing`
- `Static Content`
- `Authentication JWT`

Además, necesitaremos añadir MongoDB como se indica en el siguiente apartado.

## Añadir MongoDB

Existe una librería oficial de MongoDB para Kotlin que es la que vamos a usar nosotros. Para añadirla a tu proyecto tienes que añadir esta dependencia a tu Gradle:

```kotlin
implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")
```

Una forma básica de obtener el cliente de MongoDB y una base de datos desde un programa con Kotlin, una vez añadida la dependencia anterior, sería tal que así:

```kotlin
val pojoCodecRegistry = fromRegistries(
    MongoClientSettings.getDefaultCodecRegistry(),
    fromProviders(PojoCodecProvider.builder().automatic(true).build())
)

val mongoConnString = "mongodb://dam:dam@localhost:27017"
val connectionString = ConnectionString(mongoConnString)

val settings = MongoClientSettings.builder()
    .applyConnectionString(connectionString)
    .codecRegistry(pojoCodecRegistry)
    .build()

val client = MongoClient.create(settings)
val database = client.getDatabase("damdb")
```

## Encriptar/Desencriptar contraseñas

Las contraseñas no se deben almacenar, nunca, bajo ningún concepto, sin cifrar o *hashear*. Así, cada vez que se registre un usuario tendremos que usar una función *hash* para enmascarar/cifrar la contraseña.

Existen muchas opciones pero nosotros nos vamos a decantar por una opción: usar el algoritmo **bcrypt**.

Necesitaremos una librería para ello. Os propongo usar la siguiente:

```kotlin
mplementation("at.favre.lib:bcrypt:0.10.2")
```

Añádela a tu *Gradle* y empieza a usarla.

Para encriptar la contraseña tendrás que hacer lo siguiente:

```kotlin
val bcryptHashPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
```

Donde `password` es la contraseña en plano, tal cual nos llegó y `bcryptHashPassword` la contraseña que tendremos que guardar en la base de datos.

Para comprobar, a la hora de hacer login, que la contraseña facilitada por el usuario es válida tendremos que usar esta función:

```kotlin
val result = BCrypt.verifyer().verify(password.toCharArray(), bcryptHashPassword);
```

Donde `password` es la contraseña con la que el usuario probó a iniciar sesión y `bcryptHashPassword` la contraseña que tenemos en la base de datos.

## Autenticación

Para la autenticación en APIs lo habitual es usar **JWT** (*JSON Web Token*). En clase te explicaré en qué consiste para poder usarla en las prácticas.

En la web de Ktor tienes algunos enlaces que te resultarán de ayuda:

- [Autenticación en Ktor](https://ktor.io/docs/server-auth.html)
- [Autenticación con Bearer en Ktor](https://ktor.io/docs/server-bearer-auth.html)
- [Autenticación con JWT en Ktor](https://ktor.io/docs/server-jwt.html)

Si no añadiste el plugin/dependencia para la autenticación en Ktor cuando creaste el proyecto, lo puedes hacer llevando a cabo estos dos pasos:

1. Esto en tu fichero `libs.versions.toml`:

```kotlin
ktor-server-auth = { module = "io.ktor:ktor-server-auth", version.ref = "ktor-version" }
ktor-server-auth-jwt = { module = "io.ktor:ktor-server-auth-jwt", version.ref = "ktor-version" }
```

2. Y esto otro en la sección `dependencies` del fichero `build.gradle.kts`:

```kotlin
implementation(libs.ktor.server.auth)
implementation(libs.ktor.server.auth.jwt)
```

Y, por último, te muestro los pasos, en general, que tienes que dar para usar autenticación en tus REST APIs con Ktor.

1. Configura JWT usando un grupo `jwt` en el fichero de configuración `application.yaml`. No es esta la mejor manera de configurar el grupo `jwt` y JWT en general, pero no es el objetivo ahora mismo aplicar las técnicas que deberíamos sobre la seguridad:

```kotlin
jwt:
    secret: "secret"
    issuer: "http://0.0.0.0:8080/"
    audience: "http://0.0.0.0:8080/damgram"
    realm: "Access to DAMgram"
```

2. Instala el módulo de autenticación JWT en el módulo donde lo quieras usar y añade el `verifier` tal cual ves y usa un `validate` como el que observas en el código de abajo. En el código de abajo se entiende que en el *payload* del token se tiene el `username`. Fíjate cómo, al principio, obtenemos los valores que configuramos en el paso anterior:

```kotlin
val myRealm = environment.config.property("jwt.realm").getString()
val secret = environment.config.property("jwt.secret").getString()
val issuer = environment.config.property("jwt.issuer").getString()
val audience = environment.config.property("jwt.audience").getString()

jwt("jwt-auth") {
    realm = myRealm

    // Verifica que el token sea un token válido así como la signatura
    verifier(JWT
        .require(Algorithm.HMAC256(secret))
        .withAudience(audience)
        .withIssuer(issuer)
        .build())

    // Valida el payload
    validate { credential ->
        if (credential.payload.getClaim("username").asString() != "") {
            JWTPrincipal(credential.payload)
        } else {
            null
        }
    }
    
    // Configura una respuesta cuando la autenticación falle
    challenge { defaultScheme, realm ->
        call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
    }
}
```

3. Crea una ruta para el login que devuelva un token cuando dicho login sea correcto:

```kotlin
post("/login") {
    // Obtenemos el objeto JSON con un usuario
    val user = call.receive<UserRequest>()

    // Aquí tendrás que verificar que dicho usuario es válido
    // y existe en el sistema
    // ...

    // Si el usuario no existe se devolvería un 401
    // ...
    
    // Si es válido, podemos crear y devolver el token
    val token = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("username", user.username)
        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
        .sign(Algorithm.HMAC256(secret))

    call.respond(hashMapOf("token" to token))
}
```

4. Ya puedes proteger las rutas para que solo los usuarios que usen un token válido tengan permisos sobre ellas:

```kotlin
authenticate("jwt-auth") {
    get("/ruta/protegida") {
        // En principal tienes los datos del JWT que necesites.
        val principal = call.principal<JWTPrincipal>()
        
        // Aquí imprimo algunos valores:
        val username = principal!!.payload.getClaim("username").asString()
        val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
        
        println("Username: $username")
        println("Expiration token at: $expiresAt")
    }
}
```
