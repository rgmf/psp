# Resultados de aprendizaje y criterios de evaluación

- **RA2**. Desarrolla aplicaciones compuestas por varios procesos reconociendo y aplicando principios de programación paralela.
  - **CE2a**. Se han identificado situaciones en las que resulte útil la utilización de varios hilos en un programa.
  - **CE2b**. Se han reconocido los mecanismos para crear, iniciar y finalizar hilos.
  - **CE2c**. Se han programado aplicaciones que implementen varios hilos.
  - **CE2d**. Se han identificado los posibles estados de ejecución de un hilo y programado aplicaciones que los gestionen.
  - **CE2e**. Se han utilizado mecanismos para compartir información entre varios hilos de un mismo proceso.
  - **CE2f**. Se han desarrollado programas formados por varios hilos sincronizados mediante técnicas específicas.
  - **CE2h**. Se han depurado y documentado los programas desarrollados.

# Ejercicio propuesto: sincronización de hilos

El siguiente programa tiene un problema de sincronización que tienes que resolver. Te explico.

Se trata de un programa de gestión de cuentas bancarias en el que varios usuarios pueden acceder a dichas cuentas para hacer retiradas e ingresos de dinero, y lo podrían hacer al mismo tiempo. Como ves en el código de partida, he añadido un `Thread.sleep(500)` en cada método para simular que la operación es relativamente pesada.

En el `main` puedes ver como se simula el acceso simultáneo de 10 usuarios a una misma cuenta bancaria. Cada hilo es un usuario. Los hilos pares ingresan 50€ y los hilos impares retiran 100€. Pero al ejecutarlo varias veces me encuentro con diferentes resultados, lo cual significa que algo va mal, que no se está sincronizando correctamente el problema:

- Ejecución 1. Resultado por pantalla:

```text
Operaciones completadas. El balance es de 750€
```

- Ejecución 2. Resultado por pantalla:

```text
Operaciones completadas. El balance es de 850€
```

- Ejecución 3. Resultado por pantalla:

```text
Operaciones completadas. El balance es de 700€
```

- Ejecución 4. Resultado por pantalla:

```text
Operaciones completadas. El balance es de 750€
```

Ahora se trata de que "pongas orden" en esta aplicación multihilo para que los hilos no se chafen entre ellos.

Este es el código de partida:

```kotlin
class BankAccount {
    private var _balance = 1000
    val balance: Int
        get() = _balance

    fun deposit(amount: Int) {
        Thread.sleep(500)
        _balance += amount
    }

    fun withdraw(amount: Int): Boolean {
        Thread.sleep(500)
        if (_balance >= amount) {
            _balance -= amount
            return true
        } else {
            return false
        }
    }
}

fun main() {
    val account = BankAccount()

    val threads = List(10) { i ->
        thread {
            if (i % 2 == 0) {
                account.deposit(50)
            } else {
                account.withdraw(100)
            }
        }
    }

    threads.forEach { it.join() }

    println("Operaciones completadas. El balance es de ${account.balance}€")
}
```

El ejercicio está resuelto en [Aquí](./workout/bank/) y puedes descargarlo en formato comprimido para abrirlo en tu editor de código favorito [aquí](./workout/bank.zip).