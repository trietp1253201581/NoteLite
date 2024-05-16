package com.notelite;

import com.notelite.controller.LoginController;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Class chính của project, tạo ứng dụng chạy cho user
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class NoteLite extends Application {
    private double x,y;
    
    /**
     * @param primaryStage Stage khởi tạo
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            //Mở GUI Login trước
            FXMLLoader fXMLLoader = new FXMLLoader();
            String loginFXMLPath = "view/LoginView.fxml";
            fXMLLoader.setLocation(getClass().getResource(loginFXMLPath));
            //Chuyển sang GUI Login
            Scene scene = new Scene(fXMLLoader.load());
            LoginController loginFXMLController = fXMLLoader.getController();
            loginFXMLController.init();
            //Thiết lập các hiệu ứng di chuyển
            x = 0;
            y = 0;
            scene.setOnMousePressed((MouseEvent mouseEvent) -> {
                x = mouseEvent.getSceneX();
                y = mouseEvent.getSceneY();
            });
            scene.setOnMouseDragged((MouseEvent mouseEvent) -> {
                primaryStage.setX(mouseEvent.getScreenX() - x);
                primaryStage.setY(mouseEvent.getScreenY() - y);
            });
            //Set scene cho stage và show
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't open application");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(String.valueOf(alertType));
        alert.setHeaderText(text);
        return alert.showAndWait();
    }
}