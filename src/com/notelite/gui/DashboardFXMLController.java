package com.notelite.gui;

import com.notelite.service.ClientServerService;
import com.notelite.service.ClientServerServiceException;
import com.notelite.service.PdfService;
import com.notelite.service.UndoRedoService;
import com.notelitemodel.datatransfer.Note;
import com.notelitemodel.datatransfer.ShareNote;
import com.notelitemodel.datatransfer.User;
import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class cho Dashboard GUI
 * 
 * @author Lê Minh Triết
 * @since 07/04/2024
 * @version 1.0
 */
public class DashboardFXMLController {
    //Các thuộc tính FXML của form dashboard chung
    @FXML 
    private BorderPane extraServiceScene;
    @FXML
    private VBox mainScene;
    //Các thuộc tính của main scene
    //Các thuộc tính chung
    @FXML 
    private Label noteHeaderLabel;
    @FXML
    private Button homeMenuButton;
    @FXML
    private Button editMenuButton;
    @FXML
    private HBox editBox;
    //Các thuộc tính của edit Box
    @FXML
    private Button saveNoteButton;
    @FXML
    private Button openNoteButton;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;
    @FXML 
    private Button addFilterButton;
    @FXML
    private ComboBox<String> fontTypeComboBox; 
    @FXML
    private ComboBox<String> fontSizeComboBox;
    @FXML
    private ColorPicker ColorPicker;
    //Các thuộc tính còn lại
    @FXML
    private TextArea contentArea;
    @FXML
    private Label numCharLabel;
    @FXML
    private GridPane filterGridLayout;
    //Các thuộc tính của extra scene
    //Các thuộc tính chung
    @FXML
    private Label userLabel;   
    @FXML
    private Button extraCloseButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button backMainSceneButton;
    @FXML
    private Button myNotesButton;
    @FXML
    private Button myAccountButton;
    @FXML
    private Button importExportButton;
    @FXML
    private Button shareNoteButton;
    //Các thuộc tính của myNotesScene
    @FXML
    private AnchorPane myNotesScene;
    @FXML
    private TextField searchNoteField;  
    @FXML
    private VBox noteCardLayout;   
    @FXML
    private Button createNoteButton;   
    @FXML
    private Button deleteNoteButton; 
    //Các thuộc tính của myAccountScene
    @FXML
    private AnchorPane myAccountScene;    
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label userIdLabel;
    @FXML
    private TextField nameField;
    @FXML 
    private TextField dayOfBirthField;
    @FXML
    private TextField monthOfBirthField;
    @FXML
    private TextField yearOfBirthField;
    @FXML
    private TextField schoolField;
    @FXML
    private RadioButton genderMale;
    @FXML
    private RadioButton genderFemale;
    @FXML
    private RadioButton genderOther;
    @FXML
    private Label errorNameFieldLabel;
    @FXML
    private Label errorPasswordFieldLabel;
    @FXML
    private Label errorBirthdayFieldLabel;
    @FXML
    private Button changePasswordButton;
    @FXML
    private Button saveAccountButton;  
    //Các thuộc tính FXML của importExportScene
    @FXML
    private AnchorPane importExportScene;
    @FXML
    private Button exportFileButton;
    @FXML
    private ComboBox<String> exportNoteComboBox;
    @FXML
    private ComboBox<String> exportFormatComboBox;
    @FXML
    private Label importNoteName;
    @FXML
    private Button chooseInputFileButton;
    @FXML
    private Button importFileButton;
    @FXML
    private Label importFileName;
    //Các thuộc tính FXML của shareNoteScene
    @FXML
    private AnchorPane shareNoteScene;
    @FXML
    private ComboBox<String> chooseShareNoteComboBox;
    @FXML
    private TextField chooseUserShareField;
    @FXML
    private RadioButton shareTypeReadOnly;
    @FXML
    private RadioButton shareTypeCanEdit;
    @FXML
    private Button sendNoteButton;
    @FXML
    private VBox shareNoteCardLayout;
    
    private UndoRedoService undoRedoService;
    private ClientServerService clientServerService;
    private User myUser;   
    private Note myNote;    
    private List<Note> myNotes;   
    private List<ShareNote> mySharedNotes;
    private boolean savedNoteStatus;

    private double x,y;
    
    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }

    @FXML
    void handleCloseButton(ActionEvent event) {
        //Kiểm tra Note hiện hành đã được lưu chưa?
        autoSave();
        Optional<ButtonType> optional = showAlert(Alert.AlertType.CONFIRMATION,
                "Close NoteLite?");
        if(optional.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
    
    @FXML
    void handleNoteHeaderLabel(MouseEvent event) {
        if(event.getSource() != noteHeaderLabel) {
            return;
        }
        //Mở dialog
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Change header for " + myNote.getHeader());
        inputDialog.setHeaderText("Input your new header");
        //Lấy kết quả và xử lý
        Optional<String> confirm = inputDialog.showAndWait();
        confirm.ifPresent(newNoteHeader -> {
            //Lấy tất cả các Note của myUser
            try { 
                //Lấy thành công
                myNotes = clientServerService.getAllNotes(myUser.getUsername());
                for(Note note: myNotes) {
                    if(note.getHeader().equals(newNoteHeader)) {
                        showAlert(Alert.AlertType.ERROR, "This header exist");
                        return;
                    }
                }
                //Thiết lập note name vừa nhập cho Label   
                noteHeaderLabel.setText(newNoteHeader);
            } catch (ClientServerServiceException ex) {
                //Xử lý các ngoại lệ
                switch (ex.getErrorType()) {
                    case ClientServerService.ErrorType.NOT_EXISTS -> {
                        myNotes = new ArrayList<>();
                    }
                    case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                        showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                    }
                    case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                        showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                    }
                }
            }
        });
    }
    
    @FXML
    void handleEditMenuButton(ActionEvent event) {  
        
    }
    
    @FXML
    void handleSaveNoteButton(ActionEvent event) {
        //Thiết lập lại undoRedoService
        undoRedoService = new UndoRedoService();    
        undoRedoService.saveText(contentArea.getText());
        //Set dữ liệu gần nhất cho myNote
        myNote.setHeader(noteHeaderLabel.getText());
        myNote.setContent(Note.ContentConverter.convertToDBText(contentArea.getText()));
        myNote.setLastModified(1);
        myNote.setLastModifiedDate(Date.valueOf(LocalDate.now()));
        //Lưu note
        try { 
            //Lưu thành công
            myNote = clientServerService.saveNote(myNote);
            showAlert(Alert.AlertType.INFORMATION, "Successfully save for " + myNote.getHeader());
            //Chỉnh trạng thái lưu Note 
            savedNoteStatus = true;     
        } catch (ClientServerServiceException ex) {
            //Xử lý các ngoại lệ
            switch (ex.getErrorType()) {
                case ClientServerService.ErrorType.CAN_NOT_EXECUTE -> {
                    showAlert(Alert.AlertType.ERROR, "Can't save this note");
                }
                case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                    showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                }
                case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                    showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                }
            }
        }
    }
    
    @FXML
    void handleUndoButton(ActionEvent event) {      
        //Lấy text thu được khi undo
        String text = undoRedoService.undo();
        //Nhận và thông báo
        if("Can't undo".equals(text)) {
            //Không có text để undo
            showAlert(Alert.AlertType.ERROR, text);
        } else {
            //Thiết lập content là text vừa undo
            contentArea.setText(text);
            numCharLabel.setText(String.valueOf(contentArea.getText().length()) + "/10000");
        }
        //Chỉnh trạng thái lưu Note 
        savedNoteStatus = false;        
    }

    @FXML
    void handleRedoButton(ActionEvent event) {    
        //Lấy text thu được khi undo
        String text = undoRedoService.redo();
        //Nhận và thông báo
        if(text.equals("Can't redo")) {
            //Không có text để redo
            showAlert(Alert.AlertType.ERROR, text);
        } else {
            //Thiết lập content là text vừa redo
            contentArea.setText(text);       
            numCharLabel.setText(String.valueOf(contentArea.getText().length()) + "/10000");
        }
        //Chỉnh trạng thái lưu Note 
        savedNoteStatus = false;        
    }

    @FXML
    void handleAddFilterButton(ActionEvent event) {        
        //Hiện Dialog để nhập filter mới
        TextInputDialog inputDialog = new TextInputDialog();        
        inputDialog.setTitle("Add filter for " + myNote.getHeader());
        inputDialog.setHeaderText("Enter your new filter");   
        //Lấy kết quả
        Optional<String> confirm = inputDialog.showAndWait();
        //Xử lý kết quả khi nhấn OK
        confirm.ifPresent(newFilter -> {
            //Thiết lập và add tất cả các filter cũ     
            if(myNote.getFilters().contains(newFilter)) {
                //Nếu filter đã tồn tại thì thông báo lỗi
                showAlert(Alert.AlertType.ERROR, "Exist Filter");
            } else {
                //Thêm filter vào list
                myNote.getFilters().add(newFilter);
                //Load lại filter GUI
                loadFilter(myNote.getFilters(), 8);
            }     
            //Chỉnh trạng thái lưu Note 
            savedNoteStatus = false;
        }); 
    }
    
    @FXML
    void handleFontTypeComboBox(ActionEvent event) {
        String fontType = fontTypeComboBox.getSelectionModel().getSelectedItem();
        double fontSize = contentArea.getFont().getSize();
        contentArea.setFont(Font.font(fontType, fontSize));
    }
    
    @FXML
    void handleFontSizeComboBox(ActionEvent event) {
        double fontSize = Double.parseDouble(fontSizeComboBox.getSelectionModel().getSelectedItem());
        String fontType = contentArea.getFont().getFamily();
        contentArea.setFont(Font.font(fontType, fontSize));
    }
    
    @FXML
    void changeTextArea(KeyEvent event) {  
        if(event.getSource() != contentArea) {
            return;
        }
        //Lấy các thông số
        int nowLength = contentArea.getText().length();
        int lastLength = undoRedoService.getLastText().length();
        //Kiểm tra đã vượt quá số ký tự quy định
        numCharLabel.setText(String.valueOf(nowLength) + "/10000");
        //Tự động lưu văn bản vào undoRedoService
        if(nowLength < lastLength || nowLength > lastLength + 3) {
            undoRedoService.saveText(contentArea.getText());
        }
        //Chỉnh trạng thái lưu Note 
        savedNoteStatus = false;
    }
    
    @FXML
    void handleHomeMenuButton(ActionEvent event) {
        initAndChangeToExtraServiceScene();
    }
    
    @FXML
    void handleBackMainSceneButton(ActionEvent event) {
        initAndChangeToMainScene();
    }
    
    @FXML
    void handleLogoutButton(ActionEvent event) { 
        //Kiểm tra và lưu
        autoSave();
        //Set lại style của button
        logoutButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #0e544e, #0e2f52)");
        //Ẩn Dashboard GUI
        logoutButton.getScene().getWindow().hide();
        //Load Login GUI
        FXMLLoader fXMLLoader = new FXMLLoader();
        String loginFXMLPath = "LoginFXML.fxml";
        fXMLLoader.setLocation(getClass().getResource(loginFXMLPath));
        //Mở Login GUI
        Stage stage = new Stage();
        try {
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

    @FXML
    void handleMyNotesButton(ActionEvent event) {        
        //Chuyển sang scene My Notes
        changeSceneInExtraScene(myNotesButton);
        //Lấy tất cả các Note của myUser
        try { 
            //Lấy thành công
            myNotes = clientServerService.getAllNotes(myUser.getUsername());
        } catch (ClientServerServiceException ex) {
            //Xử lý các ngoại lệ
            switch (ex.getErrorType()) {
                case ClientServerService.ErrorType.NOT_EXISTS -> {
                    myNotes = new ArrayList<>();
                }
                case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                    showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                }
                case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                    showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                }
            }
        }
        //Init lại Scene
        initMyNotesScene(myNotes);
    }
    
    @FXML
    void handleSearchNoteField(ActionEvent event) {
        //Lấy thông tin cần search
        String searchText = searchNoteField.getText();
        //Tạo list mới để chứa các note hợp lệ
        List<Note> notes = new ArrayList<>();
        //Thêm các note hợp lệ vào list
        for(Note newNote: myNotes) {
            if(newNote.getHeader().contains(searchText) 
                    || Note.FiltersConverter.convertToString(newNote.getFilters()).contains(searchText)) { 
                notes.add(newNote);
            }     
        }
        //Load lại My Notes Scene
        initMyNotesScene(notes);
    }
    
    @FXML
    void handleCreateNoteButton(ActionEvent event) {
        //Hiện dialog để nhập header cho Note mới
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create new note");
        dialog.setHeaderText("Enter header for your new note");
        //Lấy kết quả
        Optional<String> confirm = dialog.showAndWait();
        //Xử lý kết quả khi nhấn OK
        confirm.ifPresent(selectedHeader -> {
            //Set dữ liệu cho note mới
            Note newNote = new Note();
            newNote.setAuthor(myUser.getUsername());
            newNote.setHeader(selectedHeader);
            newNote.setContent("Edit here");
            newNote.setLastModifiedDate(Date.valueOf(LocalDate.now()));
            List<String> filters = new ArrayList<>();
            newNote.setFilters(filters);
            //Tạo Note mới
            try { 
                //Tạo thành công
                newNote = clientServerService.createNote(newNote);
                showAlert(Alert.AlertType.INFORMATION, "Successfully create " + newNote.getHeader());
                //Thêm vào list và load lại
                myNotes.add(newNote);
                initMyNotesScene(myNotes);
            } catch (ClientServerServiceException ex) {
                //Xử lý các ngoại lệ
                switch (ex.getErrorType()) {
                    case ClientServerService.ErrorType.ALREADY_EXISTS -> {
                        showAlert(Alert.AlertType.ERROR, "This note already exists");
                    }
                    case ClientServerService.ErrorType.CAN_NOT_EXECUTE -> {
                        showAlert(Alert.AlertType.ERROR, "Can't create new note");
                    }
                    case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                        showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                    }
                    case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                        showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                    }
                }
            }
        });
    }
    
    @FXML
    void handleDeleteNoteButton(ActionEvent event) {
        //Lấy list các header note
        List<String> myNotesHeader = new ArrayList<>();
        for(Note note: myNotes){
            myNotesHeader.add(note.getHeader());
        }
        //Hiện dialog để chọn note cần xóa
        ChoiceDialog<String> dialog = new ChoiceDialog<>(myNotesHeader.get(0), myNotesHeader);
        dialog.setTitle("Delete Note");
        dialog.setHeaderText("Choose note to delete");
        //Lấy kết quả
        Optional<String> confirm = dialog.showAndWait();
        //Xử lý kết quả khi nhấn OK
        confirm.ifPresent(selectedHeader -> {
            //Xóa Note được chọn
            try { 
                //Xóa thành công
                Note deletedNote = clientServerService.deleteNote(myUser.getUsername(), selectedHeader);
                showAlert(Alert.AlertType.INFORMATION, "Successfully create " + deletedNote.getHeader());
                //Xóa khỏi list và load lại
                myNotes.remove(deletedNote);
                //Load lại My Notes Scene
                initMyNotesScene(myNotes);
            } catch (ClientServerServiceException ex) {
                //Xử lý các ngoại lệ
                switch (ex.getErrorType()) {
                    case ClientServerService.ErrorType.ALREADY_EXISTS -> {
                        showAlert(Alert.AlertType.ERROR, "This note already exists");
                    }
                    case ClientServerService.ErrorType.CAN_NOT_EXECUTE -> {
                        showAlert(Alert.AlertType.ERROR, "Can't create new note");
                    }
                    case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                        showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                    }
                    case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                        showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                    }
                }
            }
        });
    }

    @FXML
    void handleMyAccountButton(ActionEvent event) {
        //Chuyển scene sang My Account Scene
        changeSceneInExtraScene(myAccountButton);
        //Load My Account Scene
        initMyAccountScene(myUser);
    }

    @FXML
    void handleSaveAccountButton(ActionEvent event) {
        errorBirthdayFieldLabel.setVisible(false);
        errorNameFieldLabel.setVisible(false);
        errorPasswordFieldLabel.setVisible(false);
        //Lấy password
        if("".equals(passwordField.getText())) {
            errorPasswordFieldLabel.setVisible(true);
        }
        myUser.setPassword(passwordField.getText());
        //Láy thông tin name
        if("".equals(nameField.getText())) {
            errorNameFieldLabel.setVisible(true);
        }
        myUser.setName(nameField.getText());
        //Lấy school
        myUser.setSchool(schoolField.getText());
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
            myUser.setBirthday(Date.valueOf(LocalDate.of(yearOfBirth, monthOfBirth, dayOfBirth)));
        }
        //Lấy gender
        if(genderMale.isSelected()) {
            myUser.setGender(User.Gender.MALE);
        } else if (genderFemale.isSelected()) {
            myUser.setGender(User.Gender.FEMALE);
        } else {
            myUser.setGender(User.Gender.OTHER);
        }
        //Kiểm tra xem có lỗi nào không
        if(errorNameFieldLabel.isVisible() || errorPasswordFieldLabel.isVisible() 
                || errorBirthdayFieldLabel.isVisible()) {
            return;
        }
        //Cập nhật User
        try { 
            //Cập nhật thành công
            myUser = clientServerService.updateUser(myUser);
            showAlert(Alert.AlertType.INFORMATION, "Successfully update for " + myUser.getUsername());
        } catch (ClientServerServiceException ex) {
            //Xử lý các ngoại lệ
            switch (ex.getErrorType()) {
                case ClientServerService.ErrorType.NOT_EXISTS -> {
                    showAlert(Alert.AlertType.ERROR, "Not exist user");
                }
                case ClientServerService.ErrorType.CAN_NOT_EXECUTE -> {
                    showAlert(Alert.AlertType.ERROR, "Can't update your user");
                }
                case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                    showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                }
                case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                    showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                }
            }
        }
    }

    @FXML
    void handleChangePasswordButton(ActionEvent event) {
        //Hiện dialog để nhập mật khẩu cũ
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter your present password");
        //Lấy kết quả
        Optional<String> confirm = dialog.showAndWait();
        //Xử lý kết quả
        confirm.ifPresent(password -> {
            //Nếu nhập đúng thì cho phép nhập mật khẩu mới
            if(password.equals(myUser.getPassword())) {
                passwordField.setEditable(true);
            }
        });      
    }

    @FXML
    void handleImportExportButton(ActionEvent event) {
        //Chuyển sang Import/Export Scene
        changeSceneInExtraScene(importExportButton);
        //Lấy tất cả các note của myUser
        try { 
            //Lấy thành công
            myNotes = clientServerService.getAllNotes(myUser.getUsername());
        } catch (ClientServerServiceException ex) {
            //Xử lý các ngoại lệ
            switch (ex.getErrorType()) {
                case ClientServerService.ErrorType.NOT_EXISTS -> {
                    myNotes = new ArrayList<>();
                }
                case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                    showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                }
                case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                    showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                }
            }
        }
        //Init lại Scene
        initImportExportScene(myNotes);
    }

    @FXML
    void handleExportFileButton(ActionEvent event) {
        //Lấy header được chọn từ ComboBox
        String selectedNoteHeader = exportNoteComboBox.getSelectionModel().getSelectedItem();
        //Lấy dữ liệu từ note được chọn
        try { 
            //Lấy thành công
            Note selectedNote = clientServerService.openNote(myUser.getUsername(), selectedNoteHeader);
            //Export file
            PdfService.export(selectedNoteHeader + ".pdf", 
                    Note.ContentConverter.convertToObjectText(selectedNote.getContent()));
            //Thông báo
            showAlert(Alert.AlertType.INFORMATION, "Succesfully export");
        } catch (ClientServerServiceException ex) {
            //Xử lý các ngoại lệ
            switch (ex.getErrorType()) {
                case ClientServerService.ErrorType.NOT_EXISTS -> {
                    showAlert(Alert.AlertType.ERROR, "This note not exists");
                }
                case ClientServerService.ErrorType.CAN_NOT_EXECUTE -> {
                    showAlert(Alert.AlertType.ERROR, "Can't open this note");
                }
                case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                    showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                }
                case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                    showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                }
            }
        } catch (FileNotFoundException | DocumentException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't export this file");
        }
    }

    @FXML
    void handleImportFileButton(ActionEvent event) {
        try {
            //Lấy số trang
            int numOfPage = PdfService.getNumberOfPage(importFileName.getText());
            //Lấy dữ liệu từ PDF và chuyển vào content
            String contents = "";
            for(int i = 1; i <= numOfPage; i++) {
                contents += PdfService.read(importFileName.getText(), i);
                contents += "\n----------------------\n";
            }
            contentArea.setText(contents);
            numCharLabel.setText(String.valueOf(contentArea.getText().length()) + "/10000");
            //Thông báo
            showAlert(Alert.AlertType.INFORMATION, "Succesfully import");
            //Chuyển sang main
            mainScene.setVisible(true);
            extraServiceScene.setVisible(false);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't read this file");
        }
    }

    @FXML
    void handleChooseInputFileButton(ActionEvent event) {
        //Tạo FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your file");
        //Thiết lập loại file được phép chọn
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
        fileChooser.getExtensionFilters().add(extensionFilter);
        //Show FileChooser
        File file = fileChooser.showOpenDialog(null);
        //Lấy đường dẫn của file được chọn
        if(file != null) {
            importFileName.setText(file.getPath());
        }
    }
    
    @FXML
    void handleShareNoteButton(ActionEvent event) {
        //Chuyển sang Scene ShareNote
        changeSceneInExtraScene(shareNoteButton);
        //Lấy tất cả các note của myUser
        try { 
            //Lấy thành công
            myNotes = clientServerService.getAllNotes(myUser.getUsername());
        } catch (ClientServerServiceException ex) {
            //Xử lý các ngoại lệ
            switch (ex.getErrorType()) {
                case ClientServerService.ErrorType.NOT_EXISTS -> {
                    myNotes = new ArrayList<>();
                }
                case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                    showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                }
                case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                    showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                }
            }
        }
        //Lấy tất cả các note được share tới myUser này
        try { 
            //Lấy thành công
            mySharedNotes = clientServerService.getAllReceivedNotes(myUser.getUsername());
        } catch (ClientServerServiceException ex) {
            //Xử lý các ngoại lệ
            switch (ex.getErrorType()) {
                case ClientServerService.ErrorType.NOT_EXISTS -> {
                    mySharedNotes = new ArrayList<>();
                }
                case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                    showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                }
                case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                    showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                }
            }
        }
        //Init lại Scene
        initShareNoteScene(myNotes, mySharedNotes);
    }
    
    @FXML
    void handleSendNoteButton(ActionEvent event) {
        //Lấy header được chọn từ ComboBox và lấy note tương ứng
        String selectedNoteHeader = chooseShareNoteComboBox.getSelectionModel().getSelectedItem();
        try { 
            //Lấy thành công
            Note selectedNote = clientServerService.openNote(myUser.getUsername(), selectedNoteHeader);
            //Lấy receiver Id
            String receiverUsename = chooseUserShareField.getText();
            //Tạo ShareNote mới để Share
            ShareNote newShareNote = new ShareNote();
            newShareNote.setNote(selectedNote);
            newShareNote.setReceiver(receiverUsename);
            if(shareTypeReadOnly.isSelected()) {
                newShareNote.setShareType(ShareNote.ShareType.READ_ONLY);
            } else {
                newShareNote.setShareType(ShareNote.ShareType.CAN_EDIT);
            }
            clientServerService.sendNote(newShareNote);
            //Thông báo
            showAlert(Alert.AlertType.INFORMATION, "Successfully share");
        } catch (ClientServerServiceException ex) {
            //Xử lý các ngoại lệ
            switch (ex.getErrorType()) {
                case ClientServerService.ErrorType.NOT_EXISTS -> {
                    showAlert(Alert.AlertType.ERROR, "Not exists note or receiver");
                }
                case ClientServerService.ErrorType.CAN_NOT_EXECUTE -> {
                    showAlert(Alert.AlertType.ERROR, "Can't send this note");
                }
                case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                    showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                }
                case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                    showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                }
            }
        }
    }
    
    /**
     * Chạy Dashboard và thiết lập các thuộc tính ban đầu
     */
    public void init() {  
        //Mở Client Server Service
        clientServerService = new ClientServerService();
        //Thiết lập các thông tin myUser
        userLabel.setText(myUser.getName());
        //Mở Note thao tác gần nhất
        try { 
            //Lấy thành công
            myNote = clientServerService.openLastNote(myUser.getUsername());
            //Chỉnh trạng thái lưu Note 
            savedNoteStatus = true;     
        } catch (ClientServerServiceException ex) {
            //Xử lý các ngoại lệ
            switch (ex.getErrorType()) {
                case ClientServerService.ErrorType.NOT_EXISTS -> {
                    myNote = new Note();
                    myNote.setAuthor(myUser.getUsername());
                    myNote.setHeader("New Note");
                    myNote.setContent("Edit here");
                    List<String> filters = new ArrayList<>();
                    myNote.setFilters(filters);
                    //Chỉnh trạng thái lưu Note 
                    savedNoteStatus = false;     
                }
                case ClientServerService.ErrorType.CAN_NOT_EXECUTE -> {
                    showAlert(Alert.AlertType.ERROR, "Can't open your last note");
                }
                case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                    showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                }
                case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                    showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                }
            }
        }
        //Init lại Scene
        initAndChangeToMainScene();
    }

    private void initAndChangeToMainScene() { 
        //Chuyển sang main scene
        mainScene.setVisible(true);
        extraServiceScene.setVisible(false);
        //Thiết lập content, header
        contentArea.setText(Note.ContentConverter.convertToObjectText(myNote.getContent()));
        contentArea.setEditable(true);
        noteHeaderLabel.setText(myNote.getHeader());
        numCharLabel.setText(String.valueOf(contentArea.getText().length()) + "/10000");
        //Thiết lập undoRedoService và lưu vào văn bản đầu tiên
        undoRedoService = new UndoRedoService();
        undoRedoService.saveText(contentArea.getText()); 
        //Load lại Filter GUI
        loadFilter(myNote.getFilters(), 8);   
        //Thiết lập font
        fontTypeComboBox.getItems().clear();
        for(String fontType: Font.getFontNames()) {
            fontTypeComboBox.getItems().add(fontType);
        }
        //Thiết lập size
        fontSizeComboBox.getItems().clear();
        for(int i = 0; i <= 8; i++) {
            fontSizeComboBox.getItems().add(String.valueOf(8 + 2 * i));
        }
        for(int i = 0; i <= 8; i++) {
            fontSizeComboBox.getItems().add(String.valueOf(32 + 8 * i));
        }
    }
    
    private void initAndChangeToExtraServiceScene() {
        //Chuyển sang extra scene
        mainScene.setVisible(false);
        extraServiceScene.setVisible(true);
        //Chuyển sang scene My Notes
        changeSceneInExtraScene(myNotesButton);
        //Lấy tất cả các Note của myUser
        try { 
            //Lấy thành công
            myNotes = clientServerService.getAllNotes(myUser.getUsername());
        } catch (ClientServerServiceException ex) {
            //Xử lý các ngoại lệ
            switch (ex.getErrorType()) {
                case ClientServerService.ErrorType.NOT_EXISTS -> {
                    myNotes = new ArrayList<>();
                }
                case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                    showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                }
                case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                    showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                }
            }
        }
        //Init lại Scene
        initMyNotesScene(myNotes);
    }

    private void initMyNotesScene(List<Note> notes) {        
        //Làm sạch layout
        noteCardLayout.getChildren().clear();
        if(notes.isEmpty()) {
            return;
        }
        //Load các Note Card
        for(int i=0; i < notes.size(); i++) {
            //Load Note Card Layout
            FXMLLoader fXMLLoader = new FXMLLoader();
            String noteCardFXMLPath = "NoteCardFXML.fxml";
            fXMLLoader.setLocation(getClass().getResource(noteCardFXMLPath));
            try {
                HBox box = fXMLLoader.load();
                //Thiết lập dữ liệu cho Note Card
                NoteCardFXMLController noteCardFXMLController = fXMLLoader.getController();
                noteCardFXMLController.setData(notes.get(i));
                //Xử lý khi nhấn vào note card
                box.setOnMouseClicked((MouseEvent event) -> {
                    //Tạo thông báo và mở note nếu chọn OK
                    Optional<ButtonType> optional = showAlert(Alert.AlertType.CONFIRMATION, 
                            "Open " + noteCardFXMLController.getHeader());
                    if(optional.get() == ButtonType.OK) {
                        autoSave();
                        try {
                            //Lấy thành công
                            myNote = clientServerService.openNote(noteCardFXMLController.getAuthor(),
                                    noteCardFXMLController.getHeader());
                            //Chỉnh trạng thái lưu Note 
                            savedNoteStatus = true;     
                            //Load lại Edit Scene và mở Edit Scene
                            initAndChangeToMainScene();
                        } catch (ClientServerServiceException ex) {
                            //Xử lý các ngoại lệ
                            switch (ex.getErrorType()) {
                                case ClientServerService.ErrorType.NOT_EXISTS -> {
                                    showAlert(Alert.AlertType.ERROR, "This note not exists");
                                }
                                case ClientServerService.ErrorType.CAN_NOT_EXECUTE -> {
                                    showAlert(Alert.AlertType.ERROR, "Can't open this note");
                                }
                                case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                                    showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                                }
                                case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                                    showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                                }
                            } 
                        }
                    }
                });
                //Thêm Note Card vào layout
                noteCardLayout.getChildren().add(box);
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }      
    }
    
    private void initMyAccountScene(User user) {
        //Thiết lập các thuộc tính
        usernameField.setText(user.getUsername());
        usernameField.setEditable(false);
        passwordField.setText(user.getPassword());
        userIdLabel.setText(String.valueOf(user.getId()));
        nameField.setText(user.getName());
        schoolField.setText(user.getSchool());
        dayOfBirthField.setText(String.valueOf(user.getBirthday().toLocalDate().getDayOfMonth()));
        monthOfBirthField.setText(String.valueOf(user.getBirthday().toLocalDate().getMonthValue()));
        yearOfBirthField.setText(String.valueOf(user.getBirthday().toLocalDate().getYear()));
        switch(user.getGender()) {
            case User.Gender.MALE -> {
                genderMale.setSelected(true);
            }
            case User.Gender.FEMALE -> {
                genderFemale.setSelected(true);
            }
            case User.Gender.OTHER -> {
                genderOther.setSelected(true);
            }
        }
        //Ẩn các error
        errorBirthdayFieldLabel.setVisible(false);
        errorNameFieldLabel.setVisible(false);
        errorPasswordFieldLabel.setVisible(false);
    }
    
    private void initImportExportScene(List<Note> notes) {
        //Clear ComboBox và thêm vào các header note trong list
        exportNoteComboBox.getItems().clear();
        if(notes.isEmpty()) {
            return;
        }
        for(Note note: notes) {
            exportNoteComboBox.getItems().add(note.getHeader());
        }
        //Clear ComboBox và thêm vào các định dạng cho phép
        exportFormatComboBox.getItems().clear();
        exportFormatComboBox.getItems().add("PDF");
        //Set header của note đang edit
        importNoteName.setText(myNote.getHeader());
    }

    private void initShareNoteScene(List<Note> notes, List<ShareNote> shareNotes) {
        //Clear ComboBox và thêm vào các header note trong list
        chooseShareNoteComboBox.getItems().clear();
        if(notes.isEmpty()) {
            return;
        }
        for(Note note: notes) {
            chooseShareNoteComboBox.getItems().add(note.getHeader());
        }
        //Set Type mặc định là Read Only
        shareTypeReadOnly.setSelected(true);
        //Khởi tạo các share note card
        shareNoteCardLayout.getChildren().clear();
        if(shareNotes.isEmpty()) {
            return;
        }
        for(int i=0; i < shareNotes.size(); i++) {
            //Load Note Card Layout
            FXMLLoader fXMLLoader = new FXMLLoader();
            String noteCardFXMLPath = "NoteCardFXML.fxml";
            fXMLLoader.setLocation(getClass().getResource(noteCardFXMLPath));
            try {
                HBox box = fXMLLoader.load();
                //Thiết lập dữ liệu cho Note Card
                NoteCardFXMLController noteCardFXMLController = fXMLLoader.getController();
                noteCardFXMLController.setData(shareNotes.get(i));
                //Xử lý khi nhấn vào note card
                box.setOnMouseClicked((MouseEvent event) -> {
                    //Tạo thông báo và mở note nếu chọn OK
                    Optional<ButtonType> optional = showAlert(Alert.AlertType.CONFIRMATION, 
                            "Open " + noteCardFXMLController.getHeader());
                    if(optional.get() == ButtonType.OK) {
                        autoSave();
                        try {
                            //Lấy thành công
                            myNote = clientServerService.openNote(noteCardFXMLController.getAuthor(),
                                    noteCardFXMLController.getHeader());
                            //Chỉnh trạng thái lưu Note 
                            savedNoteStatus = true;                                
                            //Load lại Edit Scene và mở Edit Scene
                            initAndChangeToMainScene();
                            //Nếu là READ_ONLY thì không được sửa
                            if(noteCardFXMLController.getShareType() == ShareNote.ShareType.READ_ONLY) {
                                contentArea.setEditable(false);
                            }
                        } catch (ClientServerServiceException ex) {
                            //Xử lý các ngoại lệ
                            switch (ex.getErrorType()) {
                                case ClientServerService.ErrorType.NOT_EXISTS -> {
                                    showAlert(Alert.AlertType.ERROR, "This note not exists");
                                }
                                case ClientServerService.ErrorType.CAN_NOT_EXECUTE -> {
                                    showAlert(Alert.AlertType.ERROR, "Can't open this note");
                                }
                                case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                                    showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                                }
                                case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                                    showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                                }
                            } 
                        }
                    }
                });
                //Thêm Note Card vào layout
                shareNoteCardLayout.getChildren().add(box);
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }        
    }
    
    private void autoSave() {
        if(!savedNoteStatus) {
            Optional<ButtonType> optional = showAlert(Alert.AlertType.CONFIRMATION, 
                    myNote.getHeader() + " is unsaved. Do you want to save?");
            if(optional.get() == ButtonType.OK) {
                //Set dữ liệu gần nhất cho myNote
                myNote.setHeader(noteHeaderLabel.getText());
                myNote.setContent(Note.ContentConverter.convertToDBText(contentArea.getText()));
                myNote.setLastModified(1);
                myNote.setLastModifiedDate(Date.valueOf(LocalDate.now()));
                //Lưu note
                try { 
                    //Lưu thành công
                    myNote = clientServerService.saveNote(myNote);
                    showAlert(Alert.AlertType.INFORMATION, "Successfully save for " + myNote.getHeader());
                    //Chỉnh trạng thái lưu Note 
                    savedNoteStatus = true;     
                } catch (ClientServerServiceException ex) {
                    //Xử lý các ngoại lệ
                    switch (ex.getErrorType()) {
                        case ClientServerService.ErrorType.CAN_NOT_EXECUTE -> {
                            showAlert(Alert.AlertType.ERROR, "Can't save this note");
                        }
                        case ClientServerService.ErrorType.FAILED_CONNECT_TO_SERVER -> {
                            showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                        }
                        case ClientServerService.ErrorType.UNSUPPORTED_SERVICE -> {
                            showAlert(Alert.AlertType.ERROR, "This service is unsupported");
                        }
                    }
                }
            }           
        }
    }

    private void loadFilter(List<String> filters, int maxColumn) {
        int column = 0;
        int row = 0;
        //Làm sạch filter layout
        filterGridLayout.getChildren().clear();
        if(filters.isEmpty()) {
            return;
        }
        //Thiết lập khoảng cách giữa các filter
        filterGridLayout.setHgap(8);
        filterGridLayout.setVgap(8);
        //Thiết lập filter layout
        try {
            for(int i = 0; i < filters.size(); i++) {
                //Load filter FXML
                FXMLLoader fXMLLoader = new FXMLLoader();
                String filterFXMLPath = "FilterFXML.fxml";
                fXMLLoader.setLocation(getClass().getResource(filterFXMLPath));
                HBox hbox = fXMLLoader.load();
                //Thiết lập dữ liệu cho filter
                FilterFXMLController filterFXMLController = fXMLLoader.getController();
                filterFXMLController.setData(filters.get(i));
                filterFXMLController.getRemoveFilterView().setOnMouseClicked(event -> {
                    myNote.getFilters().remove(filterFXMLController.getFilter());
                    loadFilter(myNote.getFilters(), 8);
                });
                //Chuyển hàng
                if(column == maxColumn){
                    column = 0;
                    row++;
                }
                //Thêm filter vừa tạo vào layout
                filterGridLayout.add(hbox, column++, row);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    private void changeSceneInExtraScene(Button button) {
        String pressedStyle = "-fx-background-color: linear-gradient(to bottom right, #0e544e, #0e2f52)";
        String unPressedStyle = "-fx-background-color: transparent";
        //init lại
        myNotesButton.setStyle(unPressedStyle);
        myNotesScene.setVisible(false);
        myAccountButton.setStyle(unPressedStyle);
        myAccountScene.setVisible(false);
        importExportButton.setStyle(unPressedStyle);
        importExportScene.setVisible(false);
        shareNoteButton.setStyle(unPressedStyle);
        shareNoteScene.setVisible(false);
        //Press button được chọn và chuyển scene tương ứng
        if (button == myNotesButton) {
            myNotesButton.setStyle(pressedStyle);
            myNotesScene.setVisible(true);
        } else if (button == myAccountButton) {          
            myAccountButton.setStyle(pressedStyle);
            myAccountScene.setVisible(true);
        } else if (button == importExportButton) {
            importExportButton.setStyle(pressedStyle);
            importExportScene.setVisible(true);
        } else if (button == shareNoteButton) {
            shareNoteButton.setStyle(pressedStyle);
            shareNoteScene.setVisible(true);
        }
    }
    
    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(String.valueOf(alertType));
        alert.setHeaderText(text);
        return alert.showAndWait();
    }
}