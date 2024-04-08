package fxgui;

import java.io.IOException;
import java.sql.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.RequestCommand;
import model.User;
import client.networking.ClientRequestProcessor;
import client.service.NeedConnectService;

/**
 * FXML Controller class cho Register GUI
 * 
 * @author Lê Minh Triết
 * @since 31/03/2024
 * @version 1.0
 */
public class RegisterFXMLController {
    //Các thuộc tính FXML    
    @FXML
    private TextField birthdayField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerButton;
    @FXML
    private TextField schoolField;
    @FXML
    private TextField usernameField;   
    @FXML
    private Label backLoginLabel;
    
    /**
     * Xử lý sự kiện ấn vào Register Button
     * @param event 
     */
    @FXML
    void handleRegisterButton(ActionEvent event) {   
        //Thiết lập các thuộc tính cho new user
        User newUser = new User();
        newUser.setName(nameField.getText());
        newUser.setUsername(usernameField.getText());
        newUser.setPassword(passwordField.getText());
        newUser.setBirthday(Date.valueOf(birthdayField.getText()));
        newUser.setSchool(schoolField.getText());
        
        try {
            //Thực hiện tạo Account
            String result = NeedConnectService.createUser(newUser);           
            //Thông báo          
            if(result.equals("Can't create")) {               
                showAlert(Alert.AlertType.ERROR, "Can't create");
            } else if(result.equals("Exist")) {                
                showAlert(Alert.AlertType.ERROR, "Exist account");
            } else {                
                showAlert(Alert.AlertType.INFORMATION, "Successfully create");
                showAlert(Alert.AlertType.CONFIRMATION, "Back to Login");
                //Quay về trang đăng nhập
                openLogin();
            }           
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Xử lý sự kiện khi click vào Login Label
     * @param event
     * @throws IOException 
     */
    @FXML
    void backLoginLabelClicked(MouseEvent event) throws IOException {
        //Quay trở lại trang Login
        openLogin();
    }   
    
    /**
     * Mở trang Login
     * @throws IOException 
     */
    private void openLogin() throws IOException{
        //Ẩn Register GUI
        registerButton.getScene().getWindow().hide();
        //Load Login GUI
        Parent root = FXMLLoader.load(getClass().getResource("LoginFXML.fxml"));
        //Mở Login GUI
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setTitle("NoteLite");
        stage.setScene(scene);
        stage.show();  
    }
    
    /**
     * Show thông báo
     * @param alertType Kiểu thông báo
     * @param text Nội dung thông báo
     */
    private void showAlert(Alert.AlertType alertType, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(String.valueOf(alertType));
        alert.setHeaderText(text);
        alert.showAndWait();
    }
}
