package com.noteliteserver.service;

import com.notelitemodel.datatransfer.Note;
import com.noteliteserver.dataaccess.NoteDataAccess;
import com.noteliteserver.dataaccess.SpecialNoteDataAccess;
import java.util.HashMap;
import java.util.Map;

/**
 * Mở note với header và author cho trước
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class OpenNoteService implements ServerService {    
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
     * (1) Note được mở nếu tìm được,
     * (2) {@link ServerService.ErrorType}.{@code NOT_EXISTS}
     * nếu user không có note nào
     */
    @Override
    public Map<String, Object> execute() {        
        //Tạo đối tượng access dữ liệu
        noteDataAccess = NoteDataAccess.getInstance();
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Lấy Note được chỉnh sửa gần nhất
        Note note = noteDataAccess.get(author, header);
        //Kiểm tra điều kiện và trả về
        if(!note.isDefaultValue()) {
            resultMap.put("Note", note);
            return resultMap;
        } else {
            resultMap.put("ServerServiceError", ServerService.ErrorType.NOT_EXISTS);
            return resultMap;
        }           
    }    
}