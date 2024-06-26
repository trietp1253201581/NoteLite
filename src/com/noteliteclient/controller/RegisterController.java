package com.noteliteclient.controller;

import com.noteliteclient.service.ClientServerService;
import com.noteliteclient.service.ClientServerServiceException;
import com.notelitemodel.datatransfer.User;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class cho Register GUI
 * 
 * @author Nhóm 23
 * @since 31/03/2024
 * @version 1.0
 */
public class RegisterController {
    //Các thuộc tính FXML    
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
    private TextField dayOfBirthField;
    @FXML
    private TextField monthOfBirthField;
    @FXML
    private TextField yearOfBirthField;
    @FXML
    private RadioButton genderMale;
    @FXML
    private RadioButton genderFemale;
    @FXML
    private RadioButton genderOther;
    @FXML
    private Label errorNameFieldLabel;
    @FXML
    private Label errorUsernameFieldLabel;
    @FXML
    private Label errorPasswordFieldLabel;
    @FXML
    private Label errorBirthdayFieldLabel;
    @FXML
    private Label backLoginLabel;
    @FXML
    private Button closeButton;
    
    private ClientServerService clientServerService;
    private String host;
    private int port;
    
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
    void handleRegisterButton(ActionEvent event) {
        initScene();
        //Thiết lập các thuộc tính cho new user
        User newUser = new User();
        //Láy thông tin name
        if("".equals(nameField.getText())) {
            errorNameFieldLabel.setVisible(true);
        }
        newUser.setName(nameField.getText());
        //Lấy username
        if("".equals(usernameField.getText())) {
            errorUsernameFieldLabel.setVisible(true);
        }
        newUser.setUsername(usernameField.getText());
        //Lấy password
        if("".equals(passwordField.getText())) {
            errorPasswordFieldLabel.setVisible(true);
        }
        newUser.setPassword(passwordField.getText());
        //Lấy school
        newUser.setSchool(schoolField.getText());
        //Lấy thông tin về birth
        int dayOfBirth = -1;
        int monthOfBirth = -1;
        int yearOfBirth = -1;
        if(dayOfBirthField.getText().matches("^[0-9]{1,2}$")) {
            dayOfBirth = Integer.parseInt(dayOfBirthField.getText());
        } else if("".equals(dayOfBirthField.getText())) {
            dayOfBirth = LocalDate.now().getDayOfMonth();
        } else {
            errorBirthdayFieldLabel.setVisible(true);
        }
        if(monthOfBirthField.getText().matches("^[0-9]{1,2}$")) {
            monthOfBirth = Integer.parseInt(monthOfBirthField.getText());
        } else if("".equals(monthOfBirthField.getText())) {
            monthOfBirth = LocalDate.now().getMonthValue();
        } else {
            errorBirthdayFieldLabel.setVisible(true);
        }
        if(yearOfBirthField.getText().matches("^[0-9]{4}$")) {
            yearOfBirth = Integer.parseInt(yearOfBirthField.getText());
        } else if("".equals(yearOfBirthField.getText())) {
            yearOfBirth = LocalDate.now().getYear();
        } else {
            errorBirthdayFieldLabel.setVisible(true);
        } 
        if(!errorBirthdayFieldLabel.isVisible()) {
            newUser.setBirthday(Date.valueOf(LocalDate.of(yearOfBirth, monthOfBirth, dayOfBirth)));
        }
        //Lấy gender
        if(genderMale.isSelected()) {
            newUser.setGender(User.Gender.MALE);
        } else if (genderFemale.isSelected()) {
            newUser.setGender(User.Gender.FEMALE);
        } else {
            newUser.setGender(User.Gender.OTHER);
        }
        //Kiểm tra xem có lỗi nào không
        if(errorNameFieldLabel.isVisible() || errorUsernameFieldLabel.isVisible()
                || errorPasswordFieldLabel.isVisible() || errorBirthdayFieldLabel.isVisible()) {
            return;
        }
        
        try { 
            //Tạo User mới thành công
            clientServerService.createUser(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Successfully create");
            Optional<ButtonType> optional = showAlert(Alert.AlertType.CONFIRMATION, "Back to Login");
            if(optional.get() == ButtonType.OK) {
                //Quay về trang đăng nhập
                openLogin();
            }          
        } catch (ClientServerServiceException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }

    @FXML
    void backLoginLabelClicked(MouseEvent event) {
        openLogin();
    }   
    
    /**
     * Khởi tạo
     */
    public void initAndGetConnect() {
        //Chạy ClientServerService
        clientServerService = new ClientServerService();
        try {
            clientServerService.createConnectToServer(host, port);
        } catch (ClientServerServiceException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
        initScene();
    }
    
    public void setOnConnect(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    private void initScene() {
        //Ẩn các error label
        errorNameFieldLabel.setVisible(false);
        errorUsernameFieldLabel.setVisible(false);
        errorPasswordFieldLabel.setVisible(false);
        errorBirthdayFieldLabel.setVisible(false);
        //Thiết lập lựa chọn mặc định cho gender
        genderOther.setSelected(true);
    }
    
    private void openLogin() {
        try {
            //Xóa connect
            clientServerService.removeConnectToServer();
            //Ẩn Register GUI
            registerButton.getScene().getWindow().hide();
            //Load Login GUI
            FXMLLoader fXMLLoader = new FXMLLoader();
            String loginFXMLPath = "../view/LoginView.fxml";
            fXMLLoader.setLocation(getClass().getResource(loginFXMLPath));
            //Chuyển sang GUI Login
            Stage stage = new Stage();
            Scene scene = new Scene(fXMLLoader.load());
            LoginController loginFXMLController = fXMLLoader.getController();
            loginFXMLController.setOnConnect(host, port);
            loginFXMLController.initAndGetConnect();
            
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
            showAlert(Alert.AlertType.ERROR, "Can't open login");
        }
    }
    
    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(String.valueOf(alertType));
        alert.setHeaderText(text);
        return alert.showAndWait();
    }
}