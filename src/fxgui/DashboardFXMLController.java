package fxgui;

import client.service.UndoRedoService;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Note;
import model.RequestCommand;
import model.User;
import client.networking.ClientRequest;
import client.service.PdfService;
import java.io.File;
import java.util.Optional;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;

/**
 * FXML Controller class cho Dashboard GUI
 * 
 * @author Lê Minh Triết
 * @since 31/03/2024
 * @version 1.0
 */
public class DashboardFXMLController {
    //Các thuộc tính FXML
    @FXML
    private TextArea contentArea;
    @FXML
    private Button editNoteButton;
    @FXML
    private AnchorPane editNoteScene;
    @FXML
    private Label header;
    @FXML
    private Label id;
    @FXML
    private Button logoutButton;
    @FXML
    private AnchorPane myNotesScene;
    @FXML
    private VBox noteCardLayout;   
    @FXML
    private Button openButton;
    @FXML
    private Button myNotesButton;
    @FXML
    private Button myAccountButton;
    @FXML
    private Label userId;
    @FXML
    private Label userLabel;   
    @FXML
    private Label noteHeaderLabel;  
    @FXML
    private Button renameButton;  
    @FXML
    private TextField noteHeaderField;  
    @FXML
    private Label numCharLabel;  
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;   
    @FXML
    private GridPane filterGridLayout;    
    @FXML
    private Button addFilterButton;   
    @FXML
    private Button removeFilterButton;   
    @FXML
    private TextField searchNoteField;  
    @FXML
    private Button createNoteButton;   
    @FXML
    private Button deleteNoteButton;   
    @FXML
    private Button openNoteButton;   
    @FXML
    private AnchorPane myAccountScene;
    @FXML
    private Label usernameLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label userIdLabel;
    @FXML
    private TextField nameField;
    @FXML 
    private TextField birthdayField;
    @FXML
    private TextField schoolField;
    @FXML
    private Button changePasswordButton;
    @FXML
    private Button saveAccountButton;  
    @FXML
    private Button importExportButton;
    @FXML
    private Button exportFileButton;
    @FXML
    private AnchorPane importExportScene;
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
    
    private UndoRedoService undoRedoService;
    private User user;   
    private Note myNote;    
    private List<Note> myNotes;   
    private RequestCommand requestCommand;
    private String replyFromServer;

    public void setUser(User user) {
        this.user = user;
    }
    
    public void setMyNote(Note myNote) {
        this.myNote = myNote;
    }
    
    /**
     * Xử lý sự kiện khi ấn vào Logout Button
     * @param event 
     */
    @FXML
    void handleLogoutButton(ActionEvent event) {       
        //Set lại style của button
        logoutButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #0e544e, #0e2f52)");
        //Ẩn Dashboard GUI
        logoutButton.getScene().getWindow().hide();
        //Load Login GUI
        FXMLLoader fXMLLoader = new FXMLLoader();
        fXMLLoader.setLocation(getClass().getResource("LoginFXML.fxml"));
        //Mở Login GUI
        Stage stage = (Stage)logoutButton.getScene().getWindow();
        try {
            Scene scene = new Scene(fXMLLoader.load());

            stage.setTitle("NoteLite");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Xử lý sự kiện ấn vào Edit Note Button
     * @param event 
     */
    @FXML
    void handleEditNoteButton(ActionEvent event) {  
        //Chuyển sang scene edit
        changeScene(editNoteButton);
    }
    
    /**
     * Xử lý sự kiện khi ấn vào Save Button
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleSaveButton(ActionEvent event) throws IOException {  
        //Thiết lập lại undoRedoService
        undoRedoService = new UndoRedoService();     
        //Set dữ liệu gần nhất cho myNote
        myNote.setHeader(noteHeaderLabel.getText());
        myNote.setContent(contentArea.getText());
        myNote.setLastModified(1);
        myNote.setLastModifiedDate(Date.valueOf(LocalDate.now()));
        //Lưu note
        saveNote(myNote);
        //Nhận reply và thông báo
        if(replyFromServer.equals("Can't save")) {
            showAlert(Alert.AlertType.ERROR, replyFromServer);
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Successfully save for " + myNote.getHeader());
        }
    }

    /**
     * Xử lý sự kiện khi ấn vào Rename Button
     * @param event 
     */
    @FXML
    void handleRenameButton(ActionEvent event) {        
        //Thiết lập note name hiện tại cho noteHeaderField
        noteHeaderField.setText(noteHeaderLabel.getText());
        //Ẩn Label và hiện field
        noteHeaderLabel.setVisible(false);
        noteHeaderField.setVisible(true);
    }
    
    /**
     * Xử lý sự kiện khi nhấn Enter ở noteHeaderLabel
     * @param event 
     */
    @FXML
    void handleNoteHeaderField(ActionEvent event) {        
        //Thiết lập note name vừa nhập cho Label
        noteHeaderLabel.setText(noteHeaderField.getText());
        //Ẩn field và hiện Label
        noteHeaderField.setVisible(false);
        noteHeaderLabel.setVisible(true);
    }
    
    /**
     * Xử lý sự kiện khi gõ trong text area
     * @param event 
     */
    @FXML
    void changeTextArea(KeyEvent event) {  
        //Lấy các thông số
        int nowLength = contentArea.getText().length();
        int lastLength = undoRedoService.getLastText().length();
        //Kiểm tra đã vượt quá số ký tự quy định
        numCharLabel.setText(String.valueOf(nowLength) + "/10000");
        //Tự động lưu văn bản vào undoRedoService
        if(nowLength < lastLength || nowLength > lastLength + 3) {
            undoRedoService.saveText(contentArea.getText());
        }
    }
    
    /**
     * Xử lý sự kiện khi ấn nút Undo
     * @param event 
     */
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
    }
    
    /**
     * Xử lý sự kiện khi ấn nút redo
     * @param event 
     */
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
    }
    
    /**
     * Xử lý sự kiện khi nhấn nút Add Filter
     * @param event 
     */
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
            List<String> filters = new ArrayList<>();
            filters.addAll(myNote.getFilters());
          
            if(filters.contains(newFilter)) {
                //Nếu filter đã tồn tại thì thông báo lỗi
                showAlert(Alert.AlertType.ERROR, "Exist Filter");
            } else {
                //Thêm filter vào list
                filters.add(newFilter);
                myNote.setFilters(filters);
                //Load lại filter GUI
                loadFilter(filters, 6);
            }     
        });      
    }
    
    @FXML
    void handleRemoveFilterButton(ActionEvent event) {     
        //Lấy list các filter
        List<String> filters = new ArrayList<>();
        filters.addAll(myNote.getFilters());
        //Hiện dialog để chọn filter cần xóa
        ChoiceDialog<String> dialog = new ChoiceDialog<>(filters.get(0), filters);     
        dialog.setTitle("Remove filter of" + myNote.getHeader());
        dialog.setHeaderText("Choose the filter to remove");   
        //Lấy kết quả
        Optional<String> confirm = dialog.showAndWait();
        //Xử lý kết quả khi nhấn Enter
        confirm.ifPresent(removedFilter -> {     
            //Xóa filter được chọn
            filters.remove(removedFilter);
            myNote.setFilters(filters);
            //Load lại filter GUI
            loadFilter(filters, 6);     
        });  
    }    
    
    /**
     * Xử lý sự kiện khi ấn vào My Notes Button
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleMyNotesButton(ActionEvent event) throws IOException {        
        //Chuyển sang scene My Notes
        changeScene(myNotesButton);
        //Lấy tất cả các Note của user
        getAllNotes(user.getId());
        //Dựa vào reply từ server để xử lý
        if(!replyFromServer.equals("Not found")) {
            //Thêm các note vào list các note của user
            String[] datas = replyFromServer.split(":::");      
            myNotes = new ArrayList<>();
            for(String element: datas) {
                myNotes.add(Note.valueOf(element));
            }
            //Thiết lập lại My Notes scene
            initMyNotesScene(myNotes);
        }
    }
    
    /**
     * Xử lý sự kiện khi ấn Enter ở Search Note Field
     * @param event 
     */
    @FXML
    void handleSearchNoteField(ActionEvent event) {
        //Lấy thông tin cần search
        String searchText = searchNoteField.getText();
        //Tạo list mới để chứa các note hợp lệ
        List<Note> notes = new ArrayList<>();
        //Thêm các note hợp lệ vào list
        for(Note newNote: myNotes) {
            if(newNote.getHeader().contains(searchText) || newNote.getFilters().contains(searchText)) { 
                notes.add(newNote);
            }     
        }
        //Load lại My Notes Scene
        initMyNotesScene(notes);
    }
    
    /**
     * Xử lý sự kiện khi ấn vào Create Note Button
     * @param event 
     */
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
            newNote.setUserId(user.getId());
            newNote.setHeader(selectedHeader);
            newNote.setContent("Edit here");
            newNote.setLastModifiedDate(Date.valueOf(LocalDate.now()));
            List<String> filters = new ArrayList<>();
            filters.add("text");
            newNote.setFilters(filters);
            //Tạo Note mới
            try {
                createNote(newNote);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //Nhận reply từ server và thông báo
            if(replyFromServer.equals("Can't create") || replyFromServer.equals("Exist")) {
                showAlert(Alert.AlertType.ERROR, replyFromServer);
            } else {
                //Láy dữ liệu note mới
                newNote = Note.valueOf(replyFromServer);
                //Thông báo đã tạo được
                showAlert(Alert.AlertType.INFORMATION, "Successfully create " + newNote.getHeader());
                //Thêm note mới vào list và load lại scene
                myNotes.add(newNote);
                initMyNotesScene(myNotes);
            }
        });
    }
    
    /**
     * Xử lý sự kiện khi ấn vào Open Note Button
     * @param event 
     */
    @FXML
    void handleOpenNoteButton(ActionEvent event) {
        //Lấy list các header note
        List<String> myNotesHeader = new ArrayList<>();  
        for(Note myNote: myNotes){
            myNotesHeader.add(myNote.getHeader());
        }
        //Hiện dialog để chọn note cần mở
        ChoiceDialog<String> dialog = new ChoiceDialog<>(myNotesHeader.get(0), myNotesHeader);
        dialog.setTitle("Open your note");
        dialog.setHeaderText("Choose note");
        //Lấy kết quả
        Optional<String> confirm = dialog.showAndWait();
        //Xử lý kết quả khi nhấn OK
        confirm.ifPresent(selectedHeader -> {
            //Mở Note được chọn
            try {
                openNote(user.getId(), selectedHeader);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //Nhận reply từ server và thông báo
            if(replyFromServer.equals("Not exist")) {
                showAlert(Alert.AlertType.ERROR, replyFromServer);
            } else {
                //Lấy thông tin note được mở
                myNote = Note.valueOf(replyFromServer);
                //Load lại Edit Scene và mở Edit Scene
                initEditScene();
                changeScene(editNoteButton);
            }
        });
    }
    
    /**
     * Xử lý sự kiện khi ấn vào Delete Note Button
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleDeleteNoteButton(ActionEvent event) {
        //Lấy list các header note
        List<String> myNotesHeader = new ArrayList<>();
        for(Note myNote: myNotes){
            myNotesHeader.add(myNote.getHeader());
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
                deleteNote(user.getId(), selectedHeader);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //Nhận reply từ server và thông báo
            if(replyFromServer.equals("Can't delete") || replyFromServer.equals("Not exist")) {
                showAlert(Alert.AlertType.ERROR, replyFromServer);
            } else {
                //Lấy thông tin Note được xóa
                Note deletedNote = Note.valueOf(replyFromServer);
                //Thông báo xóa thành công
                showAlert(Alert.AlertType.INFORMATION, "Successfully create " + deletedNote.getHeader());
                //Lấy index Note được xóa trong list các Note của user
                int deletedIndex = -1;
                for(int i = 0; i < myNotes.size(); i++) {
                    if(Note.toString(myNotes.get(i)).equals(Note.toString(deletedNote))) {
                        deletedIndex = i;
                    }
                }
                //Xóa Note dựa vào index
                myNotes.remove(deletedIndex);  
                //Load lại My Notes Scene
                initMyNotesScene(myNotes);
            }
        });
    }
    
    /**
     * Xử lý sự kiện khi ấn vào My Account Button
     * @param event 
     */
    @FXML
    void handleMyAccountButton(ActionEvent event) {
        //Chuyển scene sang My Account Scene
        changeScene(myAccountButton);
        //Load My Account Scene
        initMyAccountScene();
    }
    
    /**
     * Xử lý sự kiện khi ấn vào Save Account Button
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleSaveAccountButton(ActionEvent event) throws IOException {
        //Lấy dữ liệu của user vừa được chỉnh sửa
        user.setPassword(passwordField.getText());
        user.setName(nameField.getText());
        user.setBirthday(Date.valueOf(birthdayField.getText()));
        user.setSchool(schoolField.getText());
        //Cập nhật Account
        updateAccount(user);
        //Nhận reply từ server và thông báo
        if(replyFromServer.equals("Can't update")) {
            showAlert(Alert.AlertType.ERROR, replyFromServer);
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Successfully save for " + user.getUsername());
        }
    }
    
    /**
     * Xử lý sự kiện khi ấn vào Change Password Button
     * @param event 
     */
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
            if(password.equals(user.getPassword())) {
                passwordField.setEditable(true);
            }
        });      
    }
    
    /**
     * Xử lý sự kiện khi ấn vào Import/Export Button
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleImportExportButton(ActionEvent event) throws IOException {
        //Chuyển sang Import/Export Scene
        changeScene(importExportButton);
        //Lấy tất cả các note của user
        getAllNotes(user.getId());
        //Nhận reply từ server
        if(!replyFromServer.equals("Not found")) {
            //Thêm các note vào list
            String[] datas = replyFromServer.split(":::");      
            myNotes = new ArrayList<>();
            for(String element: datas) {
                myNotes.add(Note.valueOf(element));
            }
            //Load Import/Export Scene
            initImportExportScene(myNotes);
        }
    }
    
    /**
     * Xử lý sự kiện khi ấn vào Export File Button
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleExportFileButton(ActionEvent event) throws IOException {
        //Lấy header được chọn từ ComboBox
        String selectedNoteHeader = exportNoteComboBox.getSelectionModel().getSelectedItem();
        //Lấy dữ liệu từ note được chọn
        openNote(user.getId(), selectedNoteHeader);
        Note selectedNote = Note.valueOf(replyFromServer);
        //Lấy format để export từ ComboBox
        String selectedFormat = exportFormatComboBox.getSelectionModel().getSelectedItem();
        //Export file
        PdfService.export(selectedNoteHeader + ".pdf", selectedNote.getContent());
        //Thông báo
        showAlert(Alert.AlertType.INFORMATION, "Succesfully export");
    }
    
    /**
     * Xử lý sự kiện khi ấn vào Import File Button
     * @param event 
     */
    @FXML
    void handleImportFileButton(ActionEvent event) {
        //Lấy dữ liệu từ PDF và chuyển vào content
        myNote.setContent(PdfService.read(importFileName.getText(), 1));
        //Load lại Edit Scene
        initEditScene();
        //Thông báo
        showAlert(Alert.AlertType.INFORMATION, "Succesfully import");
    }
    
    /**
     * Xử lý sự kiện khi ấn vào Choose Input File Button
     * @param event 
     */
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
    
    /**
     * Chạy Dashboard và thiết lập các thuộc tính ban đầu
     * @throws IOException 
     */
    public void run() throws IOException {  
        //Chuyển sang scene Edit Note và thiết lập các thông tin user
        userLabel.setText(user.getName());
        noteHeaderField.setVisible(false);     
        changeScene(editNoteButton);
        //Mở Note thao tác gần nhất
        openLastNote(user.getId());
        //Nhận reply từ server và xử lý
        if(replyFromServer.equals("Not exist")) {
            //Nếu user chưa có note nào thì tạo một note mới tạm thời
            myNote = new Note();
            myNote.setUserId(user.getId());
            myNote.setHeader("New Note");
            myNote.setContent("Edit here");
            List<String> filters = new ArrayList<>();
            filters.add("text");
            myNote.setFilters(filters);
        } else {
            //Lấy dữ liệu note gần nhất
            myNote = Note.valueOf(replyFromServer);
        }
        //Load lại Edit Scene
        initEditScene();
    }
    
    /**
     * Load Edit Scene
     */
    private void initEditScene() { 
        //Thiết lập content, header
        contentArea.setText(myNote.getContent());
        noteHeaderLabel.setText(myNote.getHeader());
        numCharLabel.setText(String.valueOf(contentArea.getText().length()) + "/10000");
        //Thiết lập undoRedoService và lưu vào văn bản đầu tiên
        undoRedoService = new UndoRedoService();
        undoRedoService.saveText(contentArea.getText()); 
        //Load lại Filter GUI
        loadFilter(myNote.getFilters(), 6);   
    }
    
    /**
     * Load MyNotes Scene
     * @param notes list các note muốn hiện
     */
    private void initMyNotesScene(List<Note> notes) {        
        //Làm sạch layout và thêm vào hàng tiêu đề
        Node first = noteCardLayout.getChildren().getFirst();
        noteCardLayout.getChildren().clear();
        noteCardLayout.getChildren().add(first);
        //Load các Note Card
        for(int i=0; i < notes.size(); i++) {
            //Load Note Card Layout
            FXMLLoader fXMLLoader = new FXMLLoader();
            fXMLLoader.setLocation(getClass().getResource("NoteCardFXML.fxml"));
            try {
                HBox hbox = fXMLLoader.load();
                //Thiết lập dữ liệu cho Note Card
                NoteCardFXMLController noteCardFXMLController = fXMLLoader.getController();
                noteCardFXMLController.setData(notes.get(i));
                //Thêm Note Card vào layout
                noteCardLayout.getChildren().add(hbox);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }      
    }
    
    /**
     * Load My Account Scene
     */
    private void initMyAccountScene() {
        //Thiết lập các thuộc tính
        usernameLabel.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        userIdLabel.setText(String.valueOf(user.getId()));
        nameField.setText(user.getName());
        birthdayField.setText(String.valueOf(user.getBirthday()));
        schoolField.setText(user.getSchool());
    }
    
    /**
     * Load Import/Export Scene
     * @param notes 
     */
    private void initImportExportScene(List<Note> notes) {
        //Clear ComboBox và thêm vào các header note trong list
        exportNoteComboBox.getItems().clear();
        for(Note note: notes) {
            exportNoteComboBox.getItems().add(note.getHeader());
        }
        //Clear ComboBox và thêm vào các định dạng cho phép
        exportFormatComboBox.getItems().clear();
        exportFormatComboBox.getItems().add("PDF");
        //Set header của note đang edit
        importNoteName.setText(myNote.getHeader());
    }

    /**
     * Thiết lập thể hiện của filter tới người dùng
     * @param filters List chứa các filter
     * @param maxColumn số lượng filter lớn nhất trên một hàng
     */
    private void loadFilter(List<String> filters, int maxColumn) {
        int column = 0;
        int row = 0;
        //Làm sạch filter layout
        filterGridLayout.getChildren().clear();
        //Thiết lập khoảng cách giữa các filter
        filterGridLayout.setHgap(8);
        filterGridLayout.setVgap(8);
        //Thiết lập filter layout
        try {
            for(int i = 0; i < filters.size(); i++) {
                //Load filter FXML
                FXMLLoader fXMLLoader = new FXMLLoader();
                fXMLLoader.setLocation(getClass().getResource("FilterFXML.fxml"));
                HBox hbox = fXMLLoader.load();
                //Thiết lập dữ liệu cho filter
                FilterFXMLController filterFXMLController = fXMLLoader.getController();
                filterFXMLController.setData(filters.get(i));
                //Chuyển hàng
                if(column == maxColumn){
                    column = 0;
                    row++;
                }
                //Thêm filter vừa tạo vào layout
                filterGridLayout.add(hbox, column++, row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Chuyển Scene khi ấn vào một Button nào đó
     * @param button 
     */
    private void changeScene(Button button) {
        if(button == editNoteButton) {
            editNoteButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #0e544e, #0e2f52)");
            myNotesButton.setStyle("-fx-background-color: transparent");
            myAccountButton.setStyle("-fx-background-color: transparent");
            importExportButton.setStyle("-fx-background-color: transparent");
            
            editNoteScene.setVisible(true);
            myNotesScene.setVisible(false);
            myAccountScene.setVisible(false);
            importExportScene.setVisible(false);
        } else if (button == myNotesButton) {
            editNoteButton.setStyle("-fx-background-color: transparent");
            myNotesButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #0e544e, #0e2f52)");
            myAccountButton.setStyle("-fx-background-color: transparent");
            importExportButton.setStyle("-fx-background-color: transparent");
            
            editNoteScene.setVisible(false);
            myNotesScene.setVisible(true);
            myAccountScene.setVisible(false);
            importExportScene.setVisible(false);
        } else if (button == myAccountButton) {
            editNoteButton.setStyle("-fx-background-color: transparent");
            myNotesButton.setStyle("-fx-background-color: transparent");
            myAccountButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #0e544e, #0e2f52)");
            importExportButton.setStyle("-fx-background-color: transparent");
            
            editNoteScene.setVisible(false);
            myNotesScene.setVisible(false);
            myAccountScene.setVisible(true);
            importExportScene.setVisible(false);
        } else if (button == importExportButton) {
            editNoteButton.setStyle("-fx-background-color: transparent");
            myNotesButton.setStyle("-fx-background-color: transparent");
            myAccountButton.setStyle("-fx-background-color: transparent");
            importExportButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #0e544e, #0e2f52)");
            
            editNoteScene.setVisible(false);
            myNotesScene.setVisible(false);
            myAccountScene.setVisible(false);
            importExportScene.setVisible(true);
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
    
    /**
     * Lưu Note
     * @param note note cần lưu
     * @throws IOException 
     */
    private void saveNote(Note note) throws IOException {
        //Tạo Request
        String serviceName = "SaveNote";
        String data = Note.toString(note);
        requestCommand = new RequestCommand(serviceName, data);
        //Gửi request và nhận reply từ server
        replyFromServer = ClientRequest.request(requestCommand);
    }
    
    /**
     * Lấy tất cả các note của user
     * @param userId id của user
     * @throws IOException 
     */
    private void getAllNotes(int userId) throws IOException {
        //Tạo Request
        String serviceName = "GetAllNotes";
        String data = String.valueOf(userId);
        requestCommand = new RequestCommand(serviceName, data);
        //Gửi request và nhận reply từ server
        replyFromServer = ClientRequest.request(requestCommand);
    }
    
    /**
     * Tạo một Note mới
     * @param newNote Note mới
     * @throws IOException 
     */
    private void createNote(Note newNote) throws IOException {
        //Tạo Request
        String serviceName = "CreateNote";
        String data = Note.toString(newNote);
        requestCommand = new RequestCommand(serviceName, data);
        //Gửi request và nhận reply từ server
        replyFromServer = ClientRequest.request(requestCommand);
    }
    
    /**
     * Mở một note
     * @param userId id của user
     * @param header header của note cần mở
     * @throws IOException 
     */
    private void openNote(int userId, String header) throws IOException {
        //Tạo Request
        String serviceName = "OpenNote";
        String data = userId + ";;;" + header;
        requestCommand = new RequestCommand(serviceName, data);
        //Gửi request và nhận reply từ server
        replyFromServer = ClientRequest.request(requestCommand);
    }
    
    /**
     * Xóa một note
     * @param userId id của user
     * @param header header của note cần xóa
     * @throws IOException 
     */
    private void deleteNote(int userId, String header) throws IOException {
        //Tạo Request
        String serviceName = "DeleteNote";
        String data = userId + ";;;" + header;
        requestCommand = new RequestCommand(serviceName, data);
        //Gửi request và nhận reply từ server
        replyFromServer = ClientRequest.request(requestCommand);
    }
    
    /**
     * Mở Note được chỉnh sửa gần nhất
     * @param userId id của user
     * @throws IOException 
     */
    private void openLastNote(int userId) throws IOException {
        //Tạo Request
        String serviceName = "OpenLastNote";
        String data = String.valueOf(userId);
        requestCommand = new RequestCommand(serviceName, data);
        //Gửi request và nhận reply từ server
        replyFromServer = ClientRequest.request(requestCommand);
    }
    
    /**
     * Cập nhật Account
     * @param user user cần cập nhật
     * @throws IOException 
     */
    private void updateAccount(User user) throws IOException {
        //Tạo Request
        String serviceName = "UpdateAccount";
        String data = User.toString(user);
        requestCommand = new RequestCommand(serviceName, data);
        //Gửi request và nhận reply từ server
        replyFromServer = ClientRequest.request(requestCommand);
    }
}
