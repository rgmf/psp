# Resultados de aprendizaje y criterios de evaluación

- **RA3**. Programa mecanismos de comunicación en red empleando sockets y analizando el escenario de ejecución.
  - **CE3a**. Se han identificado escenarios que precisan establecer comunicación en red entre varias aplicaciones.
  - **CE3b**. Se han identificado los roles de cliente y de servidor y sus funciones asociadas.
  - **CE3c**. Se han reconocido librerías y mecanismos del lenguaje de programación que permiten programar aplicaciones en red.
  - **CE3d**. Se ha analizado el concepto de socket, sus tipos y características.
  - **CE3e**. Se han utilizado sockets para programar una aplicación cliente que se comunique con un servidor.
  - **CE3f**. Se ha desarrollado una aplicación servidor en red y verificado su funcionamiento.
  - **CE3g**. Se han desarrollado aplicaciones que utilizan sockets para intercambiar información.
  - **CE3h**. Se han utilizado hilos para implementar los procedimientos de las aplicaciones relativos a la comunicación en red.

# Programación de sockets con Kotlin

Lo primero: antes de empezar con la programación de sockets en Kotlin tienes que saber que tienes incluir esta dependencia a tu fichero de **Gradle**:

```kotlin
dependencies {
    implementation("io.ktor:ktor-network:3.0.2")
}
```

## Tu primer servidor

Vayamos directos al grano con el código necesario en Kotlin para escribir un servidor que abre un socket, queda a la espera de conexiones y todo lo que recibe lo devuelve tal cual (un "eco server").

El código está comentado para que entiendas cada paso que se da, cada línea que se escribe:

```kotlin
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*

// 1. Internamente, los sockets en Kotlin, usan corrutinas, de ahí el runBlocking.
fun main() = runBlocking {
    // 2. El SelectorManager configura las corrutinas que hay por detrás de los sockets en Kotlin.
    val selectorManager = SelectorManager(Dispatchers.IO)
    
    // 3. Preparamos el server socket, en este caso un Stream Socket (TCP). Indicamos:
    //    - Dirección IP de la máquina en la que está el servidor.
    //    - Puerto en el que escuchará las peticiones.
    val serverSocket = aSocket(selectorManager).tcp().bind("127.0.0.1", 9002)

    // 4. Los servidores son programas que se ejecutan "infinitamente".
    while (true) {
        // 5. Bloqueamos el servidor a la espera de conexiones.
        val socket = serverSocket.accept()

        // 6. Llegados aquí, el servidor está conectado a un cliente y creamos un
        //    canal para recibir mensajes y otro para enviar mensajes al cliente.
        val receiveChannel = socket.openReadChannel()
        val sendChannel = socket.openWriteChannel(autoFlush = true)
        try {
            while (true) {
                // 7. Recibimos mensajes y respondemos a cada uno de ellos.
                val message = receiveChannel.readUTF8Line()
                sendChannel.writeStringUtf8("Me ha llegado este mensaje: $message\n")
            }
        } catch (e: Throwable) {
            // 8. Cuando se desconecta el cliente, se enviará un excepción y llegaremos
            //    aquí para cerrar el socket que nos conectaba al cliente.
            socket.close()
        }
    }
}
```

Hay algo en el código de arriba que debería llamar tu atención. No parece lo más adecuado que el socket se cierre, y termine la comunicación, a través de una excepción. En clase, te explicaré cómo adaptar el código de arriba para terminar de forma natural.

### Probar tu servidor

Para comprobar que tu servidor funciona correctamente sin necesidad de escribir un cliente puedes usar la herramienta **Netcat** que es una utilidad en línea de comandos que permite abrir conexiones TCP y UDP.

> Puedes leer los detalles de esta herramienta [desde aquí](https://man.archlinux.org/man/netcat.1.en).

Para lanzar **Netcat** solo tienes que especificar la IP y el puerto del servidor con el que quieres abrir una conexión. Por ejemplo, para probar el servidor del apartado anterior tienes que usar la IP `127.0.0.1` y el puerto `9002`:

```shell
$ ncat 127.0.0.1 9002
```

> Para usar **Netcat** en Windows tendrás que instalar **nmap** [desde aquí](https://nmap.org/download.html#windows).

## Tu primer cliente

Si quieres escribir un programa cliente para acceder a los servicios que ofrece un servidor a través de un socket tienes que crear un socket y conectarlo a la dirección IP y puerto al que escucha el servidor.

A partir de ahí podrás enviar mensajes y recibir las respuestas desde el servidor.

En Kotlin, el código básico sería el siguiente:

```kotlin
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*

// 1. Internamente, los sockets en Kotlin, usan corrutinas, de ahí el runBlocking.
fun main() = runBlocking {
    // 2. Crea un SelectorManager y crea un socket para conectar a la dirección IP
    //    y puerto en el que escucha el servidor (en este caso 127.0.0.1:9090).
    val selectorManager = SelectorManager(Dispatchers.IO)
    val socket = aSocket(selectorManager).tcp().connect("127.0.0.1", 9090)

    // 3. Crea el canal para enviar y recibir datos (mensajes).
    val receiveChannel = socket.openReadChannel()
    val sendChannel = socket.openWriteChannel(autoFlush = true)

    // 4. Envíamos 10 mensajes al servidor, esperamos respuesta y la imprimos por pantalla.
    for (i in 1..10) {
        sendChannel.writeStringUtf8("Hola $i\n")
        val response = receiveChannel.readUTF8Line()
        println(response)
    }

    // 5. Cerramos socket y recursos.
    socket.close()
    selectorManager.close()
}
```

## Consideraciones y cuidados a tener en cuenta

Aquí tienes una lista de consideraciones y cuidados que debes tener cuando programas sockets (cliente y servidor):

- Cada vez que envíes un mensaje escribe un `\n` al final. Cuando un usuario escribe un mensaje desde el teclado termina con un `intro`. En los programas que escribas este intro es un `\n`.

- Crea un mecanismo para no terminar abruptamente la conexión entre cliente y servidor. Por ejemplo, puedes enviar desde el cliente un mensaje al servidor como "CLOSE\n" o "END\n", por ejemplo, de manera que el cierre del socket esté sincronizado y acordado entre ambos.

- Asegúrate que hay una sincronización perfecta: cada *send* tiene que venir seguido de un *receive* en el otro lado o tu programa se quedará colgado esperando.
