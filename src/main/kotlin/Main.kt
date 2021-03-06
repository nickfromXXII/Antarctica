import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class App : Application() {

    @Throws(Exception::class)
    override fun start(stage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("/LoginForm.fxml"))
        val scene = Scene(root)

        stage.scene = scene
        stage.isResizable = false
        stage.title = "Antarctica"

        stage.show()
    }

}

fun main(args: Array<String>) {
    Application.launch(App::class.java)
}
