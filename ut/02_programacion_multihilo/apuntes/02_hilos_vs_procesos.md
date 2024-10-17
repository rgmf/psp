# Resultados de aprendizaje y criterios de evaluación

- **RA2**. Desarrolla aplicaciones compuestas por varios procesos reconociendo y aplicando principios de programación paralela.
  - **CE2a**. Se han identificado situaciones en las que resulte útil la utilización de varios hilos en un programa.
  - **CE2d**. Se han identificado los posibles estados de ejecución de un hilo y programado aplicaciones que los gestionen.

# Ahora sí: ¿hilos o procesos?

Una vez has aprendido a escribir programas multiproceso y antes de meternos de lleno en la programación multihilo me gustaría que entendieras en qué situaciones es más idóneo el uso de una tipo de programación u otra.

## Escenarios adecuados para el multihilo

La programación **multihilo** es generalmente preferida **cuando se trata de comunicación y sincronización entre tareas**.

Los hilos de un mismo proceso comparten el mismo espacio de memoria, lo que facilita el intercambio de datos y hace que la **comunicación y sincronización entre hilos sea más sencilla y rápida que entre procesos**.

Sin embargo, los hilos también comparten los mismos recursos del proceso, lo que puede causar problemas de concurrencia, como las condiciones de carrera, y exigir un manejo cuidadoso.

> Hablaremos más adelante de los problemas que provoca la concurrencia, como las temidas *race conditions* o *deadlocks*.

## Escenarios adecuados para el multiproceso

En algunos casos, como el **procesamiento de datos de alto rendimiento o la programación distribuida**, la **programación multiproceso es más adecuada**.

Esto se debe a que los procesos son independientes y no comparten el mismo espacio de memoria. En programación distribuida, los procesos pueden ejecutarse en diferentes máquinas en una red, lo cual no es posible con hilos debido a que los hilos deben estar dentro del mismo proceso y, por lo tanto, en la misma máquina.

Además de la programación distribuida, se utiliza **programación multiproceso para aprovechar mejor los núcleos de CPU en casos donde se necesita aislamiento y estabilidad**. Dado que los procesos están aislados entre sí, errores en un proceso no afecta a otros. Esto es útil en aplicaciones de alta disponibilidad y sistemas que requieren robustez.
