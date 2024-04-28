package server.service;

import dataaccess.SpecialUserDataAccess;
import dataaccess.UserDataAccess;
import java.util.HashMap;
import java.util.Map;
import model.datatransfer.User;

/**
 * Cập nhật User
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class UpdateUserService implements ServerService {    
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
     * @return Kết quả của việc thực thi là một Map miêu tả các value
     * (1) User được cập nhật nếu cập nhật được,
     * (2) {@link ServerService.ErrorType}.{@code CAN_NOT_EXECUTE}
     * nếu không thực hiện lệnh cập nhật được
     */
    @Override
    public Map<String, Object> execute() {        
        //Tạo đối tượng access dữ liệu
        userDataAccess = UserDataAccess.getInstance();
        //Tạo Map kết quả
        Map<String, Object> resultMap = new HashMap<>();
        //Thực hiện cập nhật user
        int rs = userDataAccess.update(user);
        //Kiểm tra điều kiện
        if(rs > 0) {
            //Lấy user vừa cập nhật
            User updatedUser = userDataAccess.get(user.getUsername());
            //Trả về dưới dạng string
            resultMap.put("User", updatedUser);
            return resultMap;
        } else {
            resultMap.put("ServerServiceError", ServerService.ErrorType.CAN_NOT_EXECUTE);
            return resultMap;
        }        
    }    
}