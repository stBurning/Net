import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class Server(private val port: Int = 5804) {

    private val sSocket: ServerSocket = ServerSocket(port)
    private val clients = mutableListOf<Client>()
    private var stop = false

    fun stop() {
        stop = true
    }

    inner class Client(private val socket: Socket) {
        fun startDialog() {
            thread {
                SocketIO(socket).apply {
                    addSocketClosedListener {
                        clients.remove(this@Client)
                    }
                    startDataReceiving()
                }
            }
        }

    }

    fun start() {
        stop = false
        thread {
            while (!stop) {
                clients.add(Client(sSocket.accept()).also {
                        client -> client.startDialog() })

            }

        }
    }
}