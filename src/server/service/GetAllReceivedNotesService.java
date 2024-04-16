package server.service;

import dataaccess.ShareNoteDataAccess;
import dataaccess.SpecialShareNoteDataAccess;
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
     * @return Kết quả của việc thực thi, (1) String gồm các biểu diễn string của từng ShareNote, 
     * được ngăn cách bởi {@code ":::"}, 
     * (2) String biểu diễn {@link ServerServiceErrorType}.{@code NOT_EXISTS}
     * nếu user này chưa có note nào được share bởi user khác
     */
    @Override
    public String execute() {
        //Tạo đối tượng access
        dataAccess = new ShareNoteDataAccess();
        //Lấy các note được share tới user này
        List<ShareNote> shareNotes = dataAccess.getAllReceived(receiver);
        //Trả về kết quả
        String result = "";
        if(!shareNotes.isEmpty()) {
            for(ShareNote shareNote: shareNotes) {
                result += shareNote.toString() + ":::";
            }
        } else {
            result = ServerServiceErrorType.NOT_EXISTS.toString();
        }
        
        return result;
    }
}
