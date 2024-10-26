package com.noteapp.controller;

import com.noteapp.model.datatransfer.NoteBlock;
import java.util.List;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 *
 * @author admin
 */
public class BlockController extends Controller {
    @FXML
    private Label viewText;
    @FXML
    private TextArea editableText;
    @FXML
    private Button editButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button setToDefaultButton;
    @FXML
    private ComboBox<String> otherEditComboBox;
    @FXML
    private Label blockHeader;
    @FXML
    private Button returnToYoursButton;
    @FXML
    private Button switchToOtherButton;
    
    private int noteId;
    private NoteBlock noteBlock;
    private boolean isEditing;
    private List<String> otherEditors;
    
    @Override
    public void init() {
        noteId = -1;
        noteBlock = new NoteBlock();
        isEditing = false;
        
        editButton.setOnAction((ActionEvent event) -> {
            switchToEditableStatus();
        });
        
        saveButton.setOnAction((ActionEvent event) -> {
            saveEditedText();
        });
        returnToYoursButton.setOnAction((ActionEvent event) -> {
            returnToYourContent();
        });
        setToDefaultButton.setOnAction((ActionEvent event) -> {
            noteBlock.setContent(viewText.getText());
        });
        otherEditComboBox.setPromptText("Other edit by");
    }
            
    public void initOtherEditComboBox() {
        otherEditComboBox.getItems().clear();
        otherEditComboBox.setPromptText("Other edit by");
        if(otherEditors.isEmpty()) {
            return;
        }
        for(String otherEditor: otherEditors) {
            otherEditComboBox.getItems().add(otherEditor);
        }
    }

    public boolean getIsEditing() {
        return isEditing;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }
    
    public Button getDeleteButton() {
        return deleteButton;
    }
    
    public Button getSetToDefaultButton() {
        return setToDefaultButton;
    }
    
    public Button getSwitchToOther() {
        return switchToOtherButton;
    } 

    public int getNoteId() {
        return noteId;
    }

    public NoteBlock getNoteBlock() {
        return noteBlock;
    }
    
    public String getOtherEditor() {
        return otherEditComboBox.getSelectionModel().getSelectedItem();
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
        
    public void setHeader(String blockHeader) {
        this.blockHeader.setText(blockHeader);
    } 

    public void setNoteBlock(NoteBlock noteBlock) {
        this.noteBlock = noteBlock;
    }
    
    public void setOtherEditors(List<String> otherEditors) {
        this.otherEditors = otherEditors;
    }
    
    public String getTextFromView() {
        return viewText.getText();
    }
    
    public String getTextFromArea() {
        return editableText.getText();
    }
    
    public void setTextForView(String text) {
        viewText.setText(text);
    }
    
    public void setTextForArea(String text) {
        editableText.setText(text);
    }
    
    public void switchToEditableStatus() {
        viewText.setVisible(false);
        editableText.setVisible(true);
        isEditing = true;
        
        editableText.setText(viewText.getText());
        editableText.setPrefHeight(viewText.getPrefHeight());
        editableText.setPrefRowCount(viewText.getText().split("\n").length);
    }
    
    public void saveEditedText() {
        editableText.setVisible(false);
        viewText.setVisible(true);
        isEditing = false;
        
        viewText.setText(editableText.getText());
        viewText.setPrefHeight(Region.USE_COMPUTED_SIZE);
    }
    
    public void returnToYourContent() {
        editableText.setText(NoteBlock.TextContentConverter.convertToObjectText(noteBlock.getContent()));
        saveEditedText();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.noteBlock);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BlockController other = (BlockController) obj;
        return Objects.equals(this.noteBlock, other.noteBlock);
    }
}