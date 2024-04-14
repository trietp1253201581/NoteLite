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
import model.User;
import client.service.ClientServerService;
import client.service.ClientServerServiceErrorType;
import client.service.PdfService;
import java.io.File;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import model.ShareNote;
import model.ShareType;

/**
 * FXML Controller class cho Dashboard GUI
 * 
 * @author Lê Minh Triết
 * @since 07/04/2024
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
    @FXML
    private Button shareNoteButton;
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
    private TableView<ShareNote> sharedByOtherTable;
    @FXML
    private TableColumn<ShareNote, String> senderUsernameColumn;
    @FXML
    private TableColumn<ShareNote, String> headerColumn;
    @FXML
    private TableColumn<ShareNote, String> shareTypeColumn;
    @FXML
    private Button openShareNoteButton;
    
    private UndoRedoService undoRedoService;
    private User user;   
    private Note myNote;    
    private List<Note> myNotes;   
    private String replyFromServer;
    private ObservableList<ShareNote> mySharedNotes;

    public void setUser(User user) {
        this.user = user;
    }
    
    public void setMyNote(Note myNote) {
        this.myNote = myNote;
    }

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
            System.err.println(ex);
        }
    }

    @FXML
    void handleEditNoteButton(ActionEvent event) {  
        //Chuyển sang scene edit
        changeScene(editNoteButton);
    }
    
    @FXML
    void handleSaveButton(ActionEvent event) {  
        //Thiết lập lại undoRedoService
        undoRedoService = new UndoRedoService();    
        undoRedoService.saveText(contentArea.getText());
        //Set dữ liệu gần nhất cho myNote
        myNote.setHeader(noteHeaderLabel.getText());
        myNote.setContent(contentArea.getText());
        myNote.setLastModified(1);
        myNote.setLastModifiedDate(Date.valueOf(LocalDate.now()));
        //Lưu note
        replyFromServer = ClientServerService.saveNote(myNote);
        //Nhận reply và thông báo
        if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't save this note");
        } else if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't connect to server");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Successfully save for " + myNote.getHeader());
        }
    }

    @FXML
    void handleRenameButton(ActionEvent event) {        
        //Thiết lập note name hiện tại cho noteHeaderField
        noteHeaderField.setText(noteHeaderLabel.getText());
        //Ẩn Label và hiện field
        noteHeaderLabel.setVisible(false);
        noteHeaderField.setVisible(true);
    }

    @FXML
    void handleNoteHeaderField(ActionEvent event) {        
        //Thiết lập note name vừa nhập cho Label
        noteHeaderLabel.setText(noteHeaderField.getText());
        //Ẩn field và hiện Label
        noteHeaderField.setVisible(false);
        noteHeaderLabel.setVisible(true);
    }

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

    @FXML
    void handleMyNotesButton(ActionEvent event) {        
        //Chuyển sang scene My Notes
        changeScene(myNotesButton);
        //Lấy tất cả các Note của user
        replyFromServer = ClientServerService.getAllNotes(user.getUsername());
        //Dựa vào reply từ server để xử lý
        if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't connect to server");
        } else if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't open your notes");
        } else if(ClientServerServiceErrorType.NOT_EXISTS.toString().equals(replyFromServer)) {
            //Do nothing
        } else {
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
            newNote.setAuthor(user.getUsername());
            newNote.setHeader(selectedHeader);
            newNote.setContent("Edit here");
            newNote.setLastModifiedDate(Date.valueOf(LocalDate.now()));
            List<String> filters = new ArrayList<>();
            filters.add("text");
            newNote.setFilters(filters);
            //Tạo Note mới
            replyFromServer = ClientServerService.createNote(newNote);
            //Nhận reply từ server và thông báo
            if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(replyFromServer)) {
                showAlert(Alert.AlertType.ERROR, "Can't connect to server");
            } else if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(replyFromServer)) {
                showAlert(Alert.AlertType.ERROR, "Can't create new note");
            } else if(ClientServerServiceErrorType.ALREADY_EXISTS.toString().equals(replyFromServer)) {
                showAlert(Alert.AlertType.ERROR, "This note already exists");
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
            replyFromServer = ClientServerService.deleteNote(user.getUsername(), selectedHeader);
            //Nhận reply từ server và thông báo
            if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(replyFromServer)) {
                showAlert(Alert.AlertType.ERROR, "Can't connect to server");
            } else if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(replyFromServer)) {
                showAlert(Alert.AlertType.ERROR, "Can't delete this note");
            } else if(ClientServerServiceErrorType.NOT_EXISTS.toString().equals(replyFromServer)) {
                showAlert(Alert.AlertType.ERROR, "This note not exists");
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

    @FXML
    void handleMyAccountButton(ActionEvent event) {
        //Chuyển scene sang My Account Scene
        changeScene(myAccountButton);
        //Load My Account Scene
        initMyAccountScene();
    }

    @FXML
    void handleSaveAccountButton(ActionEvent event) {
        //Lấy dữ liệu của user vừa được chỉnh sửa
        user.setPassword(passwordField.getText());
        user.setName(nameField.getText());
        user.setBirthday(Date.valueOf(birthdayField.getText()));
        user.setSchool(schoolField.getText());
        //Cập nhật User
        replyFromServer = ClientServerService.updateUser(user);
        //Nhận reply từ server và thông báo
        if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't connect to server");
        } else if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't update your user");
        } else if(ClientServerServiceErrorType.NOT_EXISTS.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "User not exists");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Successfully update for " + user.getUsername());
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
            if(password.equals(user.getPassword())) {
                passwordField.setEditable(true);
            }
        });      
    }

    @FXML
    void handleImportExportButton(ActionEvent event) {
        //Chuyển sang Import/Export Scene
        changeScene(importExportButton);
        //Lấy tất cả các note của user
        replyFromServer = ClientServerService.getAllNotes(user.getUsername());
        //Nhận reply từ server
        if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't connect to server");
        } else if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't open your notes");
        } else if(ClientServerServiceErrorType.NOT_EXISTS.toString().equals(replyFromServer)) {
            //Do nothing
        } else {
            //Thêm các note vào list các note của user
            String[] datas = replyFromServer.split(":::");      
            myNotes = new ArrayList<>();
            for(String element: datas) {
                myNotes.add(Note.valueOf(element));
            }
            //Thiết lập lại My Notes scene
            initImportExportScene(myNotes);
        }
    }

    @FXML
    void handleExportFileButton(ActionEvent event) {
        //Lấy header được chọn từ ComboBox
        String selectedNoteHeader = exportNoteComboBox.getSelectionModel().getSelectedItem();
        //Lấy dữ liệu từ note được chọn
        replyFromServer = ClientServerService.openNote(user.getUsername(), selectedNoteHeader);
        if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't connect to server");
        } else if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't open this note");
        } else if(ClientServerServiceErrorType.NOT_EXISTS.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "This note not exists");
        } else {
            Note selectedNote = Note.valueOf(replyFromServer);
            //Lấy format để export từ ComboBox
            String selectedFormat = exportFormatComboBox.getSelectionModel().getSelectedItem();
            //Export file
            PdfService.export(selectedNoteHeader + ".pdf", selectedNote.getContent());
            //Thông báo
            showAlert(Alert.AlertType.INFORMATION, "Succesfully export");
        }       
    }

    @FXML
    void handleImportFileButton(ActionEvent event) {
        //Lấy dữ liệu từ PDF và chuyển vào content
        myNote.setContent(PdfService.read(importFileName.getText(), 1));
        //Load lại Edit Scene
        initEditScene();
        //Thông báo
        showAlert(Alert.AlertType.INFORMATION, "Succesfully import");
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
        changeScene(shareNoteButton);
        //Lấy tất cả các note của user
        replyFromServer = ClientServerService.getAllNotes(user.getUsername());
        //Nhận reply từ server
        if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't connect to server");
        } else if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't open your notes");
        } else {
            //Thêm các note vào list các note của user
            String[] datas = replyFromServer.split(":::");      
            myNotes = new ArrayList<>();
            for(String element: datas) {
                myNotes.add(Note.valueOf(element));
            }
        }
        //Lấy tất cả các note được share tới user này
        replyFromServer = ClientServerService.getAllReceivedNotes(user.getUsername());
        if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't connect to server");
        } else if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't open your notes");
        } else if(ClientServerServiceErrorType.NOT_EXISTS.toString().equals(replyFromServer)) {
            //Do nothing
        } else {
            //Thêm các note vào list
            String[] datas = replyFromServer.split(":::");      
            mySharedNotes = FXCollections.observableArrayList();
            for(String element: datas) {
                mySharedNotes.add(ShareNote.valueOf(element));
            }
        }
        initShareNoteScene(myNotes, mySharedNotes);
    }
    
    @FXML
    void handleSendNoteButton(ActionEvent event) {
        //Lấy header được chọn từ ComboBox và lấy note tương ứng
        String selectedNoteHeader = chooseShareNoteComboBox.getSelectionModel().getSelectedItem();
        replyFromServer = ClientServerService.openNote(user.getUsername(), selectedNoteHeader);
        if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't connect to server");
        } else if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't open this note");
        } else if(ClientServerServiceErrorType.NOT_EXISTS.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "This note not exists");
        } else {
            //Lấy receiver Id
            String receiverUsename = chooseUserShareField.getText();
            //Share Note
            ShareNote newShareNote = new ShareNote();
            newShareNote.setNote(Note.valueOf(replyFromServer));
            newShareNote.setReceiver(receiverUsename);
            if(shareTypeReadOnly.isSelected()) {
                newShareNote.setShareType(ShareType.READ_ONLY);
            } else {
                newShareNote.setShareType(ShareType.CAN_EDIT);
            }
            replyFromServer = ClientServerService.sendNote(newShareNote);
            //Xử lý và thông báo
            if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(replyFromServer)) {
                showAlert(Alert.AlertType.ERROR, "Can't connect to server");
            } else if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(replyFromServer)) {
                showAlert(Alert.AlertType.ERROR, "Can't send this notes");
            } else if(ClientServerServiceErrorType.NOT_EXISTS.toString().equals(replyFromServer)) {
                showAlert(Alert.AlertType.INFORMATION, "You have shared this note to user " + receiverUsename);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Successfully share");
            }
        }
    }
    
    @FXML
    void handleOpenShareNoteButton(ActionEvent event) {
        int num = sharedByOtherTable.getSelectionModel().getSelectedIndex();
        if(num < 0) {
            showAlert(Alert.AlertType.ERROR, "Choose the note you want open");
        } else {
            //Lấy ShareNote được chọn
            ShareNote shareNote = sharedByOtherTable.getSelectionModel().getSelectedItem();
            //Lấy note được chọn
            replyFromServer = ClientServerService.openNote(shareNote.getAuthor(), shareNote.getHeader());
            if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(replyFromServer)) {
                showAlert(Alert.AlertType.ERROR, "Can't connect to server");
            } else if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(replyFromServer)) {
                showAlert(Alert.AlertType.ERROR, "Can't open this note");
            } else if(ClientServerServiceErrorType.NOT_EXISTS.toString().equals(replyFromServer)) {
                showAlert(Alert.AlertType.ERROR, "This note not exists");
            } else {
                //Lấy thông tin note được mở
                myNote = Note.valueOf(replyFromServer);
                //Load lại Edit Scene và mở Edit Scene
                initEditScene();
                //Nếu là ReadOnly thì không được edit
                if(shareNote.getShareType() == ShareType.READ_ONLY) {
                    contentArea.setEditable(false);
                }
                changeScene(editNoteButton);  
            }            
        }
    }
    
    /**
     * Chạy Dashboard và thiết lập các thuộc tính ban đầu
     */
    public void run() {  
        //Chuyển sang scene Edit Note và thiết lập các thông tin user
        userLabel.setText(user.getName());
        noteHeaderField.setVisible(false);     
        changeScene(editNoteButton);
        //Mở Note thao tác gần nhất
        replyFromServer = ClientServerService.openLastNote(user.getUsername());
        //Nhận reply từ server và xử lý
        if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't connect to server");
        } else if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(replyFromServer)) {
            showAlert(Alert.AlertType.ERROR, "Can't open your last note");
        } else if(ClientServerServiceErrorType.NOT_EXISTS.toString().equals(replyFromServer)) {
            //Nếu user chưa có note nào thì tạo một note mới tạm thời
            myNote = new Note();
            myNote.setAuthor(user.getUsername());
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
        contentArea.setEditable(true);
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
        //Làm sạch layout
        noteCardLayout.getChildren().clear();
        noteCardLayout.setSpacing(3);
        //Load các Note Card
        for(int i=0; i < notes.size(); i++) {
            //Load Note Card Layout
            FXMLLoader fXMLLoader = new FXMLLoader();
            fXMLLoader.setLocation(getClass().getResource("NoteCardFXML.fxml"));
            try {
                VBox box = fXMLLoader.load();
                //Thiết lập dữ liệu cho Note Card
                NoteCardFXMLController noteCardFXMLController = fXMLLoader.getController();
                noteCardFXMLController.setData(notes.get(i));
                //Xử lý khi nhấn vào note card
                box.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        noteCardFXMLController.setLabelStyle("-fx-background-color: #3a737a");
                        //Tạo thông báo và mở note nếu chọn OK
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Open Note");
                        alert.setHeaderText("Open " + noteCardFXMLController.getHeader());
                        Optional<ButtonType> optional = alert.showAndWait();
                        if(optional.get() == ButtonType.OK) {
                            replyFromServer = ClientServerService.openNote(user.getUsername(),
                                    noteCardFXMLController.getHeader());
                            //Nhận reply từ server và thông báo
                            if(ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString().equals(replyFromServer)) {
                                showAlert(Alert.AlertType.ERROR, "Can't connect to server");
                            } else if(ClientServerServiceErrorType.CAN_NOT_EXECUTE.toString().equals(replyFromServer)) {
                                showAlert(Alert.AlertType.ERROR, "Can't open this note");
                            } else if(ClientServerServiceErrorType.NOT_EXISTS.toString().equals(replyFromServer)) {
                                showAlert(Alert.AlertType.ERROR, "This note not exists");
                            } else {
                                //Lấy thông tin note được mở
                                myNote = Note.valueOf(replyFromServer);
                                //Load lại Edit Scene và mở Edit Scene
                                initEditScene();
                                changeScene(editNoteButton);
                            }
                        }
                        noteCardFXMLController.setLabelStyle("-fx-background-color: transparent");
                    }
                });

                //Thêm Note Card vào layout
                noteCardLayout.getChildren().add(box);
            } catch (IOException ex) {
                System.err.println(ex);
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
     * Load Share Note Scene
     */
    private void initShareNoteScene(List<Note> notes, ObservableList<ShareNote> shareNotes) {
        //Clear ComboBox và thêm vào các header note trong list
        chooseShareNoteComboBox.getItems().clear();
        for(Note note: notes) {
            chooseShareNoteComboBox.getItems().add(note.getHeader());
        }
        //Set Type mặc định là Read Only
        shareTypeReadOnly.setSelected(true);
        //Clear table và thêm các note được share
        sharedByOtherTable.getItems().clear();
        senderUsernameColumn.setCellValueFactory(new PropertyValueFactory<ShareNote, String>("author"));
        headerColumn.setCellValueFactory(new PropertyValueFactory<ShareNote, String>("header"));
        shareTypeColumn.setCellValueFactory(new PropertyValueFactory<ShareNote, String>("shareType"));
        sharedByOtherTable.setItems(shareNotes);
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
            System.err.println(e);;
        }
    }
    
    /**
     * Chuyển Scene khi ấn vào một Button nào đó
     * @param button 
     */
    private void changeScene(Button button) {
        if(button == editNoteButton) {
            editNoteButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #0e544e, #0e2f52)");
            editNoteScene.setVisible(true);
            
            myNotesButton.setStyle("-fx-background-color: transparent");
            myNotesScene.setVisible(false);
            
            myAccountButton.setStyle("-fx-background-color: transparent");
            myAccountScene.setVisible(false);
            
            importExportButton.setStyle("-fx-background-color: transparent");
            importExportScene.setVisible(false);
            
            shareNoteButton.setStyle("-fx-background-color: transparent");
            shareNoteScene.setVisible(false);
        } else if (button == myNotesButton) {
            editNoteButton.setStyle("-fx-background-color: transparent");
            editNoteScene.setVisible(false);
            
            myNotesButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #0e544e, #0e2f52)");
            myNotesScene.setVisible(true);
            
            myAccountButton.setStyle("-fx-background-color: transparent");
            myAccountScene.setVisible(false);
            
            importExportButton.setStyle("-fx-background-color: transparent");
            importExportScene.setVisible(false);
            
            shareNoteButton.setStyle("-fx-background-color: transparent");
            shareNoteScene.setVisible(false);
        } else if (button == myAccountButton) {
            editNoteButton.setStyle("-fx-background-color: transparent");
            editNoteScene.setVisible(false);
            
            myNotesButton.setStyle("-fx-background-color: transparent");
            myNotesScene.setVisible(false);
            
            myAccountButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #0e544e, #0e2f52)");
            myAccountScene.setVisible(true);
            
            importExportButton.setStyle("-fx-background-color: transparent");
            importExportScene.setVisible(false);
            
            shareNoteButton.setStyle("-fx-background-color: transparent");
            shareNoteScene.setVisible(false);
        } else if (button == importExportButton) {
            editNoteButton.setStyle("-fx-background-color: transparent");
            editNoteScene.setVisible(false);
            
            myNotesButton.setStyle("-fx-background-color: transparent");
            myNotesScene.setVisible(false);
            
            myAccountButton.setStyle("-fx-background-color: transparent");
            myAccountScene.setVisible(false);
            
            importExportButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #0e544e, #0e2f52)");
            importExportScene.setVisible(true);
            
            shareNoteButton.setStyle("-fx-background-color: transparent");
            shareNoteScene.setVisible(false);
        } else if (button == shareNoteButton) {
            editNoteButton.setStyle("-fx-background-color: transparent");
            editNoteScene.setVisible(false);
            
            myNotesButton.setStyle("-fx-background-color: transparent");
            myNotesScene.setVisible(false);
            
            myAccountButton.setStyle("-fx-background-color: transparent");
            myAccountScene.setVisible(false);
            
            importExportButton.setStyle("-fx-background-color: transparent");
            importExportScene.setVisible(false);
            
            shareNoteButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #0e544e, #0e2f52)");
            shareNoteScene.setVisible(true);
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
