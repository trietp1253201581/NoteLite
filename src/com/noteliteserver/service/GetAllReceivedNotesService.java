package com.noteliteserver.service;

import com.notelitemodel.datatransfer.ShareNote;
import com.noteliteserver.dataaccess.DataAccessException;
import com.noteliteserver.dataaccess.ShareNoteDataAccess;
import com.noteliteserver.dataaccess.SpecialShareNoteDataAccess;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lấy tất cả các ShareNote được share tới user
 * @author Nhóm 23
 * @since 07/04/2024
 * @version 1.0
 */
public class GetAllReceivedNotesService implements ServerService {
    private String receiver;
    private SpecialShareNoteDataAccess shareNoteDataAccess;
    
    /**
     * Set data cho các service qua một String
     * @param paramMap Một Map miêu tả các tham số, gồm {@code receiver}
     */
    @Override
    public void setData(Map<String, String> paramMap) {
        receiver = paramMap.get("receiver");
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi là một Map miêu tả các value
     * (1) List các {@link ShareNote} của User này, 
     * (2) {@link DataAccessException} miêu tả lỗi nếu ngược lại
     */
    @Override
    public Map<String, Object> execute() {
        //Tạo đối tượng access
        shareNoteDataAccess = ShareNoteDataAccess.getInstance();
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        try {
            //Lấy các note được share tới user này
            List<ShareNote> shareNotes = shareNoteDataAccess.getAllReceived(receiver);
            resultMap.put("ListShareNote", ShareNote.ListOfShareNotesConverter.convertToString(shareNotes));
            return resultMap;
        } catch (DataAccessException ex) {
            resultMap.put("ServerServiceError", ex.getMessage());
            return resultMap;
        }
    }
}