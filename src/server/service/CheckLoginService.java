package server.service;

import dataaccess.SpecialUserDataAccess;
import dataaccess.UserDataAccess;
import java.util.HashMap;
import java.util.Map;
import model.datatransfer.User;

/**
 * Kiểm tra thông tin đăng nhập
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class CheckLoginService implements ServerService {  
    private String username;
    private String password;
    private SpecialUserDataAccess dataAccess;

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
     * (2) {@link ServerServiceErrorType}.{@code FALSE_INFORMATION} 
     * nếu tài khoản tồn tại nhưng mật khẩu sai,
     * (3) {@link ServerServiceErrorType}.{@code NOT_EXISTS} 
     * nếu tài khoản không tồn tại
     */
    @Override
    public Map<String, Object> execute() { 
        //Tạo một đối tượng để xử lý dữ liệu với Database
        dataAccess = new UserDataAccess();   
        //Lấy user có username cho trước
        User user = dataAccess.get(username);
        //Tạo một Map để miêu tả kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Kiểm tra các điều kiện để thêm tương ứng vào resultMap
        if(user.isDefaultValue()) {
            resultMap.put("ServerServiceError", ServerServiceErrorType.NOT_EXISTS);
            return resultMap;
        }
        if(user.getPassword().equals(password)) {
            resultMap.put("User", user);
            return resultMap;
        } else {
            resultMap.put("ServerServiceError", ServerServiceErrorType.FALSE_INFORMATION);
            return resultMap;
        }
    }    
}
