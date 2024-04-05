package server.service;

import dataaccess.SpecialUserDataAccess;
import dataaccess.UserDataAccess;
import model.User;

/**
 * Tạo một User mới
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class CreateUser implements ServerService {    
    private User user;
    private SpecialUserDataAccess dataAccess;
    
    /**
     * Set data cho các service qua một String
     * @param data String miêu tả data là một String biểu diễn User mới
     */
    @Override
    public void setData(String data) {
        user = User.valueOf(data);
    }

    /**
     * Thực thi service
     * @return Kết quả của việc thực thi, (1) {@code "Exist"} nếu tài khoản đã tồn tại,
     * (2) User mới nếu tạo được,
     * (3) {@code "Can't create"} nếu không thực hiện lệnh tạo được
     */
    @Override
    public String execute() {       
        //Tạo một đối tượng access dữ liệu
        dataAccess = new UserDataAccess();
        //Kiểm tra xem tài khoản đã tồn tại chưa
        User check = dataAccess.get(user.getUsername());
        if(!check.isDefaultValue()) {
            return "Exist";
        }
        //Thực hiện lệnh thêm user
        int rs = dataAccess.add(user);
        if(rs > 0) {
            //Lấy User mới tạo được
            User newUser = dataAccess.get(user.getUsername());
            //Trả về biểu diễn String của user
            return User.toString(newUser);
        } else {
            return "Can't creat";
        }        
    }    
}
