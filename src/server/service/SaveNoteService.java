package server.service;

import dataaccess.NoteDataAccess;
import dataaccess.SpecialNoteDataAccess;
import java.util.Map;
import model.datatransfer.Note;

/**
 * Lưu một note
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class SaveNoteService implements ServerService {    
    private Note note;
    private SpecialNoteDataAccess dataAccess;
    
    /**
     * Set data cho các service qua một String
     * @param paramMap Một Map miêu tả các tham số, gồm {@code note}
     */
    @Override
    public void setData(Map<String, String> paramMap) {
        note = Note.valueOf(paramMap.get("note"));
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi, (1) Note cần lưu nếu lưu được,
     * (2) String biểu diễn {@link ServerServiceErrorType}.{@code CAN_NOT_EXECUTE}
     * nếu không thực hiện lệnh lưu được
     */
    @Override
    public String execute() {  
        //Tạo đối tượng access dữ liệu
        dataAccess = new NoteDataAccess(); 
        //Nếu chưa có note thì tạo note mới và trả về
        if(dataAccess.get(note.getAuthor(), note.getHeader()).isDefaultValue()) {
            //Tạo Note mới
            int rs = dataAccess.add(note);    
            //Trả về
            if(rs > 0) {
                return note.toString();
            } else {
                return ServerServiceErrorType.CAN_NOT_EXECUTE.toString();
            }
        }
        //Thiết lập note cần lưu là note được chỉnh sửa gần nhất
        Note lastNote = dataAccess.getLast(note.getAuthor());
        lastNote.setLastModified(0);
        dataAccess.update(lastNote);
        //Update note mới
        int rs = dataAccess.update(note);
        //Kiểm tra điều kiện thực hiện lệnh
        if(rs > 0) {
            //Lấy Note vừa được lưu
            Note updatedNote = dataAccess.get(note.getAuthor(), note.getHeader());
            //Trả về dưới dạng string
            return updatedNote.toString();
        } else {
            return ServerServiceErrorType.CAN_NOT_EXECUTE.toString();
        }        
    }    
}
