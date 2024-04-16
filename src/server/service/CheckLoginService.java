package server.service;

import dataaccess.SpecialUserDataAccess;
import dataaccess.UserDataAccess;
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
     * @return Kết quả của việc thực thi, (1) Một {@link User} nếu tài khoản tồn tại và mật khẩu đúng,
     * (2) String biểu diễn {@link ServerServiceErrorType}.{@code FALSE_INFORMATION} 
     * nếu tài khoản tồn tại nhưng mật khẩu sai,
     * (3) String biểu diễn {@link ServerServiceErrorType}.{@code NOT_EXISTS} 
     * nếu tài khoản không tồn tại
     */
    @Override
    public String execute() { 
        //Tạo một đối tượng để xử lý dữ liệu với Database
        dataAccess = new UserDataAccess();   
        //Lấy user có username cho trước
        User user = dataAccess.get(username);
        //Kiểm tra các điều kiện và trả về
        if(user.isDefaultValue()) {
            return ServerServiceErrorType.NOT_EXISTS.toString();
        } else if(user.getPassword().equals(password)) {
            return user.toString();
        } else {
            return ServerServiceErrorType.FALSE_INFORMATION.toString();
        }
    }    
}
