package dataaccess;

import java.util.List;
import model.Note;

/**
 * Định nghĩa các phương thức thao tác đặc biệt với dữ liệu Note
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public interface SpecialNoteDataAccess extends BasicDataAccess<Note> {
    
    /**
     * Lấy note của user với header cho trước
     * @param author user sở hữu note
     * @param header header của note
     * @return (1) note lấy được nếu user này có note mang header đã cho, 
     * (2) giá trị default của note nếu user không có note mang header này  
     */
    Note get(String author, String header);
    
    /**
     * Lấy note được chỉnh sửa mới nhật của user
     * @param author user sở hữu note
     * @return (1) note được chỉnh sửa mói nhất, (2) default note nếu user chưa có note nào
     */
    Note getLast(String author);
    
    /**
     * Lấy tất cả các note của một user
     * @param author user cần lấy note
     * @return một list chứa các note của user
     */
    List<Note> getAll(String author);
}
