package fxgui;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Note;

/**
 * FXML Controller class cho các Note Card
 *
 * @author Lê Minh Triết
 * @since 31/03/2024
 * @version 1.0
 */
public class NoteCardFXMLController {  
    //Các thuộc tính FXML
    @FXML
    private GridPane filterGridLayout;
    @FXML
    private Label header;
    @FXML
    private Label lastModified;
    
    public String getHeader() {
        return header.getText();
    }
    
    public void setLabelStyle(String style) {
        header.setStyle(style);
        lastModified.setStyle(style);
    }
    
    /**
     * Thiết lập dữ liệu cho Note Card
     * @param note note chứa dữ liệu cần chuyển vào Note Card
     */
    public void setData(Note note) {
        header.setText(note.getHeader());
        lastModified.setText(String.valueOf(note.getLastModifiedDate()));
        loadFilter(note.getFilters(), 5);
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
}
