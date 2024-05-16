package com.noteliteserver.service;

import com.notelitemodel.datatransfer.Note;
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
     * (1) List các Note của user này, 
     * (2) {@link ServerService.ErrorType}.{@code NOT_EXISTS}
     * nếu user này chưa có note nào
     */
    @Override
    public Map<String, Object> execute() {       
        //Tạo một đối tượng access dữ liệu
        noteDataAccess = NoteDataAccess.getInstance();
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Lấy các note của author
        List<Note> notes = noteDataAccess.getAll(author);  
        //Tạo và trả về kết quả     
        if(!notes.isEmpty()) {
            resultMap.put("ListNote", Note.ListOfNotesConverter.convertToString(notes));
            return resultMap;
        } else {
            resultMap.put("ServerServiceError", ServerService.ErrorType.NOT_EXISTS);
            return resultMap;
        }     
    }   
}