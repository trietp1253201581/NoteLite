package server.service;

import dataaccess.SpecialUserDataAccess;
import dataaccess.UserDataAccess;
import model.User;

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
     * @param data String miêu tả data có dạng {@code "username;;;password"}
     */
    @Override
    public void setData(String data) {
        //Chia data thành các phần và gán cho các thuộc tính
        String[] datas = data.split(";;;");
        this.username = datas[0];
        this.password = datas[1];
    }
    
    /**
     * Thực thi service
     * @return Kết quả của việc thực thi, (1) Một {@link User} nếu tài khoản tồn tại và mật khẩu đúng,
     * (2) String {@code "False"} nếu tài khoản tồn tại nhưng mật khẩu sai,
     * (3) String {@code "Not found"} nếu tài khoản không tồn tại
     */
    @Override
    public String execute() { 
        //Tạo một đối tượng để xử lý dữ liệu với Database
        dataAccess = new UserDataAccess();   
        //Lấy user có username cho trước
        User user = dataAccess.get(username);
        //Kiểm tra các điều kiện và trả về
        if(user.isDefaultValue()) {
            return "Not found";
        } else if(user.getPassword().equals(password)) {
            return User.toString(user);
        } else {
            return "False";
        }
    }    
}
