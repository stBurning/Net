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
import java.awt.FlowLayout
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
            textArea.append("Я: ${textField.text}")
            client.send(textField.text)
        }
        val mainPanel = JPanel()
        val layout = GroupLayout(mainPanel)
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGap(10)
            .addComponent(textArea,GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGap(10)
            .addGroup(layout.createParallelGroup()
                .addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(btn,  GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            )
        )
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGap(20)

            .addGroup(  layout.createParallelGroup()
                .addComponent(textArea)
                .addComponent(textField)
                .addComponent(btn))
            .addGap(20))
        mainPanel.add(textArea)
        mainPanel.add(textField)
        mainPanel.add(btn)
        mainPanel.layout = layout
        add(mainPanel)
        pack()

        client.addSessionFinishedListener {
            textArea.append("Работа с сервером завершена. Нажмите Enter для выхода...")
            exitProcess(0)
        }
        client.addMessageListener { textArea.append(it) }
    }
}
