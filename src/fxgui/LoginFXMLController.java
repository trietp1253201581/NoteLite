package fxgui;

import client.service.ClientServerService;
import client.service.ClientServerServiceErrorException;
import client.service.ClientServerServiceErrorType;
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
import model.datatransfer.User;

/**
 * FXML Controller class cho Login GUI
 * 
 * @author Lê Minh Triết
 * @since 31/03/2024
 * @version 1.0
 */
public class LoginFXMLController {   
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
            
    @FXML
    void handleCloseButton(ActionEvent event) {
        Optional<ButtonType> optional = showAlert(Alert.AlertType.CONFIRMATION,
                "Close NoteLite?");
        if(optional.get() == ButtonType.OK) {
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
        } catch (ClientServerServiceErrorException ex) {
            //Xử lý các ngoại lệ
            switch (ex.getErrorType()) {
                case ClientServerServiceErrorType.NOT_EXISTS -> {
                    showAlert(Alert.AlertType.ERROR, "Not exist user");
                }
                case ClientServerServiceErrorType.FALSE_INFORMATION -> {
                    showAlert(Alert.AlertType.ERROR, "False password");
                }
                case ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER -> {
                    showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                }
                case ClientServerServiceErrorType.UNSUPPORTED_SERVICE -> {
                    showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                }
            }
        }
    }
    
    @FXML
    void registerLabelClicked(MouseEvent event) {       
        openRegister();
    }
    
    /**
     * Khởi tạo Login Controller
     */
    public void init() {
        clientServerService = new ClientServerService();
    }

    private void openDashBoard(User user) {
        try {
            //Ẩn Login GUI
            loginButton.getScene().getWindow().hide();
            //Load GUI Dashboard
            FXMLLoader fXMLLoader = new FXMLLoader();
            String dashboardFXMLPath = "DashboardFXML.fxml";
            fXMLLoader.setLocation(getClass().getResource(dashboardFXMLPath));
            //Chuyển sang GUI Dashboard
            Stage stage = new Stage();
            Scene scene = new Scene(fXMLLoader.load());
            //Thiết lập dữ liệu user cho Dashboard
            DashboardFXMLController dashboardFXMLController = fXMLLoader.getController();
            dashboardFXMLController.setUser(user);
            //Hiển thị Dashboard
            dashboardFXMLController.init();
            
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't open dashboard");
        }
    }

    private void openRegister() {
        try {
            //Ẩn Login GUI
            registerLabel.getScene().getWindow().hide();
            //Load Register GUI
            FXMLLoader fXMLLoader = new FXMLLoader();
            String registerFXMLPath = "RegisterFXML.fxml";
            fXMLLoader.setLocation(getClass().getResource(registerFXMLPath));
            //Mở Register GUI
            Stage stage = new Stage();
            Scene scene = new Scene(fXMLLoader.load());
            //Khởi tạo và chạy
            RegisterFXMLController registerFXMLController = fXMLLoader.getController();
            registerFXMLController.init();
            
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
