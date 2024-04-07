package dataaccess;

import java.util.List;
import model.ShareNote;

/**
 * Định nghĩa các phương thức thao tác đặc biệt với dữ liệu ShareNote
 * @author Lê Minh Triết
 * @since 06/04/2024
 * @version 1.0
 */
public interface SpecialShareNoteDataAccess extends BasicDataAccess<ShareNote> {
 
    /**
     * Lấy tất cả các ShareNote được gửi tới người nhận
     * @param receiver username của người nhận
     * @return Một list chứa các ShareNote gửi tới người nhận
     */
    List<ShareNote> getAllReceived(String receiver);
    
    /**
     * Lấy một ShareNote
     * @param sender người gửi
     * @param receiver người nhận
     * @param header header của ShareNote
     * @return ShareNote duy nhất được lấy
     */
    ShareNote get(String sender, String receiver, String header); 
}
