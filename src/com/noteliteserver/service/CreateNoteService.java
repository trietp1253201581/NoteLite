package com.noteliteserver.service;

import com.notelitemodel.datatransfer.Note;
import com.noteliteserver.dataaccess.DataAccessException;
import com.noteliteserver.dataaccess.FailedExecuteException;
import com.noteliteserver.dataaccess.NoteDataAccess;
import com.noteliteserver.dataaccess.SpecialNoteDataAccess;
import java.util.HashMap;
import java.util.Map;

/**
 * Tạo một Note mới
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class CreateNoteService implements ServerService {   
    private Note note;
    protected SpecialNoteDataAccess noteDataAccess;

    public CreateNoteService() {
        noteDataAccess = NoteDataAccess.getInstance();  
    }
    
    /**
     * Set data cho các service qua một String
     * @param paramMap Một Map miêu tả các tham số, gồm {@code note}
     */
    @Override
    public void setData(Map<String, String> paramMap) {
        note = Note.valueOf(paramMap.get("note"));
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi là một Map miêu tả các value: 
     * (1) {@link Note} mới nếu tạo được,
     * (2) {@link DataAccessException} miêu tả lỗi nếu ngược lại
     */
    @Override
    public Map<String, Object> execute(){               
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Kiểm tra note đã tồn tại hay chưa
        try {
            noteDataAccess.get(note.getAuthor(), note.getHeader()); 
            resultMap.put("ServerServiceError", "This note is already exist!");
            return resultMap;
        } catch (DataAccessException ex) {
            if(ex instanceof FailedExecuteException) {
                resultMap.put("ServerServiceError", ex.getMessage());
                return resultMap;
            }
        }
        //Thực hiện thêm note
        try {
            noteDataAccess.add(note);
            //Lấy Note vừa mới tạo
            Note newNote = noteDataAccess.get(note.getAuthor(), note.getHeader());
            //Trả về Note mới
            resultMap.put("Note", newNote);
            return resultMap;
        } catch (DataAccessException ex) {
            resultMap.put("ServerServiceError", ex.getMessage());
            return resultMap;
        }    
    }    
}