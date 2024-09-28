package com.proferoman

import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

class RickAndMorty {
    companion object {
        val className = "com.proferoman.RickAndMorty"
        val endpoint = "https://rickandmortyapi.com/api/character"

        @JvmStatic
        fun main(args: Array<String>) {
            println("Comienza el proceso ${ProcessHandle.current().pid()}: ${endpoint}")

            val uri = URI.create(endpoint)
            val url = URL.of(uri, null)
            println(fetch(url))
        }
    }
}

class IpInfo {
    companion object {
        val className = "com.proferoman.IpInfo"
        val endpoint = "http://ip-api.com/json/"

        @JvmStatic
        fun main(args: Array<String>) {
            println("Comienza el proceso ${ProcessHandle.current().pid()}: ${endpoint}")

            if (args.size < 1) {
                println("Arguments error: expected an IP")
                return
            }

            val uri = URI.create("${IpInfo.endpoint}${args[0]}")
            val url = URL.of(uri, null)
            println(fetch(url))
        }
    }
}

fun fetch(url: URL): String {
    val connection = url.openConnection() as HttpURLConnection

    try {
        connection.requestMethod = "GET"
        connection.connect()

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val stream = connection.inputStream
            return stream.bufferedReader().use { it.readText() }
        } else {
            return "HTTP error code: $responseCode"
        }
    } finally {
        connection.disconnect()
    }
}

fun launchRickAndMorty() {
    val classPath = System.getProperty("java.class.path")
    val pb = ProcessBuilder("kotlin", "-cp", classPath, RickAndMorty.className)
    pb.inheritIO().start()
}

fun launchIpInfo(ip: String) {
    val classPath = System.getProperty("java.class.path")
    val pb = ProcessBuilder("kotlin", "-cp", classPath, IpInfo.className, ip)
    pb.inheritIO().start()
}

fun main() {
    // https://github.com/public-apis/public-apis
    launchRickAndMorty()
    launchIpInfo("195.77.20.100")
}
