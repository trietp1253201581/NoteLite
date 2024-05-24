package com.noteliteserver.service;

import com.notelitemodel.datatransfer.Note;
import com.noteliteserver.dataaccess.DataAccessException;
import com.noteliteserver.dataaccess.NoteDataAccess;
import com.noteliteserver.dataaccess.SpecialNoteDataAccess;
import java.util.HashMap;
import java.util.Map;

/**
 * Xóa một Note đã có
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class DeleteNoteService implements ServerService {    
    private String author;
    private String header;
    private SpecialNoteDataAccess noteDataAccess;
    
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
     * (1) {@link Note} vừa được xóa nếu xóa được,
     * (2) {@link DataAccessException} miêu tả lỗi nếu ngược lại
     */
    @Override
    public Map<String, Object> execute() {    
        //Tạo đối tượng access dữ liệu
        noteDataAccess = NoteDataAccess.getInstance(); 
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();      
        try {
            //Kiểm tra note có tồn tại khong
            Note note = noteDataAccess.get(author, header); 
            //Thực hiện lệnh xóa 
            noteDataAccess.delete(note.getId());
            //Trả về note được xóa
            resultMap.put("Note", note);
            return resultMap;
        } catch (DataAccessException ex) {
            resultMap.put("ServerServiceError", ex.getMessage());
            return resultMap;
        }
    } 
}