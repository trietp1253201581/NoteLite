package com.noteliteserver.service;

import com.notelitemodel.datatransfer.Note;
import com.noteliteserver.dataaccess.DataAccessException;
import com.noteliteserver.dataaccess.NoteDataAccess;
import com.noteliteserver.dataaccess.SpecialNoteDataAccess;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lấy tất cả các note của một user
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class GetAllNotesService implements ServerService {   
    private String author;
    protected SpecialNoteDataAccess noteDataAccess;
    
    public GetAllNotesService() {
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
     * (1) List các {@link Note} của {@link User} này, 
     * (2) {@link DataAccessException} miêu tả lỗi nếu ngược lại
     */
    @Override
    public Map<String, Object> execute() {       
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        try {
            //Lấy các note của author
            List<Note> notes = noteDataAccess.getAll(author); 
            resultMap.put("ListNote", Note.ListOfNotesConverter.convertToString(notes));
            return resultMap;
        } catch (DataAccessException ex) {
            resultMap.put("ServerServiceError", ex.getMessage());
            return resultMap;
        }  
    }   
}