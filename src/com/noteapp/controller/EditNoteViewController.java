package com.noteapp.controller;

import com.noteapp.dataaccess.DataAccessException;
import com.noteapp.model.datatransfer.Note;
import com.noteapp.model.datatransfer.NoteBlock;
import com.noteapp.model.datatransfer.NoteFilter;
import com.noteapp.model.datatransfer.User;
import com.noteapp.service.GetAllNotesService;
import com.noteapp.service.OpenLastNoteService;
import com.noteapp.service.OpenNoteService;
import com.noteapp.service.SaveNoteService;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


/**
 * FXML Controller class
 *
 * @author admin
 */
public class EditNoteViewController extends Controller {
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
    private Button addFilterButton;
    @FXML
    private Button addTextBlockButton;
    @FXML
    private ComboBox<String> fontTypeComboBox; 
    @FXML
    private ComboBox<String> fontSizeComboBox;
    @FXML
    private ColorPicker colorPicker;
    //Các thuộc tính còn lại
    @FXML
    private VBox blocksLayout;
    @FXML
    private GridPane filterGridLayout;
    @FXML
    private Button closeButton;
    
    private User myUser;
    private Note myNote;
    private List<BlockController> myNoteBlockControllers;
    private Map<String, List<NoteBlock>> blockByHeaders;
    private Timer updateTimer;
    private TimerTask updateTask;

    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }
    
    public void setMyNote(Note myNote) {
        this.myNote = myNote;
    }
    
    @Override
    public void init() {
        myNoteBlockControllers = new ArrayList<>();
        initView();
        closeButton.setOnAction((ActionEvent event) -> {
            close();
        });
        homeMenuButton.setOnAction((ActionEvent event) -> {
            openDashboard(myUser);
        });
        noteHeaderLabel.setOnMouseClicked((MouseEvent event) -> {
            changeHeaderLabel();
        });
        saveNoteButton.setOnAction((ActionEvent event) -> {
            saveMyNote();
        });
        addFilterButton.setOnAction((ActionEvent event) -> {
            addFilter();
        });
        addTextBlockButton.setOnAction((ActionEvent event) -> {
            NoteBlock newBlock = new NoteBlock();
            newBlock.setOrd(myNoteBlockControllers.size() + 1);
            newBlock.setHeader("Block " + newBlock.getOrd() + " of " + myNote.getHeader());
            newBlock.setEditor(myUser.getUsername());
            newBlock.setContent("Edit here");
            addBlock(newBlock);
        });
    }
    
    protected void initView() {       
        if(myNote.isDefaultValue()) {
            try {
                noteService = new OpenLastNoteService(myUser.getUsername());
                myNote = noteService.execute();
            } catch (DataAccessException ex) {
                myNote = new Note();
                myNote.setAuthor(myUser.getUsername());
                myNote.setHeader("NewNote");
            }
        }
        noteHeaderLabel.setText(myNote.getHeader());
        loadFilter(myNote.getFilters(), 8);
        blocksLayout.getChildren().clear();
        List<NoteBlock> blocks = myNote.getBlocks();
        blockByHeaders = new HashMap<>();
        
        for(int i=0; i<blocks.size(); i++) {
            NoteBlock newBlock = blocks.get(i);
            if(!blockByHeaders.containsKey(newBlock.getHeader())) {
                blockByHeaders.put(newBlock.getHeader(), new ArrayList<>());
                
            }
            blockByHeaders.get(newBlock.getHeader()).add(newBlock);
        }
        for(int i=0; i<blocks.size(); i++) {
            NoteBlock newBlock = blocks.get(i);
            if(newBlock.getEditor().equals(myUser.getUsername())) {
                addBlock(newBlock);
            }
        }
    }
    
    public void setOnAutoUpdate() {
        updateTimer = new Timer();
        updateTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        noteService = new OpenNoteService(myNote.getId());
                        myNote = noteService.execute();
                        noteHeaderLabel.setText(myNote.getHeader());
                        loadFilter(myNote.getFilters(), 8);
                        List<NoteBlock> blocks = myNote.getBlocks();
                        blockByHeaders = new HashMap<>();
                        for(int i=0; i<blocks.size(); i++) {
                            NoteBlock newBlock = blocks.get(i);
                            if(!blockByHeaders.containsKey(newBlock.getHeader())) {
                                blockByHeaders.put(newBlock.getHeader(), new ArrayList<>());
                            }
                            blockByHeaders.get(newBlock.getHeader()).add(newBlock);
                        }
                        updateBlock();
                        System.out.println(System.currentTimeMillis());
                    } catch (DataAccessException ex) {
                        System.err.println(ex.getMessage());
                    }
                });
            }
        };
        updateTimer.scheduleAtFixedRate(updateTask, 2000, 4000);
    }
    
    @Override
    protected void close() {
        //saveMyNote();
        super.close();
    }
    
    protected void changeHeaderLabel() {
        //Mở dialog
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Change header for " + myNote.getHeader());
        inputDialog.setHeaderText("Input your new header");
        //Lấy kết quả và xử lý
        Optional<String> confirm = inputDialog.showAndWait();
        confirm.ifPresent(newNoteHeader -> {
            //Lấy tất cả các Note của myUser
            try { 
                noteCollectionService = new GetAllNotesService(myUser.getUsername());
                //Lấy thành công
                List<Note> myNotes = noteCollectionService.execute();
                for(Note note: myNotes) {
                    if(note.getHeader().equals(newNoteHeader)) {
                        showAlert(Alert.AlertType.ERROR, "This header exist");
                        return;
                    }
                }
                //Thiết lập note name vừa nhập cho Label   
                noteHeaderLabel.setText(newNoteHeader);
            } catch (DataAccessException ex) {
                showAlert(Alert.AlertType.ERROR, ex.getMessage());
            }
        });
    }
    
    protected void saveMyNote() {
        myNote.setHeader(noteHeaderLabel.getText());
        myNote.setLastModified(1);
        myNote.setLastModifiedDate(Date.valueOf(LocalDate.now()));
        myNote.getBlocks().clear();
        for(int i=0; i<myNoteBlockControllers.size(); i++) {
            NoteBlock block = myNoteBlockControllers.get(i).getNoteBlock();
            block.setContent(myNoteBlockControllers.get(i).getTextFromView());
            block.setOrd(i+1);
            myNote.getBlocks().add(block);
        }
        for(String blockHeader: blockByHeaders.keySet()) {
            for(NoteBlock block: blockByHeaders.get(blockHeader)) {
                if(!block.getEditor().equals(myUser.getUsername())) {
                    myNote.getBlocks().add(block);
                }
            }
        }
        try {
            noteService = new SaveNoteService(myNote);
            noteService.execute();
            showAlert(Alert.AlertType.INFORMATION, "Successfully save for " + myNote.getHeader());
        } catch (DataAccessException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    protected void addBlock(NoteBlock newBlock) {
        try {
            FXMLLoader loader = new FXMLLoader();
            String blockViewPath = "../view/BlockView.fxml";
            loader.setLocation(getClass().getResource(blockViewPath));
            
            VBox box = loader.load();
            BlockController controller = loader.getController();
            controller.init();
            controller.setNoteBlock(newBlock);
            controller.setTextForView(NoteBlock.TextContentConverter.convertToObjectText(newBlock.getContent()));
            controller.setNoteId(myNote.getId());
            controller.setHeader(newBlock.getHeader());
            
            controller.getDeleteButton().setOnAction((ActionEvent event) -> {
                int idxToDelete = controller.getNoteBlock().getOrd()-1;
                blocksLayout.getChildren().remove(idxToDelete);
                myNote.getBlocks().remove(idxToDelete);
                myNoteBlockControllers.remove(idxToDelete);
            });
            
            List<String> otherEditors = new ArrayList<>();
            for(NoteBlock noteBlock: blockByHeaders.get(newBlock.getHeader())) {
                if(!noteBlock.getEditor().equals(newBlock.getEditor())) {
                    otherEditors.add(noteBlock.getEditor());
                }
            }
            controller.setOtherEditors(otherEditors);
            controller.initOtherEditComboBox();
            controller.getSwitchToOther().setOnAction((ActionEvent event) -> {
                String otherEditor = controller.getOtherEditor();
                System.out.println(otherEditor);
                NoteBlock otherNoteBlock = new NoteBlock();
                for(NoteBlock noteBlock: blockByHeaders.get(newBlock.getHeader())) {
                    if(noteBlock.getEditor().equals(otherEditor)) {
                        otherNoteBlock = noteBlock;
                    }
                }
                controller.setTextForArea(NoteBlock.TextContentConverter.convertToObjectText(otherNoteBlock.getContent()));
                controller.saveEditedText();
                controller.initOtherEditComboBox();
            });
             
            blocksLayout.getChildren().add(box);
            myNoteBlockControllers.add(controller);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    protected void updateBlock() {
        for(int i=0; i<myNoteBlockControllers.size(); i++) {
            NoteBlock thisBlock = myNoteBlockControllers.get(i).getNoteBlock();
            List<String> otherEditors = new ArrayList<>();
            for(NoteBlock noteBlock: blockByHeaders.get(thisBlock.getHeader())) {
                if(!noteBlock.getEditor().equals(thisBlock.getEditor())) {
                    otherEditors.add(noteBlock.getEditor());
                }
            }
            myNoteBlockControllers.get(i).setOtherEditors(otherEditors);
            myNoteBlockControllers.get(i).initOtherEditComboBox();
        }
    }
    
    protected void addFilter() {
        //Hiện Dialog để nhập filter mới
        TextInputDialog inputDialog = new TextInputDialog();        
        inputDialog.setTitle("Add filter for " + myNote.getHeader());
        inputDialog.setHeaderText("Enter your new filter");   
        //Lấy kết quả
        Optional<String> confirm = inputDialog.showAndWait();
        //Xử lý kết quả khi nhấn OK
        confirm.ifPresent(newFilterStr -> {
            NoteFilter newFilter = new NoteFilter(newFilterStr);
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
        }); 
    }
    
    protected void loadFilter(List<NoteFilter> filters, int maxColEachRow) {
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
                String filterFXMLPath = "../view/NoteFiltersView.fxml";
                fXMLLoader.setLocation(getClass().getResource(filterFXMLPath));
                HBox hbox = fXMLLoader.load();
                //Thiết lập dữ liệu cho filter
                NoteFiltersController filterFXMLController = fXMLLoader.getController();
                filterFXMLController.setData(filters.get(i).getFilterContent());
                filterFXMLController.getRemoveFilterView().setOnMouseClicked(event -> {
                    myNote.getFilters().remove(new NoteFilter(filterFXMLController.getFilter()));
                    loadFilter(myNote.getFilters(), 8);
                });
                //Chuyển hàng
                if(column == maxColEachRow){
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
    
    protected void openDashboard(User user) {
        updateTask.cancel();
        updateTimer.cancel();
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            String dashboardViewPath = "../view/DashboardView.fxml";
            fXMLLoader.setLocation(getClass().getResource(dashboardViewPath));

            scene = new Scene(fXMLLoader.load());

            DashboardController controller = fXMLLoader.getController();
            controller.setStage(stage);
            controller.setMyUser(myUser);
            controller.setCurrentNote(myNote);
            controller.init();

            setSceneMoveable();

            stage.setScene(scene);
        } catch (IOException ex) {
        showAlert(Alert.AlertType.ERROR, "Can't open dashboard");
        }
    }
}
