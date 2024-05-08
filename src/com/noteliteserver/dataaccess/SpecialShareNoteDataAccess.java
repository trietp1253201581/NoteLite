package com.noteliteserver.dataaccess;

import com.notelitemodel.datatransfer.Note;
import com.notelitemodel.datatransfer.ShareNote;
import java.util.List;

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
     * @param note Note được share
     * @param receiver người nhận
     * @return ShareNote duy nhất được lấy
     */
    ShareNote get(Note note, String receiver); 
}