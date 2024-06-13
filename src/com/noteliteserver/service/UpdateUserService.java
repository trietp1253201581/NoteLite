package com.noteliteserver.service;

import com.notelitemodel.datatransfer.User;
import com.noteliteserver.dataaccess.DataAccessException;
import com.noteliteserver.dataaccess.SpecialUserDataAccess;
import com.noteliteserver.dataaccess.UserDataAccess;
import java.util.HashMap;
import java.util.Map;

/**
 * Cập nhật User
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class UpdateUserService implements ServerService {    
    private User user;
    protected SpecialUserDataAccess userDataAccess;
    
    public UpdateUserService() {
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
     * @return Kết quả của việc thực thi là một Map miêu tả các value
     * (1) {@link User} được cập nhật nếu cập nhật được,
     * (2) {@link DataAccessException} miêu tả lỗi nếu ngược lại
     */
    @Override
    public Map<String, Object> execute() {
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();      
        try {
            //Thực hiện cập nhật user
            userDataAccess.update(user);
            resultMap.put("User", user);
            return resultMap;
        } catch (DataAccessException ex) {
            resultMap.put("ServerServiceError", ex.getMessage());
            return resultMap;
        }
    }    
}