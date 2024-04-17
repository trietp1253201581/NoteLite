package server.service;

import dataaccess.NoteDataAccess;
import dataaccess.SpecialNoteDataAccess;
import java.util.HashMap;
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
     * @return Kết quả của việc thực thi là một Map miêu tả các value
     * (1) List các Note của user này, 
     * (2) {@link ServerServiceErrorType}.{@code NOT_EXISTS}
     * nếu user này chưa có note nào
     */
    @Override
    public Map<String, Object> execute() {       
        //Tạo một đối tượng access dữ liệu
        dataAccess = new NoteDataAccess();
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Lấy các note của author
        List<Note> notes = dataAccess.getAll(author);  
        //Tạo và trả về kết quả     
        if(!notes.isEmpty()) {
            resultMap.put("ListNote", notes);
            return resultMap;
        } else {
            resultMap.put("ServerServiceError", ServerServiceErrorType.NOT_EXISTS);
            return resultMap;
        }     
    }   
}
