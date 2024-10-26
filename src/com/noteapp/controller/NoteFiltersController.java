package com.noteapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class cho filter
 *
 * @author Nhóm 23
 * @since 31/03/2024
 * @version 1.0
 */
public class NoteFiltersController {
    //Thuộc tính FXML
    @FXML
    private Label filterLabel;
    @FXML
    private ImageView removeFilterView;
    
    /**
     * Thiết lập dữ liệu cho filter
     * @param filter String chứa tên filter
     */
    public void setData(String filter){
        this.filterLabel.setText(filter);
    }   
    
    /**
     * Lấy nút remove filter
     * @return Nút Remove Filter
     */
    public ImageView getRemoveFilterView() {
        return removeFilterView;
    }
    
    /**
     * Lấy tên filter
     * @return tên của filter
     */
    public String getFilter() {
        return filterLabel.getText();
    }
}