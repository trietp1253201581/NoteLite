package server.service;

import dataaccess.SpecialUserDataAccess;
import dataaccess.UserDataAccess;
import java.util.HashMap;
import java.util.Map;
import model.datatransfer.User;

/**
 * Tạo một User mới
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class CreateUserService implements ServerService {    
    private User user;
    private SpecialUserDataAccess dataAccess;
    
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
     * (2) {@link ServerServiceErrorType}.{@code ALREADY_EXISTS}
     * nếu tài khoản đã tồn tại,
     * (3) {@link ServerServiceErrorType}.{@code CAN_NOT_EXECUTE}
     * nếu không thực hiện lệnh tạo được
     */
    @Override
    public Map<String, Object> execute() {       
        //Tạo một đối tượng access dữ liệu
        dataAccess = new UserDataAccess();
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Kiểm tra xem tài khoản đã tồn tại chưa
        User check = dataAccess.get(user.getUsername());
        if(!check.isDefaultValue()) {
            resultMap.put("ServerServiceError", ServerServiceErrorType.ALREADY_EXISTS);
            return resultMap;
        }
        //Thực hiện lệnh thêm user
        int rs = dataAccess.add(user);
        if(rs > 0) {
            //Lấy User mới tạo được
            User newUser = dataAccess.get(user.getUsername());
            //Trả về user
            resultMap.put("User", newUser);
            return resultMap;
        } else {
            resultMap.put("ServerServiceError", ServerServiceErrorType.CAN_NOT_EXECUTE);
            return resultMap;
        }        
    }    
}
