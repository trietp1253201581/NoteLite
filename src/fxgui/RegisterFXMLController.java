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
import model.datatransfer.User;
import client.service.ClientServerService;
import client.service.ClientServerServiceErrorType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

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
    private DatePicker birthdayField;
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
    
    private ClientServerService clientServerService;
    
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
        newUser.setBirthday(Date.valueOf(birthdayField.getValue()));
        newUser.setSchool(schoolField.getText());
        
        //Thực hiện tạo Account
        String result = clientServerService.createUser(newUser);
        //Thông báo
        if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(result)) {
            showAlert(Alert.AlertType.ERROR, "Can't create user");
        } else if(ClientServerServiceErrorType.ALREADY_EXISTS.toString().equals(result)) {
            showAlert(Alert.AlertType.ERROR, "Exist user");
        } else if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(result)) {
            showAlert(Alert.AlertType.ERROR, "Can't connect to server");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Successfully create");
            showAlert(Alert.AlertType.CONFIRMATION, "Back to Login");
            //Quay về trang đăng nhập
            openLogin();
        }
    }
    
    /**
     * Xử lý sự kiện khi click vào Login Label
     * @param event
     */
    @FXML
    void backLoginLabelClicked(MouseEvent event) {
        //Quay trở lại trang Login
        openLogin();
    }   
    
    /**
     * Khởi tạo
     */
    public void init() {
        //Chạy ClientServerService
        clientServerService = new ClientServerService();
        //Khởi tạo birthdayField
        birthdayField.setValue(LocalDate.now());
        //Tạo converter từ ngày tháng sang yyyy-MM-dd
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            @Override
            public String toString(LocalDate date) {
                if(date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if(string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return LocalDate.now();
                }
            }
        };
        
        birthdayField.setConverter(converter);
        birthdayField.setPromptText("yyyy-MM-dd");
        birthdayField.setEditable(false);
    }
    
    /**
     * Mở trang Login
     */
    private void openLogin() {
        try {
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
        } catch (IOException ex) {
            System.err.println(ex);
        }
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
