package com.noteliteclient.controller;

import com.notelitemodel.datatransfer.Note;
import com.notelitemodel.datatransfer.ShareNote;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * FXML Controller class cho các Note Card
 *
 * @author Nhóm 23
 * @since 31/03/2024
 * @version 1.0
 */
public class NoteCardController {  
    //Các thuộc tính FXML
    @FXML
    private Label header;
    @FXML
    private Label lastModifiedDate;
    @FXML
    private Label author;
    @FXML
    private Label filtersOrShareType;
    
    public String getHeader() {
        return header.getText();
    }
    
    public ShareNote.ShareType getShareType() {
        return ShareNote.ShareType.valueOf(filtersOrShareType.getText());
    }
    
    public String getAuthor() {
        return author.getText();
    }
    
    /**
     * Thiết lập dữ liệu cho Note Card
     * @param note note chứa dữ liệu cần chuyển vào Note Card
     */
    public void setData(Note note) {
        header.setText(note.getHeader());
        lastModifiedDate.setText(String.valueOf(note.getLastModifiedDate()));
        author.setText(note.getAuthor());
        String filtersString = "";
        for(String filter: note.getFilters()) {
            filtersString += filter + ", ";
        }
        if(filtersString.isEmpty()) {
            filtersOrShareType.setText(filtersString);
        } else {
            filtersOrShareType.setText(filtersString.substring(0, filtersString.length() - 2));
        }
    }
    
    /**
     * Thiết lập dữ liệu cho Note Card
     * @param shareNote shareNote chứa dữ liệu cần chuyển
     */
    public void setData(ShareNote shareNote) {
        header.setText(shareNote.getHeader());
        lastModifiedDate.setText(String.valueOf(shareNote.getLastModifiedDate()));
        author.setText(shareNote.getAuthor());
        filtersOrShareType.setText(shareNote.getShareType().toString());
    }
}