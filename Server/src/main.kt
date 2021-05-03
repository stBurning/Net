import java.awt.TextField
import java.util.*
import javax.swing.JButton
import javax.swing.JFrame

fun main(){
    val s = Server()
    s.addMessageListener { data -> println(data) }
    s.start()
    var cmd: String
    val sc = Scanner(System.`in`)
    do {
        cmd = sc.nextLine()
    } while (cmd != "STOP")
    s.stop()
}

