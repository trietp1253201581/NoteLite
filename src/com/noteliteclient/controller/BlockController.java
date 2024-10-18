package com.noteliteclient.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 *
 * @author admin
 */
public class BlockController {
    @FXML
    private Label viewText;
    @FXML
    private TextArea editableText;
    @FXML
    private Button editButton;
    @FXML
    private Button saveButton;
    
    private int blockId;
    
    public void changeEditableStatus(boolean editable) {
        viewText.setVisible(!editable);
        editableText.setVisible(editable);
    }
    
    public String getTextFromTextArea() {
        return editableText.getText();
    }
    
    public void setTextForTextArea(String str) {
        editableText.setText(str);
    }
    
    public void setText(String str) {
        viewText.setText(str);
    }
    
    public String getText() {
        return viewText.getText();
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }
}
