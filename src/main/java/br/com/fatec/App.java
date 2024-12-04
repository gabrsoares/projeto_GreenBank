package br.com.fatec;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Alert;


 /* JavaFX App
/**
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Carregar a tela de login como inicial
        scene = new Scene(loadFXML("view/Login"));
        stage.setScene(scene);
        stage.show();

        // Centralizar a janela ao ser exibida pela primeira vez
        centralizarJanela(stage);
    }

    static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);  // Carregar a nova cena
        scene.setRoot(root);  // Definir a nova cena
        
        // Ajustar o tamanho da janela para o conteúdo da nova cena
        Stage stage = (Stage) scene.getWindow();  // Obter a referência do Stage
        root.applyCss();  // Aplica o CSS (se houver) da nova cena
        stage.sizeToScene();  // Ajusta o tamanho do Stage conforme o conteúdo da cena
        
        // Centralizar a janela ao trocar de cena
        centralizarJanela(stage);
    }

    private static void centralizarJanela(Stage stage) {
        // Obter o tamanho da tela (monitor)
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();

        // Obter o tamanho da cena
        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();

        // Calcular a posição para centralizar a janela
        double xPosition = (screenWidth - sceneWidth) / 2;
        double yPosition = (screenHeight - sceneHeight) / 2;

        // Definir a posição da janela
        stage.setX(xPosition);
        stage.setY(yPosition);
    }
    
    public static void alerta(Alert.AlertType tipo, String titulo, String header){
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
