package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class chính của project, tạo ứng dụng chạy cho user
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class NoteLiteApplication extends Application {
    /**
     * @param primaryStage Stage khởi tạo
     * @throws java.io.IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        //Mở GUI Login trước
        Parent root = FXMLLoader.load(getClass().getResource("../fxgui/LoginFXML.fxml"));
        Scene scene = new Scene(root);
       
        primaryStage.setTitle("NoteLite");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
