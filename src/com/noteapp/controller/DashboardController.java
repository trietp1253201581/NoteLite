package com.noteapp.controller;

import com.noteliteclient.service.PdfService;
import com.noteapp.model.datatransfer.Note;
import com.noteapp.model.datatransfer.ShareNote;
import com.noteapp.model.datatransfer.User;
import com.itextpdf.text.DocumentException;
import com.noteapp.dataaccess.DataAccessException;
import com.noteapp.model.datatransfer.NoteBlock;
import com.noteapp.model.datatransfer.NoteFilter;
import com.noteapp.service.CreateNoteService;
import com.noteapp.service.DeleteNoteService;
import com.noteapp.service.GetAllNotesService;
import com.noteapp.service.GetAllReceivedNotesService;
import com.noteapp.service.OpenNoteService;
import com.noteapp.service.SendNoteService;
import com.noteapp.service.UpdateUserService;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * FXML Controller class cho Dashboard GUI
 * 
 * @author Nhóm 23
 * @since 07/04/2024
 * @version 1.0
 */
public class DashboardController extends Controller {
    //Các thuộc tính FXML của form dashboard chung
    @FXML 
    private BorderPane extraServiceScene;
    @FXML
    private VBox mainScene;
    //Các thuộc tính chung
    @FXML
    private Label userLabel;   
    @FXML
    private Button closeButton;
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
    
    private User myUser;   
    private Note currentNote;
    private List<Note> myNotes;   
    private List<ShareNote> mySharedNotes;
    private boolean savedNoteStatus;
    
    @Override
    public void init() {
        initView();
        closeButton.setOnAction((ActionEvent event) -> {
            super.close();
        });
        //Switch
        myNotesButton.setOnAction((ActionEvent event) -> {
            changeSceneInExtraScene(myNotesButton);
            try {
                noteCollectionService = new GetAllNotesService(myUser.getUsername());
                myNotes = noteCollectionService.execute();
            } catch (DataAccessException ex) {
                myNotes = new ArrayList<>();
            }
            initMyNotesScene(myNotes);
        });
        myAccountButton.setOnAction((ActionEvent event) -> {
            changeSceneInExtraScene(myAccountButton);
            initMyAccountScene(myUser);
        });
        importExportButton.setOnAction((ActionEvent event) -> {
            changeSceneInExtraScene(importExportButton);
            try {
                noteCollectionService = new GetAllNotesService(myUser.getUsername());
                myNotes = noteCollectionService.execute();
            } catch (DataAccessException ex) {
                myNotes = new ArrayList<>();
            }
            initImportExportScene(myNotes);
        });
        shareNoteButton.setOnAction((ActionEvent event) -> {
            //Chuyển sang Scene ShareNote
            changeSceneInExtraScene(shareNoteButton);
            //Lấy tất cả các note của myUser
            try { 
                //Lấy thành công
                noteCollectionService = new GetAllNotesService(myUser.getUsername());
                myNotes = noteCollectionService.execute();
            } catch (DataAccessException ex) {
                showAlert(Alert.AlertType.ERROR, ex.getMessage());
            }
            //Lấy tất cả các note được share tới myUser này
            try { 
                //Lấy thành công
                shareNoteCollectionService = new GetAllReceivedNotesService(myUser.getUsername());
                mySharedNotes = shareNoteCollectionService.execute();
            } catch (DataAccessException ex) {
                showAlert(Alert.AlertType.ERROR, ex.getMessage());
            }
            //Init lại Scene
            initShareNoteScene(myNotes, mySharedNotes);
        });
        //My Note Scene
        searchNoteField.setOnAction((ActionEvent event) -> {
            searchNote();
        });
        createNoteButton.setOnAction((ActionEvent event) -> {
            createNote();
        });
        deleteNoteButton.setOnAction((ActionEvent event) -> {
            deleteNote();
        });
        //My Account Scene
        changePasswordButton.setOnAction((ActionEvent event) -> {
            changePassword();
        });
        saveAccountButton.setOnAction((ActionEvent event) -> {
            saveAccount();
        });
        //Import export
        exportFileButton.setOnAction((ActionEvent event) -> {
            exportFile();
        });
        importFileButton.setOnAction((ActionEvent event) -> {
            importFile();
        });
        chooseInputFileButton.setOnAction((ActionEvent event) -> {
            chooseFileToInput();
        });
        //Share Note
        sendNoteButton.setOnAction((ActionEvent event) -> {
            sendNote();
        });
        //Other
        backMainSceneButton.setOnAction((ActionEvent event) -> {
            openEditNoteView(myUser, currentNote);
        });
        logoutButton.setOnAction((ActionEvent event) -> {
            openLogin();
        });
    }
    
    public void initView() {
        try {
            userLabel.setText(myUser.getName());
            noteCollectionService = new GetAllNotesService(myUser.getUsername());
            myNotes = noteCollectionService.execute();
        } catch (DataAccessException ex) {
            myNotes = new ArrayList<>();
        }
        initMyNotesScene(myNotes);
    }
    
    protected void initMyNotesScene(List<Note> notes) {        
        //Làm sạch layout
        noteCardLayout.getChildren().clear();
        if(notes.isEmpty()) {
            return;
        }
        //Load các Note Card
        for(int i=0; i < notes.size(); i++) {
            //Load Note Card Layout
            FXMLLoader fXMLLoader = new FXMLLoader();
            String noteCardFXMLPath = "../view/NoteCardView.fxml";
            fXMLLoader.setLocation(getClass().getResource(noteCardFXMLPath));
            try {
                HBox box = fXMLLoader.load();
                //Thiết lập dữ liệu cho Note Card
                NoteCardController noteCardFXMLController = fXMLLoader.getController();
                noteCardFXMLController.setData(notes.get(i));
                //Xử lý khi nhấn vào note card
                box.setOnMouseClicked((MouseEvent event) -> {
                    //Tạo thông báo và mở note nếu chọn OK
                    Optional<ButtonType> optional = showAlert(Alert.AlertType.CONFIRMATION, 
                            "Open " + noteCardFXMLController.getNote().getHeader());
                    if(optional.get() == ButtonType.OK) {                        
                        try {
                            //Lấy thành công
                            noteService = new OpenNoteService(noteCardFXMLController.getNote().getId());
                            currentNote = noteService.execute();
                            //Load lại Edit Scene và mở Edit Scene
                            openEditNoteView(myUser, currentNote);
                        } catch (DataAccessException ex) {
                            showAlert(Alert.AlertType.ERROR, ex.getMessage());
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
    
    protected void initMyAccountScene(User user) {
        //Thiết lập các thuộc tính
        usernameField.setText(user.getUsername());
        usernameField.setEditable(false);
        passwordField.setText(user.getPassword());
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
    
       
    protected void initImportExportScene(List<Note> notes) {
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
        importNoteName.setText(currentNote.getHeader());
    }

    protected void initShareNoteScene(List<Note> notes, List<ShareNote> shareNotes) {
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
            String noteCardFXMLPath = "../view/NoteCardView.fxml";
            fXMLLoader.setLocation(getClass().getResource(noteCardFXMLPath));
            try {
                HBox box = fXMLLoader.load();
                //Thiết lập dữ liệu cho Note Card
                NoteCardController noteCardFXMLController = fXMLLoader.getController();
                noteCardFXMLController.setData(shareNotes.get(i));
                //Xử lý khi nhấn vào note card
                box.setOnMouseClicked((MouseEvent event) -> {
                    //Tạo thông báo và mở note nếu chọn OK
                    Optional<ButtonType> optional = showAlert(Alert.AlertType.CONFIRMATION, 
                            "Open " + noteCardFXMLController.getShareNote().getHeader());
                    if(optional.get() == ButtonType.OK) {
                        try {
                            //Lấy thành công
                            noteService = new OpenNoteService(noteCardFXMLController.getShareNote().getId());
                            currentNote = noteService.execute();
                            //Load lại Edit Scene và mở Edit Scene
                            openEditNoteView(myUser, currentNote);
                        } catch (DataAccessException ex) {
                            showAlert(Alert.AlertType.ERROR, ex.getMessage());
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
    
    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }
    
    public void setCurrentNote(Note currentNote) {
        this.currentNote = currentNote;
    }
    
    protected void searchNote() {
        //Lấy thông tin cần search
        String searchText = searchNoteField.getText();
        //Tạo list mới để chứa các note hợp lệ
        List<Note> notes = new ArrayList<>();
        //Thêm các note hợp lệ vào list
        for(Note newNote: myNotes) {
            if(newNote.getHeader().contains(searchText) 
                    || NoteFilter.ListConverter.convertToString(newNote.getFilters()).contains(searchText)) { 
                notes.add(newNote);
            }     
        }
        //Load lại My Notes Scene
        initMyNotesScene(notes);
    }
    
    protected void createNote() {
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
            newNote.getBlocks().add(
                    new NoteBlock(1, "B1", myUser.getUsername(), "Edit here", NoteBlock.BlockType.TEXT));
            newNote.setLastModifiedDate(Date.valueOf(LocalDate.now()));
            //Tạo Note mới
            try { 
                //Tạo thành công
                noteService = new CreateNoteService(newNote);
                newNote = noteService.execute();
                showAlert(Alert.AlertType.INFORMATION, "Successfully create " + newNote.getHeader());
                //Thêm vào list và load lại
                myNotes.add(newNote);
                initMyNotesScene(myNotes);
            } catch (DataAccessException ex) {
                showAlert(Alert.AlertType.ERROR, ex.getMessage());
            }
        });
    }

    protected void deleteNote() {
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
                Note deletedNote = new Note();
                for(Note note: myNotes) {
                    if(note.getHeader().equals(selectedHeader)) {
                        deletedNote = note;
                    }
                }
                noteService = new DeleteNoteService(deletedNote.getId());
                deletedNote = noteService.execute();
                showAlert(Alert.AlertType.INFORMATION, "Successfully delete " + deletedNote.getHeader());
                //Xóa khỏi list và load lại
                myNotes.remove(deletedNote);
                //Load lại My Notes Scene
                initMyNotesScene(myNotes);
            } catch (DataAccessException ex) {
                showAlert(Alert.AlertType.ERROR, ex.getMessage());
            }
        });
    }

    protected void saveAccount() {
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
            userService = new UpdateUserService(myUser);
            myUser = userService.execute();
            showAlert(Alert.AlertType.INFORMATION, "Successfully update for " + myUser.getUsername());
        } catch (DataAccessException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    protected void changePassword() {
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

    protected void exportFile() {
        //Lấy header được chọn từ ComboBox
        String selectedNoteHeader = exportNoteComboBox.getSelectionModel().getSelectedItem();
        //Lấy dữ liệu từ note được chọn
        try { 
            //Lấy thành công
            Note selectedNote = new Note();
            for(Note note: myNotes) {
                if(note.getHeader().equals(selectedNoteHeader)) {
                    selectedNote = note;
                }
            }
            noteService = new OpenNoteService(selectedNote.getId());
            selectedNote = noteService.execute();
            //Export file
            String content = "";
            for(NoteBlock block: selectedNote.getBlocks()) {
                content += NoteBlock.TextContentConverter.convertToObjectText(block.getContent());
            }
            PdfService.export(selectedNoteHeader + ".pdf", content);
            //Thông báo
            showAlert(Alert.AlertType.INFORMATION, "Succesfully export");
        } catch (DataAccessException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        } catch (FileNotFoundException | DocumentException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't export this file");
        }
    }

    protected void importFile() {
        try {
            //Lấy số trang
            int numOfPage = PdfService.getNumberOfPage(importFileName.getText());
            //Lấy dữ liệu từ PDF và chuyển vào content
            String contents = "";
            for(int i = 1; i <= numOfPage; i++) {
                contents += PdfService.read(importFileName.getText(), i);
                contents += "\n----------------------\n";
            }
            
            //Thông báo
            showAlert(Alert.AlertType.INFORMATION, "Succesfully import");
            //Chuyển sang main
            mainScene.setVisible(true);
            extraServiceScene.setVisible(false);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't read this file");
        }
    }

    protected void chooseFileToInput() {
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

    protected void sendNote() {
        //Lấy header được chọn từ ComboBox và lấy note tương ứng
        String selectedNoteHeader = chooseShareNoteComboBox.getSelectionModel().getSelectedItem();
        try { 
            //Lấy thành công
            Note selectedNote = new Note();
            for(Note note: myNotes) {
                if(note.getHeader().equals(selectedNoteHeader)) {
                    selectedNote = note;
                }
            }
            noteService = new OpenNoteService(selectedNote.getId());
            selectedNote = noteService.execute();
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
            shareNoteService = new SendNoteService(newShareNote);
            shareNoteService.execute();
            //Thông báo
            showAlert(Alert.AlertType.INFORMATION, "Successfully share");
        } catch (DataAccessException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
 
    private void changeSceneInExtraScene(Button button) {
        String pressedStyle = "-fx-background-color: #1a91b8";
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
    
    protected void openEditNoteView(User user, Note note) {
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            String registerViewPath = "../view/EditNoteView.fxml";
            fXMLLoader.setLocation(getClass().getResource(registerViewPath));

            scene = new Scene(fXMLLoader.load());
            
            EditNoteViewController controller = fXMLLoader.getController();
            controller.setStage(stage);
            controller.setMyUser(user);
            controller.setMyNote(note);
            controller.init();
            controller.setOnAutoUpdate();
            
            setSceneMoveable();
            
            stage.setScene(scene);  
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't open edit view");
        }
    }
    
    protected void openLogin() {
        try {
            //Load Login GUI
            FXMLLoader fXMLLoader = new FXMLLoader();
            String loginFXMLPath = "../view/LoginView.fxml";
            fXMLLoader.setLocation(getClass().getResource(loginFXMLPath));
            //Chuyển sang GUI Login
            scene = new Scene(fXMLLoader.load());
            LoginController loginController = fXMLLoader.getController();
            loginController.setStage(stage);
            loginController.init();
            
            setSceneMoveable();
            
            stage.setScene(scene);  
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't open login");
        }
    }
}