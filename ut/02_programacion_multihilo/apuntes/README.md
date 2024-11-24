# Ficha pedagógica

<table>
  <thead>
    <tr><th colspan="2">Ficha pedagógica</th></tr>
  </thead>
  <tbody>
    <tr>
      <th>Temporalización</th><td>TODO</td>
    </tr>
    <tr>
      <th>Objetivos generales</th><td>b, e, i, j, l, n, ñ</td>
    </tr>
    <tr>
      <th>Competencias</th><td>e, i, j, n, t, w</td>
    </tr>
    <tr>
      <th>CONTENIDOS</th>
      <th>CRITERIOS DE EVALUACIÓN</th>
    </tr>
    <tr>
      <td>
        <p>
          1. Hilos<br>
          1.1. ¿Por qué hilos y programación multihilo?<br>
        </p>
        <p>
          2. Ahora sí: ¿hilos o procesos?<br>
          2.1. Escenarios adecuados para multihilo<br>
          2.2. Escenarios adecuados para multiproceso<br>
        </p>
        <p>
          3. Medir tiempos de ejecución en Kotlin<br>
        </p>
        <p>
          4. La clase Thread<br>
          4.1. Ejemplo básico<br>
          4.2. Memoria compartida<br>
        </p>
        <p>
          5. La clase Runnable<br>
          5.1. Creando tareas: heredando de Runnable<br>
        </p>
        <p>
          6. Ejercicio propuesto: hilos con lógica sencilla<br>
        </p>
        <p>
          7. Ejercicio propuesto: hilos con lógica más complicada<br>
        </p>
        <p>
          8. Sincronización de hilos<br>
          8.1. Race conditions<br>
          8.2. Mutex o exclusión mutua<br>
          8.3. Deadlock o bloqueo mutuo
        </p>
        <p>
          9. Ejercicio propuesto: sincronización de hilos
        </p>
        <p>
          10. Corrutinas<br>
          10.1. Introducción a las corrutinas<br>
          10.2. kotlinx.coroutines<br>
          10.3. Relación entre corrutinas e hlos
        </p>
        <p>
          11. Corrutinas en acción: iniciación<br>
          11.1 Tu primera corrutina<br>
          11.2. Tu primera función suspendible<br>
          11.3. Construcción de contexto o scope builder: coroutineScope y runBlocking
          11.4. Esperando a las corrutinas: la clase Job
          11.5. Async: un launch que devuelve valores
        </p>
        <p>
          12. Corrutinas en acción: cancelaciones y timeout<br>
          12.1. Cancelación de corrutinas lanzadas con launch<br>
          12.2. Cancelación de corrutinas lanzadas con async
        </p>
        <p>
          13. Corrutinas en acción: concurrencia con funciones suspend<br>
          13.1. Secuencial por defecto<br>
          13.2. Concurrencia con async
        </p>
        <p>
          14. Ejercicio propuesto: descarga de ficheros simulada
        </p>
        <p>
          15. Corrutinas en acción: context y dispatcher<br>
          15.1. Dispatchers e hilos<br>
          15.2. CoroutineScope: un caso de uso en Android
        </p>
        <p>
          16. Corrutinas en acción: Flow<br>
          16.1. Ejemplo práctico: simulación de un sensor de temperatura
        </p>
      </td>
      <td>
        CE2a. Se han identificado situaciones en las que resulte útil la utilización de varios hilos en un programa.<br>
        CE2b. Se han reconocido los mecanismos para crear, iniciar y finalizar hilos.<br>
        CE2c. Se han programado aplicaciones que implementen varios hilos.<br>
        CE2d. Se han identificado los posibles estados de ejecución de un hilo y programado aplicaciones que los gestionen.<br>
        CE2e. Se han utilizado mecanismos para compartir información entre varios hilos de un mismo proceso.<br>
        CE2f. Se han desarrollado programas formados por varios hilos sincronizados mediante técnicas específicas.<br>
        CE2g. Se ha establecido y controlado la prioridad de cada uno de los hilos de ejecución.<br>
        CE2h. Se han depurado y documentado los programas desarrollados.<br>
      </td>
    </tr>
  </tbody>
</table>

# Índice de contenidos

1. [Hilos](01_hilos.md)
2. [Hilos vs procesos](02_hilos_vs_procesos.md)
3. [Medir tiempos de ejecución en Kotlin](03_medir_tiempos.md)
4. [La clase Thread](04_clase_thread.md)
5. [La clase Runnable](05_clase_runnable.md)
6. [Ejercicio propuesto: hilos con lógica sencilla](06_ejercicio.md)
7. [Ejercicio propuesto: hilos con lógica más complicada](07_ejercicio.md)
8. [Sincronización de hilos](08_sync_mutex_lock.md)
9. [Ejercicio propuesto: sincronización de hilos](09_ejercicio.md)
10. [Corrutinas](10_coroutines_intro.md)
11. [Corrutinas en acción: iniciación](11_coroutines_in_action.md)
12. [Corrutinas en acción: cancelaciones y timeout](12_coroutines_cancelation_timeout.md)
13. [Corrutinas en acción: concurrencia con funciones suspend](13_coroutines_concurrencia.md)
14. [Ejercicio propuesto: descarga de ficheros simulada](14_ejercicio.md)
15. [Corrutinas en acción: context y dispatcher](15_coroutines_context_dispatcher.md)
16. [Corrutinas en acción: Flow](16_coroutines_flow.md)
