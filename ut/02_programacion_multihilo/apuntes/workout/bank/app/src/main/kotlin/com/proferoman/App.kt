package com.proferoman

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock

class BankAccount {
    private var _balance = 1000
    val balance: Int
        get() = _balance

    private val mutex = ReentrantLock()

    fun deposit(amount: Int) {
        mutex.withLock {
            Thread.sleep(500)
            _balance += amount
        }
    }

    fun withdraw(amount: Int): Boolean {
        mutex.withLock {
            Thread.sleep(500)
            if (_balance >= amount) {
                _balance -= amount
                return true
            } else {
                return false
            }
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
