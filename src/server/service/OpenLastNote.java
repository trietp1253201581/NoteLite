package server.service;

import dataaccess.NoteDataAccess;
import dataaccess.SpecialNoteDataAccess;
import model.Note;

/**
 * Mở note được chỉnh sửa gần nhất bởi user
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class OpenLastNote implements ServerService {   
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
     * @return Kết quả của việc thực thi, (1) Note được chỉnh sửa gần nhất,
     * (2) {@code "Not found"} nếu user không có note nào
     */
    @Override
    public String execute() { 
        //Tạo đối tượng access dữ liệu
        dataAccess = new NoteDataAccess();
        //Lấy Note được chỉnh sửa gần nhất
        Note note = dataAccess.getLast(userId);
        //Kiểm tra điều kiện và trả về
        if(!note.isDefaultValue()) {
            return Note.toString(note);
        } else {
            return "Not exist";
        }        
    }    
}