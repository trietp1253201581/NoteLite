package com.noteliteserver.service;

import com.notelitemodel.datatransfer.User;
import com.noteliteserver.dataaccess.DataAccessException;
import com.noteliteserver.dataaccess.FailedExecuteException;
import com.noteliteserver.dataaccess.SpecialUserDataAccess;
import com.noteliteserver.dataaccess.UserDataAccess;
import java.util.HashMap;
import java.util.Map;

/**
 * Tạo một User mới
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class CreateUserService implements ServerService {    
    private User user;
    protected SpecialUserDataAccess userDataAccess;
    
    public CreateUserService() {
        userDataAccess = UserDataAccess.getInstance();
    }
    
    /**
     * Set data cho các service qua một String
     * @param paramMap Một Map miêu tả các tham số, gồm {@code user}
     */
    @Override
    public void setData(Map<String, String> paramMap) {
        user = User.valueOf(paramMap.get("user"));
    }

    /**
     * Thực thi service
     * @return Kết quả của việc thực thi, 
     * (1) {@link User} mới nếu tạo được,
     * (2) {@link DataAccessException} miêu tả lỗi nếu ngược lại
     */
    @Override
    public Map<String, Object> execute() {       
        //Tạo một đối tượng access dữ liệu
        userDataAccess = UserDataAccess.getInstance();
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Kiểm tra xem tài khoản đã tồn tại chưa
        try {
            userDataAccess.get(user.getUsername());
            resultMap.put("ServerServiceError", "This user is already exist!");
            return resultMap;
        } catch (DataAccessException ex) {
            if(ex instanceof FailedExecuteException) {
                resultMap.put("ServerServiceError", ex.getMessage());
                return resultMap;
            }
        }
        //Thực hiện lệnh thêm user
        try {
            userDataAccess.add(user);
            //Lấy User mới tạo được
            User newUser = userDataAccess.get(user.getUsername());
            //Trả về user
            resultMap.put("User", newUser);
            return resultMap;
        } catch (DataAccessException ex) {
            resultMap.put("ServerServiceError", ex.getMessage());
            return resultMap;
        }    
    }    
}