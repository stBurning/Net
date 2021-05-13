/**17 мая
 * Написать клиент-серверное приложение в котором содержится бд с элементами матрицы
 * БД 2НФ
 * Матрицы большие, 10000 элементов по гориз и верт
 * Выполнение операций между матрицами на клиентах
 * Клиенту отправляется порция данных для вычисления
 * Клиент возвращает серверу резульат
 * Если клиент остается подключенным то след порция отправляется
 * Каждый блок который отправл клиенту верифицировать, сервер отпр хотя бы 2 разным клиентам то с идентичн резульататми все четко
 * Если разная информация то пересчитываем с другими клиентами
 *
 * */
import java.awt.TextArea
import java.awt.TextField
import javax.swing.GroupLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.system.exitProcess

fun main() {
    val client = ClientWindow("Клиент")
    client.isVisible = true
}


class ClientWindow(title: String) : JFrame() {
    init {
        val client = Client("localhost", 5804)
        client.start()

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setTitle(title)
        val textField = TextField()
        val textArea = TextArea()

        val btn = JButton("Send")
        btn.addActionListener {
            textArea.append("Я: ${textField.text}\n")
            client.send(textField.text)
        }
        val mainPanel = JPanel()
        val gl = GroupLayout(mainPanel)
        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addGap(4)
                .addComponent(textArea, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(4)
                .addGroup(
                    gl.createParallelGroup()
                        .addComponent(textField)
                        .addGap(5)
                        .addComponent(btn)
                )
                .addGap(4)
        )
        gl.setHorizontalGroup(
            gl.createParallelGroup()
                .addComponent(textArea)
                .addGap(5)
                .addGroup(
                    gl.createSequentialGroup()
                        .addComponent(textField)
                        .addGap(5)
                        .addComponent(btn)
                )

        )


        mainPanel.add(textArea)
        mainPanel.add(textField)
        mainPanel.add(btn)
        mainPanel.layout = gl
        add(mainPanel)
        pack()

        client.addSessionFinishedListener {
            textArea.append("Работа с сервером завершена. Нажмите Enter для выхода...\n")
            exitProcess(0)
        }
        client.addMessageListener { textArea.append(it+"\n") }
    }
}
