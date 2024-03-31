package model;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Một transfer cho dữ liệu của các user
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class User {
    private int id;
    private String name;
    private String username;
    private String password;
    private Date birthday;
    private String school;

    /**
     * Constructor và cài đặt dữ liệu default cho User
     */
    public User() {
        this.id = -1;
        this.name = "";
        this.username = "";
        this.password = "";
        this.birthday = Date.valueOf(LocalDate.MIN);
        this.school = "";
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getSchool() {
        return school;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setSchool(String school) {
        this.school = school;
    }
    
    /**
     * Kiểm tra xem một thể hiện User có mang giá trị default không
     * @return (1) {@code true} nếu đây là default User, (2) {@code false} nếu ngược lại
     */
    public boolean isDefaultValue() {
        return id == -1;
    }

    /**
     * Chuyển một User thành String
     * @param user user cần chuyển
     * @return String thu được, có dạng {@code "*;;;*;;;*;;;*;;;*;;;*;;;*;;;///"}
     * trong đó * đại diện cho các thuộc tính
     */
    public static String toString(User user) {        
        String result = "";
        //Tạo ra một String biểu diễn cho User
        result += user.getId() + ";;;";
        result += user.getName() + ";;;";
        result += user.getUsername() + ";;;";
        result += user.getPassword() + ";;;";
        result += user.getBirthday() + ";;;";
        result += user.getSchool() + ";;;";
        result += "///";
        
        return result;        
    }
    
    /**
     * Chuyển một String sang một User
     * @param str String cần chuyển có dạng {@code "*;;;*;;;*;;;*;;;*;;;*;;;*;;;///"}
     * trong đó * đại diện cho các thuộc tính
     * @return User thu được
     */
    public static User valueOf(String str) {       
        User user = new User();
        //Chia chuỗi thành các phần để xử lý
        String[] strarr = str.split(";;;");
        //Dựa vào dữ liệu từng phần dể set cho các thuộc tính của user
        user.setId(Integer.parseInt(strarr[0]));
        user.setName(strarr[1]);
        user.setUsername(strarr[2]);
        user.setPassword(strarr[3]);
        user.setBirthday(Date.valueOf(strarr[4]));
        user.setSchool(strarr[5]);
        
        return user;       
    }
}
