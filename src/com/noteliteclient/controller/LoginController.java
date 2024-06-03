package com.noteliteclient.controller;

import com.noteliteclient.service.ClientServerService;
import com.noteliteclient.service.ClientServerServiceException;
import com.notelitemodel.datatransfer.User;
import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class cho Login GUI
 * 
 * @author Nhóm 23
 * @since 31/03/2024
 * @version 1.0
 */
public class LoginController {   
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

    private ClientServerService clientServerService;   
    private double x,y;
            
    @FXML
    void handleCloseButton(ActionEvent event) {
        Optional<ButtonType> optional = showAlert(Alert.AlertType.CONFIRMATION,
                "Close NoteLite?");
        if(optional.get() == ButtonType.OK) {
            clientServerService.removeConnectToServer();
            System.exit(0);
        }
    }
    
    @FXML
    void handleLoginButton(ActionEvent event) {  
        //Lấy username và password
        String username = usernameField.getText();
        String password = passwordField.getText();
      
        //Kiểm tra thông tin đăng nhập
        try { 
            //Trường hợp đăng nhập thành công
            //Thiết lập user nhận được
            User user = clientServerService.checkLogin(username, password);
            showAlert(Alert.AlertType.INFORMATION, "Successfully Login");
            //Mở Dashboard của user này
            openDashBoard(user);
        } catch (ClientServerServiceException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    @FXML
    void registerLabelClicked(MouseEvent event) {    
        openRegister();
    }
    
    /**
     * Khởi tạo Login Controller
     */
    public void initAndGetConnect() {
        clientServerService = new ClientServerService();
        try {
            clientServerService.createConnectToServer();
        } catch (ClientServerServiceException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }

    private void openDashBoard(User user) {
        try {
            //Xóa connect
            clientServerService.removeConnectToServer();
            //Ẩn Login GUI
            loginButton.getScene().getWindow().hide();
            //Load GUI Dashboard
            FXMLLoader fXMLLoader = new FXMLLoader();
            String dashboardFXMLPath = "../view/DashboardView.fxml";
            fXMLLoader.setLocation(getClass().getResource(dashboardFXMLPath));
            //Chuyển sang GUI Dashboard
            Stage stage = new Stage();
            Scene scene = new Scene(fXMLLoader.load());
            //Thiết lập dữ liệu user cho Dashboard
            DashboardController dashboardFXMLController = fXMLLoader.getController();
            dashboardFXMLController.setMyUser(user);
            //Hiển thị Dashboard
            dashboardFXMLController.initAndGetConnect();
            
            x = 0;
            y = 0;
            scene.setOnMousePressed((MouseEvent mouseEvent) -> {
                x = mouseEvent.getSceneX();
                y = mouseEvent.getSceneY();
            });       
            scene.setOnMouseDragged((MouseEvent mouseEvent) -> {
                stage.setX(mouseEvent.getScreenX() - x);
                stage.setY(mouseEvent.getScreenY() - y);
            });
            
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't open dashboard");
        }
    }

    private void openRegister() {
        try {
            //Xóa connect
            clientServerService.removeConnectToServer();
            //Ẩn Login GUI
            registerLabel.getScene().getWindow().hide();
            //Load Register GUI
            FXMLLoader fXMLLoader = new FXMLLoader();
            String registerFXMLPath = "../view/RegisterView.fxml";
            fXMLLoader.setLocation(getClass().getResource(registerFXMLPath));
            //Mở Register GUI
            Stage stage = new Stage();
            Scene scene = new Scene(fXMLLoader.load());
            //Khởi tạo và chạy
            RegisterController registerFXMLController = fXMLLoader.getController();
            registerFXMLController.initAndGetConnect();
            
            x = 0;
            y = 0;
            scene.setOnMousePressed((MouseEvent mouseEvent) -> {
                x = mouseEvent.getSceneX();
                y = mouseEvent.getSceneY();
            });       
            scene.setOnMouseDragged((MouseEvent mouseEvent) -> {
                stage.setX(mouseEvent.getScreenX() - x);
                stage.setY(mouseEvent.getScreenY() - y);
            });
            
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);  
            stage.show();
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't open register");
        }
    }
    
    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(String.valueOf(alertType));
        alert.setHeaderText(text);
        return alert.showAndWait();
    }
}