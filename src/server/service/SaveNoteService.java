package server.service;

import dataaccess.NoteDataAccess;
import dataaccess.SpecialNoteDataAccess;
import java.util.HashMap;
import java.util.Map;
import model.datatransfer.Note;

/**
 * Lưu một note
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class SaveNoteService implements ServerService {    
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
     * @return Kết quả của việc thực thi là một Map miêu tả các value
     * (1) Note cần lưu nếu lưu được,
     * (2) {@link ServerServiceErrorType}.{@code CAN_NOT_EXECUTE}
     * nếu không thực hiện lệnh lưu được
     */
    @Override
    public Map<String, Object> execute() {  
        //Tạo đối tượng access dữ liệu
        noteDataAccess = NoteDataAccess.getInstance(); 
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Nếu chưa có note thì tạo note mới và trả về
        if(noteDataAccess.get(note.getId()).isDefaultValue()) {
            //Tạo Note mới
            int rs = noteDataAccess.add(note);    
            //Trả về
            if(rs > 0) {
                resultMap.put("Note", note);
                return resultMap;
            } else {
                resultMap.put("ServerServiceError", ServerServiceErrorType.CAN_NOT_EXECUTE);
                return resultMap;
            }
        }
        //Thiết lập note cần lưu là note được chỉnh sửa gần nhất
        Note lastNote = noteDataAccess.getLast(note.getAuthor());
        lastNote.setLastModified(0);
        noteDataAccess.update(lastNote);
        //Update note mới
        int rs = noteDataAccess.update(note);
        //Kiểm tra điều kiện thực hiện lệnh
        if(rs > 0) {
            //Lấy Note vừa được lưu
            Note updatedNote = noteDataAccess.get(note.getAuthor(), note.getHeader());
            //Trả về dưới dạng string
            resultMap.put("Note", updatedNote);
            return resultMap;
        } else {
            resultMap.put("ServerServiceError", ServerServiceErrorType.CAN_NOT_EXECUTE);
            return resultMap;
        }        
    }    
}
