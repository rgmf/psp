# Resultados de aprendizaje y criterios de evaluación

- **RA4**. Desarrolla aplicaciones que ofrecen servicios en red, utilizando librerías de clases y aplicando criterios de eficiencia y disponibilidad.
  - **CE4e**. Se han incorporado mecanismos para posibilitar la comunicación simultánea de varios clientes con el servicio.
  - **CE4f**. Se ha verificado la disponibilidad del servicio.
  - **CE4g**. Se han depurado y documentado las aplicaciones desarrolladas.
  

# cURL: comandos útiles

Te explico en este apartado una herramienta importante para probar APIs: **cURL**.

Existen otras herramientas gráficas (GUI) más intuitivas, pero es importante conocer **cURL**.

Verás en los ejemplos que introduzco una *pipe* para dirigir la salida del comando `curl` a otro comando llamado `jq` que no es más que una utilidad para formatear la salida para que sea más legible.

Vas a necesitar tener instalado `curl` y `jq` (este último es opcional).

## GET

Hacemos un *GET* a la API de Rick And Morty para obtener todos los personajes (y toda la información):

```shell
$ curl -X GET https://rickandmortyapi.com/api/character | jq
```

## POST

Envíamos un JSON con datos de una persona (nombre y apellidos) a una API ficticia que está corriendo en *localhost* puerto *8080*:

```shell
$ curl -X POST localhost:8080/api/person --header 'Content-Type: application/json' --data-raw '{"firstName": "Alice", "lastName": "Bob"}'
```

Verás que indicamos, vía **header**, el `Content-Type` del cuerto de la petición, en este caso `application/json`.
