package server.service;

import dataaccess.NoteDataAccess;
import dataaccess.SpecialNoteDataAccess;
import model.Note;

/**
 * Lưu một note
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class SaveNote implements ServerService {    
    private Note note;
    private SpecialNoteDataAccess dataAccess;
    
    /**
     * Set data cho các service qua một String
     * @param data String miêu tả data là một String biểu diễn Note cần lưu
     */
    @Override
    public void setData(String data) {
        note = Note.valueOf(data);
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi, (1) Note cần lưu nếu lưu được,
     * (2) {@code "Can't save"} nếu không thực hiện lệnh lưu được
     */
    @Override
    public String execute() {  
        //Tạo đối tượng access dữ liệu
        dataAccess = new NoteDataAccess(); 
        //Nếu chưa có note thì tạo note mới và trả về
        if(dataAccess.get(note.getUserId(), note.getHeader()).isDefaultValue()) {
            //Tạo Note mới
            int rs = dataAccess.add(note);    
            //Trả về
            if(rs > 0) {
                return Note.toString(note);
            } else {
                return "Can't save";
            }
        }
        //Thiết lập note cần lưu là note được chỉnh sửa gần nhất
        Note lastNote = dataAccess.getLast(note.getUserId());
        lastNote.setLastModified(0);
        dataAccess.update(lastNote);
        //Update note mới
        int rs = dataAccess.update(note);
        //Kiểm tra điều kiện thực hiện lệnh
        if(rs > 0) {
            //Lấy Note vừa được lưu
            Note updatedNote = dataAccess.get(note.getUserId(), note.getHeader());
            //Trả về dưới dạng string
            return Note.toString(updatedNote);
        } else {
            return "Can't save";
        }        
    }    
}
