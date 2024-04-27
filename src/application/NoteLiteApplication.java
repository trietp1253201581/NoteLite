package application;

import fxgui.LoginFXMLController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Class chính của project, tạo ứng dụng chạy cho user
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class NoteLiteApplication extends Application {
    /**
     * @param primaryStage Stage khởi tạo
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        //Mở GUI Login trước
        FXMLLoader fXMLLoader = new FXMLLoader();
        String loginFXMLPath = "../fxgui/LoginFXML.fxml";
        fXMLLoader.setLocation(getClass().getResource(loginFXMLPath));
        //Chuyển sang GUI Login
        Scene scene = new Scene(fXMLLoader.load());
        LoginFXMLController loginFXMLController = fXMLLoader.getController();
        loginFXMLController.init();
        
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
