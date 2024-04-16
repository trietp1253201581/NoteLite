package fxgui;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.datatransfer.User;
import client.service.ClientServerService;
import client.service.ClientServerServiceErrorType;

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

    private ClientServerService clientServerService;   
            
    /**
     * Xử lý sự kiện khi ấn vào Login Button
     * @param event 
     */
    @FXML
    void handleLoginButton(ActionEvent event) {  
        //Lấy username và password
        String username = usernameField.getText();
        String password = passwordField.getText();
      
        //Kiểm tra thông tin đăng nhập
        String result = clientServerService.checkLogin(username, password);
        //Thông báo với user
        if(ClientServerServiceErrorType.NOT_EXISTS.toString().equals(result)) {
            showAlert(AlertType.ERROR, "Not exist account");
        } else if(ClientServerServiceErrorType.FALSE_INFORMATION.toString().equals(result)) {
            showAlert(AlertType.ERROR, "False password");
        } else if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(result)) {
            showAlert(AlertType.ERROR, "Can't connect to server");
        } else {
            showAlert(AlertType.INFORMATION, "Successfully Login");
            //Thiết lập user nhận được
            User user = User.valueOf(result);
            //Mở Dashboard của user này
            openDashBoard(user);
        }
    }
    
    /**
     * Xử lý sự kiện khi click chuột vào Register Label
     * @param event
     */
    @FXML
    void registerLabelClicked(MouseEvent event) {       
        openRegister();
    }
    
    public void init() {
        clientServerService = new ClientServerService();
    }
    
    /**
     * Mở Dashboard GUI
     * @param user user được mở Dashboard
     * @throws IOException 
     */
    private void openDashBoard(User user) {
        try {
            //Ẩn Login GUI
            loginButton.getScene().getWindow().hide();
            //Load GUI Dashboard
            FXMLLoader fXMLLoader = new FXMLLoader();
            fXMLLoader.setLocation(getClass().getResource("DashboardFXML.fxml"));
            //Chuyển sang GUI Dashboard
            Stage stage = (Stage)loginButton.getScene().getWindow();
            Scene scene = new Scene(fXMLLoader.load());
            //Thiết lập dữ liệu user cho Dashboard
            DashboardFXMLController dashboardFXMLController = fXMLLoader.getController();
            dashboardFXMLController.setUser(user);
            //Hiển thị Dashboard
            dashboardFXMLController.init();
            
            stage.setTitle("NoteLite");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    /**
     * Mở Register GUI
     * @throws IOException 
     */
    private void openRegister() {
        try {
            //Ẩn Login GUI
            registerLabel.getScene().getWindow().hide();
            //Load Register GUI
            FXMLLoader fXMLLoader = new FXMLLoader();
            fXMLLoader.setLocation(getClass().getResource("RegisterFXML.fxml"));
            //Mở Register GUI
            Stage stage = new Stage();
            Scene scene = new Scene(fXMLLoader.load());
            //Khởi tạo và chạy
            RegisterFXMLController registerFXMLController = fXMLLoader.getController();
            registerFXMLController.init();
            
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
