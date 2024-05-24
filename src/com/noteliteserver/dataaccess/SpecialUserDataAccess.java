package com.noteliteserver.dataaccess;

import com.notelitemodel.datatransfer.User;
import java.util.List;

/**
 * Cung cấp các phương thức thao tác đặc biệt với dữ liệu User
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public interface SpecialUserDataAccess extends BasicDataAccess<User> {
    
    /**
     * Lấy user bằng username
     * @param username username của user cần lấy
     * @return object lấy được
     * @throws com.noteliteserver.dataaccess.DataAccessException nếu không thực thi được
     * hoặc không tồn tại User có username này
     */
    User get(String username) throws DataAccessException;
    
    /**
     * Lấy tất cả các user
     * @return một list chứa tất cả các user
     * @throws com.noteliteserver.dataaccess.DataAccessException nếu không thực thi được
     * hoặc đang chưa có user này
     */
    List<User> getAll() throws DataAccessException;
}