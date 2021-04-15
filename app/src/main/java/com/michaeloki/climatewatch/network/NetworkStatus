package com.michaeloki.climatewatch.network

import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.*

class NetworkStatus {

     fun internetConnectionAvailable(timeOut: Long): Boolean {
        var inetAddress: InetAddress? = null
        try {
            val future: Future<InetAddress?>? =
                Executors.newSingleThreadExecutor().submit(Callable<InetAddress?> {
                    try {
                        InetAddress.getByName("google.com")
                    } catch (e: UnknownHostException) {
                        null
                    }
                })
            if (future != null) {
                inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS)
            }
            future?.cancel(true)
        } catch (e: InterruptedException) {
        } catch (e: ExecutionException) {
        } catch (e: TimeoutException) {
        }
        return inetAddress != null && !inetAddress.equals("")
    }
}