package server.service;

import dataaccess.NoteDataAccess;
import dataaccess.SpecialNoteDataAccess;
import model.Note;

/**
 * Mở note với header và author cho trước
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class OpenNoteService implements ServerService {    
    private String author;
    private String header;
    private SpecialNoteDataAccess dataAccess;
    
    /**
     * Set data cho các service qua một String
     * @param data String miêu tả data có dạng {@code "author;;;header"}
     */
    @Override
    public void setData(String data) {
        //Chia data thành các phần và gán cho các thuộc tính
        String[] datas = data.split(";;;");
        this.author = datas[0];
        this.header = datas[1];
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi, (1) Note được mở nếu tìm được,
     * (2) String biểu diễn {@link ServerServiceErrorType}.{@code NOT_EXISTS}
     * nếu user không có note với header này
     */
    @Override
    public String execute() {        
        //Tạo đối tượng access dữ liệu
        dataAccess = new NoteDataAccess();
        //Mở note
        Note note = dataAccess.get(author, header);
        //Kiểm tra điều kiện và trả về
        if(!note.isDefaultValue()) {
            return Note.toString(note);
        } else {
            return ServerServiceErrorType.NOT_EXISTS.toString();
        }        
    }    
}