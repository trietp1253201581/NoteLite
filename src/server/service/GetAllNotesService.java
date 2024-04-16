package server.service;

import dataaccess.NoteDataAccess;
import dataaccess.SpecialNoteDataAccess;
import java.util.List;
import java.util.Map;
import model.datatransfer.Note;

/**
 * Lấy tất cả các note của một user
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class GetAllNotesService implements ServerService {   
    private String author;
    private SpecialNoteDataAccess dataAccess;
    
    /**
     * Set data cho các service qua một String
     * @param paramMap Một Map miêu tả các tham số, gồm {@code author}
     */
    @Override
    public void setData(Map<String, String> paramMap) {
        author = paramMap.get("author");
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi, (1) String gồm các biểu diễn string của từng note, 
     * được ngăn cách bởi {@code ":::"}, 
     * (2) String biểu diễn {@link ServerServiceErrorType}.{@code NOT_EXISTS}
     * nếu user này chưa có note nào
     */
    @Override
    public String execute() {       
        //Tạo một đối tượng access dữ liệu
        dataAccess = new NoteDataAccess();
        //Lấy các note của author
        List<Note> notes = dataAccess.getAll(author);  
        //Tạo và trả về kết quả
        String result = "";       
        if(!notes.isEmpty()) {
            for(Note note: notes) {
                result += note.toString() + ":::";
            }
        } else {
            result = ServerServiceErrorType.NOT_EXISTS.toString();
        }     
        
        return result;      
    }   
}
