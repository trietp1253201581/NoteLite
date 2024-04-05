package fxgui;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;
import client.service.NeedConnectService;

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

    /**
     * Xử lý sự kiện khi ấn vào Login Button
     * @param event 
     */
    @FXML
    void handleLoginButton(ActionEvent event) {  
        //Lấy username và password
        String username = usernameField.getText();
        String password = passwordField.getText();
      
        try {
            //Kiểm tra thông tin đăng nhập
            String result = NeedConnectService.checkLogin(username, password);
            //Thông báo với user
            if(result.equals("Not found")) {
                showAlert(AlertType.ERROR, "Not exist account");
            } else if(result.equals("False")) {
                showAlert(AlertType.ERROR, "False password");
            } else {
                showAlert(AlertType.INFORMATION, "Successfully Login");
                //Thiết lập user nhận được
                User user = User.valueOf(result);
                //Mở Dashboard của user này
                openDashBoard(user);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Xử lý sự kiện khi click chuột vào Register Label
     * @param event
     * @throws IOException 
     */
    @FXML
    void registerLabelClicked(MouseEvent event) throws IOException {       
        openRegister();
    }
    
    /**
     * Mở Dashboard GUI
     * @param user user được mở Dashboard
     * @throws IOException 
     */
    private void openDashBoard(User user) throws IOException {
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
        dashboardFXMLController.run();
        
        stage.setTitle("NoteLite");
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Mở Register GUI
     * @throws IOException 
     */
    private void openRegister() throws IOException {
        //Ẩn Login GUI
        registerLabel.getScene().getWindow().hide();
        //Load Register GUI
        Parent root = FXMLLoader.load(getClass().getResource("RegisterFXML.fxml"));
        //Mở Register GUI
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
