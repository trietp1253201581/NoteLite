package server.service;

import dataaccess.SpecialUserDataAccess;
import dataaccess.UserDataAccess;
import model.User;

/**
 * Cập nhật User
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class UpdateUser implements ServerService {    
    private User user;
    private SpecialUserDataAccess dataAccess;
    
    /**
     * Set data cho các service qua một String
     * @param data String miêu tả data là một String biểu diễn User cần cập nhật
     */
    @Override
    public void setData(String data) {
        user = User.valueOf(data);
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi, (1) User được cập nhật nếu cập nhật được,
     * (2) {@code "Can't save"} nếu không thực hiện lệnh cập nhật được
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
            return User.toString(updatedUser);
        } else {
            return "Can't update";
        }        
    }    
}