package com.noteliteserver.service;

import com.notelitemodel.datatransfer.ShareNote;
import com.noteliteserver.dataaccess.DataAccessException;
import com.noteliteserver.dataaccess.FailedExecuteException;
import com.noteliteserver.dataaccess.ShareNoteDataAccess;
import com.noteliteserver.dataaccess.SpecialShareNoteDataAccess;
import java.util.HashMap;
import java.util.Map;

/**
 * Send một Note tới user khác
 * @author Nhóm 23
 * @since 07/04/2024
 * @version 1.0
 */
public class SendNoteService implements ServerService {
    private ShareNote shareNote;
    protected SpecialShareNoteDataAccess shareNoteDataAccess;
    
    public SendNoteService() {
        shareNoteDataAccess = ShareNoteDataAccess.getInstance();
    }
    
    /**
     * Set data cho các service qua một String
     * @param paramMap Một Map miêu tả các tham số, gồm {@code shareNote}
     */
    @Override
    public void setData(Map<String, String> paramMap) {
        shareNote = ShareNote.valueOf(paramMap.get("shareNote"));
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi, (1) {@link ShareNote} send được,
     * (2) {@link DataAccessException} miêu tả lỗi nếu ngược lại
     */
    @Override
    public Map<String, Object> execute() {
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        try {
            //Kiểm tra ShareNote đã tồn tại hay chưa
            ShareNote check = shareNoteDataAccess.get(shareNote.getId(), shareNote.getReceiver());
            //Nếu tồn tại thì chỉ cân update
            shareNote.setShareId(check.getShareId());
            shareNoteDataAccess.update(shareNote);
            //Trả về ShareNote vừa tạo
            resultMap.put("ShareNote", shareNote);
            return resultMap;
        } catch (DataAccessException ex) {
            if(ex instanceof FailedExecuteException) {
                resultMap.put("ServerServiceError", ex.getMessage());
                return resultMap;
            } 
        }
        try {
            shareNoteDataAccess.add(shareNote);
            //Trả về ShareNote vừa tạo
            resultMap.put("ShareNote", shareNote);
            return resultMap;
        } catch (DataAccessException ex) {
            resultMap.put("ServerServiceError", ex.getMessage());
            return resultMap;           
        }
    }
}