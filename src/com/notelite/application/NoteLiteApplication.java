package com.notelite.application;

import com.notelite.gui.LoginFXMLController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Class chính của project, tạo ứng dụng chạy cho user
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class NoteLiteApplication extends Application {
    private double x,y;
    
    /**
     * @param primaryStage Stage khởi tạo
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        //Mở GUI Login trước
        FXMLLoader fXMLLoader = new FXMLLoader();
        String loginFXMLPath = "../gui/LoginFXML.fxml";
        fXMLLoader.setLocation(getClass().getResource(loginFXMLPath));
        //Chuyển sang GUI Login
        Scene scene = new Scene(fXMLLoader.load());
        LoginFXMLController loginFXMLController = fXMLLoader.getController();
        loginFXMLController.init();
        
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
        
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}