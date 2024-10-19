package com.noteliteserver.service;

import com.notelitemodel.datatransfer.User;
import com.noteliteserver.dataaccess.BasicDataAccess;
import com.noteliteserver.dataaccess.DataAccessException;
import com.noteliteserver.dataaccess.NullKey;
import com.noteliteserver.dataaccess.UserDataAccess;
import com.noteliteserver.dataaccess.UserKey;
import java.util.HashMap;
import java.util.Map;


/**
 * Kiểm tra thông tin đăng nhập
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class CheckLoginService implements ServerService {  
    private String username;
    private String password;
    protected BasicDataAccess<User, UserKey, NullKey> userDataAccess;
    
    public CheckLoginService() {
        userDataAccess = UserDataAccess.getInstance();
    }

    /**
     * Set data cho các service qua một String
     * @param paramMap Một Map miêu tả các tham số, gồm {@code username, passworđ}
     */
    @Override
    public void setData(Map<String, String> paramMap) {
        username = paramMap.get("username");
        password = paramMap.get("password");
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi là một Map với các value
     * (1) Một {@link User} nếu tài khoản tồn tại và mật khẩu đúng,
     * (2) {@link DataAccessException} miêu tả lỗi nếu ngược lại
     */
    @Override
    public Map<String, Object> execute() { 
        //Tạo một Map để miêu tả kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Lấy user có username cho trước
        try {
            User user = userDataAccess.get(username);
            if(user.getPassword().equals(password)) {
                resultMap.put("User", user);
            } else {
                resultMap.put("ServerServiceError", "The password is false!");
            }
            return resultMap;
        } catch (DataAccessException ex) {
            resultMap.put("ServerServiceError", ex.getMessage());
            return resultMap;
        }       
    }    
}