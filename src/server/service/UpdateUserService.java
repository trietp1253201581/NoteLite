package server.service;

import dataaccess.SpecialUserDataAccess;
import dataaccess.UserDataAccess;
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
     * @return Kết quả của việc thực thi, (1) User được cập nhật nếu cập nhật được,
     * (2) String biểu diễn {@link ServerServiceErrorType}.{@code CAN_NOT_EXECUTE}
     * nếu không thực hiện lệnh cập nhật được
     */
    @Override
    public String execute() {        
        //Tạo đối tượng access dữ liệu
        dataAccess = new UserDataAccess();
        //Thực hiện cập nhật user
        int rs = dataAccess.update(user);
        //Kiểm tra điều kiện
        if(rs > 0) {
            //Lấy user vừa cập nhật
            User updatedUser = dataAccess.get(user.getUsername());
            //Trả về dưới dạng string
            return updatedUser.toString();
        } else {
            return ServerServiceErrorType.CAN_NOT_EXECUTE.toString();
        }        
    }    
}