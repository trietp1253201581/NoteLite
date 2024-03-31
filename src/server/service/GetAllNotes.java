package server.service;

import dataaccess.NoteDataAccess;
import dataaccess.SpecialNoteDataAccess;
import java.util.List;
import model.Note;

/**
 * Lấy tất cả các note của một user
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class GetAllNotes implements ServerService {   
    private int userId;
    private SpecialNoteDataAccess dataAccess;
    
    /**
     * Set data cho các service qua một String
     * @param data String miêu tả data gồm một userId
     */
    @Override
    public void setData(String data) {
        this.userId = Integer.parseInt(data);
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi, (1) String gồm các biểu diễn string của từng note, 
     * được ngăn cách bởi {@code ":::"}, 
     * (2) {@code "Not found"} nếu user này chưa có note nào
     * 
     */
    @Override
    public String execute() {       
        //Tạo một đối tượng access dữ liệu
        dataAccess = new NoteDataAccess();
        //Lấy các note của userId
        List<Note> notes = dataAccess.getAll(userId);  
        //Tạo và trả về kết quả
        String result = "";       
        if(!notes.isEmpty()) {
            for(Note note: notes) {
                result += Note.toString(note) + ":::";
            }
        } else {
            result = "Not found";
        }     
        return result;      
    }   
}
