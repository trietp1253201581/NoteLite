package com.noteliteserver.dataaccess;

import com.notelitemodel.datatransfer.ShareNote;
import java.util.List;

/**
 * Định nghĩa các phương thức thao tác đặc biệt với dữ liệu ShareNote
 * @author Nhóm 23
 * @since 06/04/2024
 * @version 1.0
 */
public interface SpecialShareNoteDataAccess extends BasicDataAccess<ShareNote> {
 
    /**
     * Lấy tất cả các ShareNote được gửi tới người nhận
     * @param receiver username của người nhận
     * @return Một list chứa các ShareNote gửi tới người nhận
     * @throws com.noteliteserver.dataaccess.DataAccessException nếu không thực thi được
     * hoặc receiver không nhận được sharenote nào
     */
    List<ShareNote> getAllReceived(String receiver) throws DataAccessException;
    
    /**
     * Lấy một ShareNote
     * @param noteId id của Note được share
     * @param receiver người nhận
     * @return ShareNote duy nhất được lấy
     * @throws com.noteliteserver.dataaccess.DataAccessException nếu không thực thi được
     * hoặc receiver không nhận được note có id là noteId
     */
    ShareNote get(int noteId, String receiver) throws DataAccessException; 
    
    /**
     * Xóa tất cả các ShareNote có NoteId cho trước
     * @param noteId id của note được share
     * @throws DataAccessException nếu không thực thi được
     */
    void deleteAll(int noteId) throws DataAccessException;
}