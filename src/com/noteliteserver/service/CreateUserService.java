package com.noteliteserver.service;

import com.notelitemodel.datatransfer.User;
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
    private SpecialUserDataAccess userDataAccess;
    
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
     * (2) {@link ServerService.ErrorType}.{@code ALREADY_EXISTS}
     * nếu tài khoản đã tồn tại,
     * (3) {@link ServerService.ErrorType}.{@code CAN_NOT_EXECUTE}
     * nếu không thực hiện lệnh tạo được
     */
    @Override
    public Map<String, Object> execute() {       
        //Tạo một đối tượng access dữ liệu
        userDataAccess = UserDataAccess.getInstance();
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Kiểm tra xem tài khoản đã tồn tại chưa
        User check = userDataAccess.get(user.getUsername());
        if(!check.isDefaultValue()) {
            resultMap.put("ServerServiceError", ServerService.ErrorType.ALREADY_EXISTS);
            return resultMap;
        }
        //Thực hiện lệnh thêm user
        int rs = userDataAccess.add(user);
        if(rs > 0) {
            //Lấy User mới tạo được
            User newUser = userDataAccess.get(user.getUsername());
            //Trả về user
            resultMap.put("User", newUser);
            return resultMap;
        } else {
            resultMap.put("ServerServiceError", ServerService.ErrorType.CAN_NOT_EXECUTE);
            return resultMap;
        }        
    }    
}