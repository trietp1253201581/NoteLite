package com.noteliteserver.service;

import com.notelitemodel.datatransfer.Note;
import com.noteliteserver.dataaccess.NoteDataAccess;
import com.noteliteserver.dataaccess.SpecialNoteDataAccess;
import java.util.HashMap;
import java.util.Map;

/**
 * Tạo một Note mới
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class CreateNoteService implements ServerService {   
    private Note note;
    private SpecialNoteDataAccess noteDataAccess;
    
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
     * (2) {@link ServerService.ErrorType}.{@code ALREADY_EXISTS}
     * nếu note đã tồn tại,
     * (3) {@link ServerService.ErrorType}.{@code CAN_NOT_EXECUTE}
     * nếu không thực hiện lệnh tạo được
     */
    @Override
    public Map<String, Object> execute(){        
        //Tạo đối tượng access
        noteDataAccess = NoteDataAccess.getInstance();  
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Kiểm tra note đã tồn tại hay chưa
        Note check = noteDataAccess.get(note.getAuthor(), note.getHeader());       
        if(!check.isDefaultValue()) {
            resultMap.put("ServerServiceError", ServerService.ErrorType.ALREADY_EXISTS);
            return resultMap;
        }
        //Thực hiện thêm note
        int rs = noteDataAccess.add(note);        
        if(rs > 0) {
            //Lấy Note vừa mới tạo
            Note newNote = noteDataAccess.get(note.getAuthor(), note.getHeader());
            //Trả về Note mới
            resultMap.put("Note", newNote);
            return resultMap;
        } else {
            resultMap.put("ServerServiceError", ServerService.ErrorType.CAN_NOT_EXECUTE);
            return resultMap;
        }       
    }    
}