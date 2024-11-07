package com.proferoman

const val GO_SLEEP: Long = 2000

fun main() {
    //namingNotSync()
    //countingNotSync()

    namingWithMutex()
    countingWithMutex()
    countingWithLock()
}
