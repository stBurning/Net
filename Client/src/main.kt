import java.util.*

fun main(){
    val client = Client("localhost", 5804)
    client.start()
    val s = Scanner(System.`in`)
    var data: String
    do {
        data = s.nextLine()
        client.send(data)
    }while (data != "STOP")
    client.stop()
}