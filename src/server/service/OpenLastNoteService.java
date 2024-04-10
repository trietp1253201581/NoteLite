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
public class OpenLastNoteService implements ServerService {   
    private String author;
    private SpecialNoteDataAccess dataAccess;
    
    /**
     * Set data cho các service qua một String
     * @param data String miêu tả data gồm một author
     */
    @Override
    public void setData(String data) {
        this.author = data;
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi, (1) Note được chỉnh sửa gần nhất,
     * (2) String biểu diễn {@link ServerServiceErrorType}.{@code NOT_EXISTS}
     * nếu user không có note nào
     */
    @Override
    public String execute() { 
        //Tạo đối tượng access dữ liệu
        dataAccess = new NoteDataAccess();
        //Lấy Note được chỉnh sửa gần nhất
        Note note = dataAccess.getLast(author);
        //Kiểm tra điều kiện và trả về
        if(!note.isDefaultValue()) {
            return Note.toString(note);
        } else {
            return ServerServiceErrorType.NOT_EXISTS.toString();
        }        
    }    
}