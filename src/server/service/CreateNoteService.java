package server.service;

import dataaccess.NoteDataAccess;
import dataaccess.SpecialNoteDataAccess;
import java.util.HashMap;
import java.util.Map;
import model.datatransfer.Note;

/**
 * Tạo một Note mới
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class CreateNoteService implements ServerService {   
    private Note note;
    private SpecialNoteDataAccess dataAccess;
    
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
     * (2) {@link ServerServiceErrorType}.{@code ALREADY_EXISTS}
     * nếu note đã tồn tại,
     * (3) {@link ServerServiceErrorType}.{@code CAN_NOT_EXECUTE}
     * nếu không thực hiện lệnh tạo được
     */
    @Override
    public Map<String, Object> execute(){        
        //Tạo đối tượng access
        dataAccess = NoteDataAccess.getInstance();  
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Kiểm tra note đã tồn tại hay chưa
        Note check = dataAccess.get(note.getAuthor(), note.getHeader());       
        if(!check.isDefaultValue()) {
            resultMap.put("ServerServiceError", ServerServiceErrorType.ALREADY_EXISTS);
            return resultMap;
        }
        //Thực hiện thêm note
        int rs = dataAccess.add(note);        
        if(rs > 0) {
            //Lấy Note vừa mới tạo
            Note newNote = dataAccess.get(note.getAuthor(), note.getHeader());
            //Trả về Note mới
            resultMap.put("Note", newNote);
            return resultMap;
        } else {
            resultMap.put("ServerServiceError", ServerServiceErrorType.CAN_NOT_EXECUTE);
            return resultMap;
        }       
    }    
}
