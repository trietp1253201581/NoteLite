package com.noteapp.controller;

import com.noteapp.dataaccess.DataAccessException;
import com.noteapp.model.datatransfer.Note;
import com.noteapp.model.datatransfer.User;
import com.noteapp.service.CheckLoginService;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class cho Login GUI
 * 
 * @author Nhóm 23
 * @since 31/03/2024
 * @version 1.0
 */
public class LoginController extends Controller {   
    //Các thuộc tính FXML
    @FXML
    private Button loginButton;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;    
    @FXML 
    private Label registerLabel;
    @FXML
    private Button closeButton;
        
    @Override
    public void init() {
        loginButton.setOnAction((ActionEvent event) -> {
            login();
        });
        closeButton.setOnAction((ActionEvent event) -> {
            close();
        });
        registerLabel.setOnMouseClicked((MouseEvent event) -> {
            openRegister();
        });
    }

    protected void login() {  
        //Lấy username và password
        String username = usernameField.getText();
        String password = passwordField.getText();
      
        //Kiểm tra thông tin đăng nhập
        try { 
            //Trường hợp đăng nhập thành công
            userService = new CheckLoginService(username, password);
            //Thiết lập user nhận được
            User user = userService.execute();
            showAlert(Alert.AlertType.INFORMATION, "Successfully Login");
            //Mở Dashboard của user này
            openEditNoteView(user);
        } catch (DataAccessException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }

    protected void openEditNoteView(User user) {
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            String registerViewPath = "../view/EditNoteView.fxml";
            fXMLLoader.setLocation(getClass().getResource(registerViewPath));

            scene = new Scene(fXMLLoader.load());
            
            EditNoteViewController controller = fXMLLoader.getController();
            controller.setStage(stage);
            controller.setMyUser(user);
            controller.setMyNote(new Note());
            controller.init();
            controller.setOnAutoUpdate();
            
            setSceneMoveable();
            
            stage.setScene(scene);  
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't open edit view");
        }
    }
    
    protected void openRegister() {
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            String registerViewPath = "../view/RegisterView.fxml";
            fXMLLoader.setLocation(getClass().getResource(registerViewPath));

            scene = new Scene(fXMLLoader.load());
            
            RegisterController registerController = fXMLLoader.getController();
            registerController.setStage(stage);
            registerController.init();
            
            setSceneMoveable();
            
            stage.setScene(scene);  
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't open register");
        }
    }
}