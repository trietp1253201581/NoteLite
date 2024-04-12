package server.service;

import dataaccess.ShareNoteDataAccess;
import dataaccess.SpecialShareNoteDataAccess;
import dataaccess.SpecialUserDataAccess;
import dataaccess.UserDataAccess;
import model.ShareNote;

/**
 * Send một Note tới user khác
 * @author Lê Minh Triết
 * @since 07/04/2024
 * @version 1.0
 */
public class SendNoteService implements ServerService {
    private ShareNote shareNote;
    private SpecialShareNoteDataAccess dataAccess;
    
    /**
     * Set data cho các service qua một String
     * @param data String miêu tả data là một String biểu diễn ShareNote mới
     */
    @Override
    public void setData(String data) {
        shareNote = ShareNote.valueOf(data);
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi, (1) ShareNote send được,
     * (2) String biểu diễn {@link ServerServiceErrorType}.{@code NOT_EXISTS}
     * nếu receiver không tồn tại
     * (3) String biểu diễn {@link ServerServiceErrorType}.{@code CAN_NOT_EXECUTE}
     * nếu không thực hiện lệnh tạo được
     */
    @Override
    public String execute() {
        //Tạo đối tượng access
        dataAccess = new ShareNoteDataAccess();
        //Kiểm tra receiver có tồn tại hay không
        SpecialUserDataAccess userDataAccess = new UserDataAccess();
        if(userDataAccess.get(shareNote.getReceiver()).isDefaultValue()) {
            return ServerServiceErrorType.NOT_EXISTS.toString();
        }
        //Kiểm tra ShareNote đã tồn tại hay chưa
        ShareNote check = dataAccess.get(shareNote.getSender(), 
                    shareNote.getReceiver(), shareNote.getHeader());
        //Nếu tồn tại thì chỉ cân update
        if(!check.isDefaultValue()) {
            shareNote.setId(check.getId());
            int updateRs = dataAccess.update(shareNote);
            if(updateRs > 0) {
                //Lấy ShareNote vừa update
                shareNote = dataAccess.get(shareNote.getSender(), 
                        shareNote.getReceiver(), shareNote.getHeader());
                //Trả về String biểu diễn ShareNote vừa tạo
                return ShareNote.toString(shareNote);
            } else {
                return ServerServiceErrorType.CAN_NOT_EXECUTE.toString();
            }
        }
        //Thực hiện thêm ShareNote
        int addRs = dataAccess.add(shareNote);
        if(addRs > 0) {
            //Lấy ShareNote vừa tạo
            shareNote = dataAccess.get(shareNote.getSender(), 
                    shareNote.getReceiver(), shareNote.getHeader());
            //Trả về String biểu diễn ShareNote vừa tạo
            return ShareNote.toString(shareNote);
        } else {
            return ServerServiceErrorType.CAN_NOT_EXECUTE.toString();
        }
    }
}