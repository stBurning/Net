import java.net.Socket

class Client(private val host: String, private val port: Int) {
    private val socket: Socket = Socket(host, port)
    private val communicator = SocketIO(socket)
    fun stop(){
        communicator.stop()
    }

    fun start(){
        communicator.startDataReceiving()
        communicator.send("Hello")
    }

    fun send(data: String) {
        communicator.send(data)
    }
}