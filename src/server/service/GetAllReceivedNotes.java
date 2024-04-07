package server.service;

import dataaccess.ShareNoteDataAccess;
import dataaccess.SpecialShareNoteDataAccess;
import java.util.List;
import model.ShareNote;

/**
 * Lấy tất cả các ShareNote được share tới user
 * @author Lê Minh Triết
 * @since 07/04/2024
 * @version 1.0
 */
public class GetAllReceivedNotes implements ServerService {
    private String receiver;
    private SpecialShareNoteDataAccess dataAccess;
    
    /**
     * Set data cho các service qua một String
     * @param data String miêu tả data gồm một receiver
     */
    @Override
    public void setData(String data) {
        receiver = data;
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi, (1) String gồm các biểu diễn string của từng ShareNote, 
     * được ngăn cách bởi {@code ":::"}, 
     * (2) {@code "Not found"} nếu user này chưa có note nào được share bởi user khác
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
                result += ShareNote.toString(shareNote) + ":::";
            }
        } else {
            result = "Not found";
        }
        
        return result;
    }
}
