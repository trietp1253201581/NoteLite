package fxgui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * FXML Controller class cho filter
 *
 * @author Lê Minh Triết
 * @since 31/03/2024
 * @version 1.0
 */
public class FilterFXMLController {
    //Thuộc tính FXML
    @FXML
    private Label filterLabel;
    
    /**
     * Thiết lập dữ liệu cho filter
     * @param filter String chứa tên filter
     */
    public void setData(String filter){
        this.filterLabel.setText(filter);
    }   
}
