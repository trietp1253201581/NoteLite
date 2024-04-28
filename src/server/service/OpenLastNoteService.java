package server.service;

import dataaccess.NoteDataAccess;
import dataaccess.SpecialNoteDataAccess;
import java.util.HashMap;
import java.util.Map;
import model.datatransfer.Note;

/**
 * Mở note được chỉnh sửa gần nhất bởi user
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class OpenLastNoteService implements ServerService {   
    private String author;
    private SpecialNoteDataAccess noteDataAccess;
    
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
     * (1) Note được chỉnh sửa gần nhất,
     * (2) {@link ServerService.ErrorType}.{@code NOT_EXISTS}
     * nếu user không có note nào
     */
    @Override
    public Map<String, Object> execute() { 
        //Tạo đối tượng access dữ liệu
        noteDataAccess = NoteDataAccess.getInstance();
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Lấy Note được chỉnh sửa gần nhất
        Note note = noteDataAccess.getLast(author);
        //Kiểm tra điều kiện và trả về
        if(!note.isDefaultValue()) {
            resultMap.put("Note", note);
            return resultMap;
        } else {
            resultMap.put("ServerServiceError", ServerService.ErrorType.NOT_EXISTS);
            return resultMap;
        }        
    }    
}