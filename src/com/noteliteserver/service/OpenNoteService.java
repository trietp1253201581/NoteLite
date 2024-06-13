package com.noteliteserver.service;

import com.notelitemodel.datatransfer.Note;
import com.noteliteserver.dataaccess.DataAccessException;
import com.noteliteserver.dataaccess.NoteDataAccess;
import com.noteliteserver.dataaccess.SpecialNoteDataAccess;
import java.util.HashMap;
import java.util.Map;

/**
 * Mở note với header và author cho trước
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class OpenNoteService implements ServerService {    
    private String author;
    private String header;
    protected SpecialNoteDataAccess noteDataAccess;
    
    public OpenNoteService() {
        noteDataAccess = NoteDataAccess.getInstance();
    }
    
    /**
     * Set data cho các service qua một String
     * @param paramMap Một Map miêu tả các tham số, gồm {@code author, header}
     */
    @Override
    public void setData(Map<String, String> paramMap) {
        author = paramMap.get("author");
        header = paramMap.get("header");
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi là một Map miêu tả các value
     * (1) Note được mở nếu tìm được,
     * (2) {@link DataAccessException} miêu tả lỗi nếu ngược lại
     */
    @Override
    public Map<String, Object> execute() {        
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        try {
            //Lấy Note cần thiết
            Note note = noteDataAccess.get(author, header);
            resultMap.put("Note", note);
            return resultMap;
        } catch (DataAccessException ex) {
            resultMap.put("ServerServiceError", ex.getMessage());
            return resultMap;           
        }         
    }    
}