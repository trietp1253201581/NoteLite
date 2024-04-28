package server.service;

import dataaccess.ShareNoteDataAccess;
import dataaccess.SpecialShareNoteDataAccess;
import dataaccess.SpecialUserDataAccess;
import dataaccess.UserDataAccess;
import java.util.HashMap;
import java.util.Map;
import model.datatransfer.Note;
import model.datatransfer.ShareNote;

/**
 * Send một Note tới user khác
 * @author Lê Minh Triết
 * @since 07/04/2024
 * @version 1.0
 */
public class SendNoteService implements ServerService {
    private ShareNote shareNote;
    private SpecialShareNoteDataAccess shareNoteDataAccess;
    private SpecialUserDataAccess userDataAccess;
    
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
     * @return Kết quả của việc thực thi, (1) ShareNote send được,
     * (2) String biểu diễn {@link ServerService.ErrorType}.{@code NOT_EXISTS}
     * nếu receiver không tồn tại
     * (3) String biểu diễn {@link ServerService.ErrorType}.{@code CAN_NOT_EXECUTE}
     * nếu không thực hiện lệnh tạo được
     */
    @Override
    public Map<String, Object> execute() {
        //Tạo đối tượng access
        shareNoteDataAccess = ShareNoteDataAccess.getInstance();
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Kiểm tra receiver có tồn tại hay không
        userDataAccess = UserDataAccess.getInstance();
        if(userDataAccess.get(shareNote.getReceiver()).isDefaultValue()) {
            resultMap.put("ServerServiceError", ServerService.ErrorType.NOT_EXISTS);
            return resultMap;
        }
        //Kiểm tra ShareNote đã tồn tại hay chưa
        ShareNote check = shareNoteDataAccess.get((Note) shareNote, shareNote.getReceiver());
        //Nếu tồn tại thì chỉ cân update
        if(!check.isDefaultValue()) {
            shareNote.setId(check.getId());
            int updateRs = shareNoteDataAccess.update(shareNote);
            if(updateRs > 0) {
                //Lấy ShareNote vừa update
                shareNote = shareNoteDataAccess.get((Note) shareNote, shareNote.getReceiver());
                //Trả về String biểu diễn ShareNote vừa tạo
                resultMap.put("ShareNote", shareNote);
                return resultMap;
            } else {
                resultMap.put("ServerServiceError", ServerService.ErrorType.CAN_NOT_EXECUTE);
                return resultMap;
            }
        }
        //Thực hiện thêm ShareNote
        int addRs = shareNoteDataAccess.add(shareNote);
        if(addRs > 0) {
            //Lấy ShareNote vừa tạo
            shareNote = shareNoteDataAccess.get((Note) shareNote, shareNote.getReceiver());
            //Trả về ShareNote vừa tạo
            resultMap.put("ShareNote", shareNote);
            return resultMap;
        } else {
            resultMap.put("ServerServiceError", ServerService.ErrorType.CAN_NOT_EXECUTE);
                return resultMap;
        }
    }
}