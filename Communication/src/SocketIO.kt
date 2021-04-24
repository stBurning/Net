import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

class SocketIO(private val socket: Socket) {
    private var stop = false
    private var thread: Thread? = null
    private val socketClosedListeners = mutableListOf<() -> Unit>()

    fun addSocketClosedListener(l: () -> Unit){
        socketClosedListeners.add(l)
    }

    fun removeSocketClosedListener(l: () -> Unit){
        socketClosedListeners.remove(l)
    }

    fun stop() {
        stop = true
        socket.close()
    }

    fun startDataReceiving() {
        stop = false
        stopAll = false
        val br = BufferedReader(InputStreamReader(socket.getInputStream()))
        thread = thread {
            try{
                while (!stop && !stopAll) {
                    val data = br.readLine()
                    if (data != null){
                        println(data) //TODO
                    }else{
                        throw IOException("Connection Failed")
                    }
                }
                socket.close()
            }catch (e: Exception){
                socket.close()
                socketClosedListeners.forEach { it() }
                println(e.message)
            }

        }
    }

    fun send(data: String): Boolean{
        return try{
            val pw = PrintWriter(socket.getOutputStream())
            pw.println(data)
            pw.flush() // Очистка буфера
            true
        }catch (e: Exception){
            false
        }
    }

    companion object{
        private var stopAll = false
        fun stopAll(){
            stopAll = true
        }
    }
}