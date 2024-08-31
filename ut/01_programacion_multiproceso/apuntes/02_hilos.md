# Resultados de aprendizaje y criterios de evaluación

- **RA1**. Desarrolla aplicaciones compuestas por varios procesos reconociendo y aplicando principios de programación paralela.
  - **CE1c**. Se han analizado las características de los procesos y de su ejecución por el sistema operativo.
  - **CE1d**. Se han caracterizado los hilos de ejecución y descrito su relación con los procesos.

# Hilos

Cuando un Sistema Operativo crea un proceso, este le asigna un espacio de direcciones de memoria y un hilo de control. Sin embargo, con frecuencia, hay situaciones en las que es conveniente tener varios hilos de control dentro del mismo proceso que se ejecutan casi en paralelo, como si fueran procesos separados (excepto por el espacio de direcciones compartido de memoria).

¿Por qué complicar las cosas con esta idea o concepto? Hay varias razones para que estos *miniprocesos*, conocidos como hilos, sean útiles y necesarios. Aquí tienes algunas de esas razones:

- La principal razón de tener hilos es que en muchas aplicaciones se desarrollan varias actividades a la vez.
- Algunas de ésas se pueden bloquear de vez en cuando.
- Al descomponer una aplicación en varios hilos secuenciales que se ejecutan casi en paralelo, el modelo de programación se simplifica.
- Los hilos comparten el mismo espacio de direcciones de memoria.

Entonces, ¿cuándo usar multiproceso o multihilo? De forma muy básica: usaremos hilos para actividades pequeñas dentro de un mismo proceso, donde se tienen que crear y destruir hilos rápidamente; y usaremos procesos para dividir tareas pesadas dentro de un proceso que llevan mucho tiempo.

> La experiencia, como en todos los ámbitos de la vida, te harán comprender cuándo usar procesos y cuando usar hilos dentro de un mismo proceso. Cuando comencemos con las prácticas lo acabarás de entender.

Los procesos son unidades independientes de ejecución aunque con una relación jerárquica como hemos visto. Cada proceso tiene su propio espacio de memoria independiente al que solo él puede acceder (el Sistema Operativo protege la memoria de cada proceso para que solo él pueda acceder a esa área).

Crear procesos permite aprovechar las capacidades multiprocesador de los sistemas actuales, permite reducir tiempos de ejecución y dividir tareas pesadas. La contrapartida es que, desde el punto de vista del Sistema Operativo, la creación de procesos es costoso en recursos y, por tanto, no siempre resultará en una mejor opción: se puede llegar al caso que un programa multiproceso tarde más tiempo en realizar una tarea que un programa formado por un único proceso. Esto se debe a que crear un proceso también requiere tiempo de computación.

> Según Andrew Tanenbaum, en su libro Sistemas Operativo Modernos, la creación de hilos puede ser de 10 a 100 veces más rápido que la creación de procesos. Así pues, si se van a realizar tareas que acaban rápido quizás crear un proceso no sea conveniente.
