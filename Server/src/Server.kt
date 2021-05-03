import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class Server(port: Int = 5804) {
    private val sSocket: ServerSocket = ServerSocket(port)
    private val clients = mutableListOf<Client>()
    private var stop = false

    private val messageListeners = mutableListOf<(String)-> Unit>()
    fun addMessageListener(l:(String)-> Unit){
        messageListeners.add(l)
    }
    fun removeMessageListener(l:(String)-> Unit){
        messageListeners.remove(l)
    }


    inner class Client(private val socket: Socket){
        private var sio: SocketIO? = null
        private val id : Int = clients.size + 1
        fun startDialog(){
            sio = SocketIO(socket).apply{
                addSocketClosedListener {
                    clients.remove(this@Client)
                }
                addMessageListener { data ->
                    messageListeners.forEach { l -> l("[$id] $data") }
                    clients.forEach { client ->
                        if (client != this@Client) client.send("[$id] $data")
                    }
                }
                startDataReceiving()
            }
        }

        fun stop(){
            sio?.stop()
        }

        fun send(data: String){
            sio?.send(data)
        }
    }

    fun send(i: Int, data: String){
        clients[i].send(data)
    }



    fun stop(){
        sSocket.close()
        stop = true
    }

    fun start() {
        messageListeners.forEach { l -> l("[SERVER] Сервер запущен.") }
        stop = false
        thread {
            try {
                while (!stop) {
                    clients.add(
                        Client(
                            sSocket.accept()
                        ).also { client -> client.startDialog() })
                }
            } catch (e: Exception){
                messageListeners.forEach { l->
                    e.message?.let { l(it) }
                }
            } finally {
                stopAllClients()
                sSocket.close()
                messageListeners.forEach { l ->
                    l("[SERVER] Сервер остановлен.")
                }
            }
        }
    }

    private fun stopAllClients(){
        clients.forEach { client -> client.stop() }
    }
}