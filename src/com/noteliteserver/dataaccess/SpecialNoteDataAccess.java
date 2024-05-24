package com.noteliteserver.dataaccess;

import com.notelitemodel.datatransfer.Note;
import java.util.List;

/**
 * Định nghĩa các phương thức thao tác đặc biệt với dữ liệu Note
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public interface SpecialNoteDataAccess extends BasicDataAccess<Note> {
    
    /**
     * Lấy note của user với header cho trước
     * @param author user sở hữu note
     * @param header header của note
     * @return note lấy được 
     * @throws com.noteliteserver.dataaccess.DataAccessException nếu không thực thi được
     * hoặc author đã cho không có note chứa header này
     */
    Note get(String author, String header) throws DataAccessException;
    
    /**
     * Lấy note được chỉnh sửa mới nhật của user
     * @param author user sở hữu note
     * @return note được chỉnh sửa mới nhất bởi author
     * @throws com.noteliteserver.dataaccess.DataAccessException nếu không thực thi được
     * hoặc author đã cho không có note nào
     */
    Note getLast(String author) throws DataAccessException;
    
    /**
     * Lấy tất cả các note của một user
     * @param author user cần lấy note
     * @return một list chứa các note của user
     * @throws com.noteliteserver.dataaccess.DataAccessException nếu không thực thi được
     * hoặc author đã cho không có note nào
     */
    List<Note> getAll(String author) throws DataAccessException;
}