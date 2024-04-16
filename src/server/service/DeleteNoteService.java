package server.service;

import dataaccess.NoteDataAccess;
import dataaccess.SpecialNoteDataAccess;
import java.util.Map;
import model.datatransfer.Note;

/**
 * Xóa một Note đã có
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class DeleteNoteService implements ServerService {    
    private String author;
    private String header;
    private SpecialNoteDataAccess dataAccess;
    
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
     * @return Kết quả của việc thực thi, 
     * (1) String biểu diễn {@link ServerServiceErrorType}.{@code NOT_EXISTS}
     * nếu note không tồn tại,
     * (2) Note vừa được xóa nếu xóa được,
     * (3) String biểu diễn {@link ServerServiceErrorType}.{@code CAN_NOT_EXECUTE} 
     * nếu không thực hiện lệnh xóa được
     */
    @Override
    public String execute() {    
        //Tạo đối tượng access dữ liệu
        dataAccess = new NoteDataAccess(); 
        //Kiểm tra note có tồn tại khong
        Note note = dataAccess.get(author, header);
        if(note.isDefaultValue()) {
            return ServerServiceErrorType.NOT_EXISTS.toString();
        }
        //Thực hiện lệnh xóa      
        int rs = dataAccess.delete(note.getId());        
        if(rs > 0) {
            //Trả về biểu diễn String của note được xóa
            return note.toString();
        } else {
            return ServerServiceErrorType.CAN_NOT_EXECUTE.toString();
        }      
    } 
}
