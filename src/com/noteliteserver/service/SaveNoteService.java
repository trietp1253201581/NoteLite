package com.noteliteserver.service;

import com.notelitemodel.datatransfer.Note;
import com.noteliteserver.dataaccess.DataAccessException;
import com.noteliteserver.dataaccess.NotExistDataException;
import com.noteliteserver.dataaccess.NoteDataAccess;
import com.noteliteserver.dataaccess.SpecialNoteDataAccess;
import java.util.HashMap;
import java.util.Map;

/**
 * Lưu một note
 * @author Nhóm 23
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
     * (1) {@link Note} cần lưu nếu lưu được,
     * (2) {@link DataAccessException} miêu tả lỗi nếu ngược lại
     */
    @Override
    public Map<String, Object> execute() {  
        //Tạo đối tượng access dữ liệu
        noteDataAccess = NoteDataAccess.getInstance(); 
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Nếu chưa có note thì tạo note mới và trả về
        try {
            noteDataAccess.get(note.getId());
        } catch (DataAccessException ex) {
            if(ex instanceof NotExistDataException) {
                //Thêm note mới và trả về
                try {
                    noteDataAccess.add(note);
                    resultMap.put("Note", note);
                    return resultMap;
                } catch (DataAccessException ex1) {
                    resultMap.put("ServerServiceError", ex1.getMessage());
                    return resultMap;
                }
            }
            resultMap.put("ServerServiceError", ex.getMessage());
            return resultMap;
        }
        try {
            //Thiết lập note cần lưu là note được chỉnh sửa gần nhất
            Note lastNote = noteDataAccess.getLast(note.getAuthor());
            lastNote.setLastModified(0);
            noteDataAccess.update(lastNote);
            noteDataAccess.update(note);
            //Trả về Note vừa được lưu
            resultMap.put("Note", note);
            return resultMap;
        //Update note mới
        } catch (DataAccessException ex) {
            resultMap.put("ServerServiceError", ex.getMessage());
            return resultMap;
        }
    }    
}