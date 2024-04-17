package server.service;

import dataaccess.ShareNoteDataAccess;
import dataaccess.SpecialShareNoteDataAccess;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.datatransfer.ShareNote;

/**
 * Lấy tất cả các ShareNote được share tới user
 * @author Lê Minh Triết
 * @since 07/04/2024
 * @version 1.0
 */
public class GetAllReceivedNotesService implements ServerService {
    private String receiver;
    private SpecialShareNoteDataAccess dataAccess;
    
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
     * (1) List các ShareNote của user này, 
     * (2) String biểu diễn {@link ServerServiceErrorType}.{@code NOT_EXISTS}
     * nếu user này chưa có note nào được share bởi user khác
     */
    @Override
    public Map<String, Object> execute() {
        //Tạo đối tượng access
        dataAccess = new ShareNoteDataAccess();
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Lấy các note được share tới user này
        List<ShareNote> shareNotes = dataAccess.getAllReceived(receiver);
        //Trả về kết quả
        if(!shareNotes.isEmpty()) {
            resultMap.put("ListShareNote", shareNotes);
            return resultMap;
        } else {
            resultMap.put("ServerServiceError", ServerServiceErrorType.NOT_EXISTS);
            return resultMap;
        }
    }
}
