package dataaccess;

import java.util.List;
import model.User;

/**
 * Cung cấp các phương thức thao tác đặc biệt với dữ liệu User
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public interface SpecialUserDataAccess extends BasicDataAccess<User> {
    
    /**
     * Lấy user bằng username
     * @param username username của user cần lấy
     * @return (1) user lấy được nếu username tồn tại, (2) giá trị default nếu username không tồn tại
     */
    User get(String username);
    
    /**
     * Lấy tất cả các user
     * @return một list chứa tất cả các user
     */
    List<User> getAll();
}
