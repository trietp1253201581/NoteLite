package com.noteliteserver.service;

import com.notelitemodel.datatransfer.Note;
import com.noteliteserver.dataaccess.DataAccessException;
import com.noteliteserver.dataaccess.NoteDataAccess;
import com.noteliteserver.dataaccess.SpecialNoteDataAccess;
import java.util.HashMap;
import java.util.Map;

/**
 * Mở note được chỉnh sửa gần nhất bởi user
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class OpenLastNoteService implements ServerService {   
    private String author;
    protected SpecialNoteDataAccess noteDataAccess;
    
    public OpenLastNoteService() {
        noteDataAccess = NoteDataAccess.getInstance();
    }
    
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
     * (1) {@link Note} được chỉnh sửa gần nhất,
     * (2) {@link DataAccessException} miêu tả lỗi nếu ngược lại
     */
    @Override
    public Map<String, Object> execute() { 
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        try {
            //Lấy Note được chỉnh sửa gần nhất
            Note note = noteDataAccess.getLast(author);
            resultMap.put("Note", note);
            return resultMap;
        } catch (DataAccessException ex) {
            resultMap.put("ServerServiceError", ex.getMessage());
            return resultMap;           
        }  
    }    
}