package dataaccess;

import dataaccess.connectdatabase.ConnectDatabase;
import dataaccess.connectdatabase.ConnectMySQLDatabase;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;

/**
 * Triển khai các phương thức thao tác dữ liệu với User
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class UserDataAccess implements SpecialUserDataAccess {
    private Connection connection;

    /**
     * Khởi tạo và lấy connection tới Database
     */
    public UserDataAccess() {
        ConnectDatabase connectSQLDatabase = new ConnectMySQLDatabase();
        this.connection = connectSQLDatabase.getJDBCConnection();
    }

    /**
     * Lấy tất cả các user
     * @return một list chứa tất cả các user
     */
    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        //Câu truy vấn SQL
        String query = "SELECT * FROM USERS";

        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Chuyển từng hàng dữ liệu sang user và thêm vào list
            while (resultSet.next()) {
                User user = new User();
                //Set dữ liệu cho user
                user.setId(resultSet.getInt("ID"));
                user.setName(resultSet.getString("NAME"));
                user.setUsername(resultSet.getString("USERNAME"));
                user.setPassword(resultSet.getString("PASSWORD"));
                user.setBirthday(Date.valueOf(resultSet.getString("BIRTHDAY")));
                user.setSchool(resultSet.getString("SCHOOL"));

                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return users;
    }
    
    /**
     * Lấy user theo id
     * @param id id của user cần lấy
     * @return (1) user lấy được nếu id tồn tại, (2) giá trị default nếu id không tồn tại
     */
    @Override
    public User get(int id) {
        //Câu truy vấn SQL
        String query = "SELECT * FROM USERS WHERE ID = ?";

        try {
            //Thực thi truy vấn SQL, gán tham số cho ID và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                //Set dữ liệu từ hàng vào user
                user.setId(resultSet.getInt("ID"));
                user.setName(resultSet.getString("NAME"));
                user.setUsername(resultSet.getString("USERNAME"));
                user.setPassword(resultSet.getString("PASSWORD"));
                user.setBirthday(Date.valueOf(resultSet.getString("BIRTHDAY")));
                user.setSchool(resultSet.getString("SCHOOL"));

                return user;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //Trả về giá trị default nếu không tìm được id
        return new User();
    }

    /**
     * Lấy user bằng username
     * @param username username của user cần lấy
     * @return (1) user lấy được nếu username tồn tại, (2) giá trị default nếu username không tồn tại
     */
    @Override
    public User get(String username) {
        //Câu truy vấn SQL
        String query = "SELECT * FROM USERS WHERE USERNAME = ?";

        try {
            //Thực thi truy vấn SQL, gán tham số cho USERNAME và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                //Set dữ liệu từ hàng nhận được
                user.setId(resultSet.getInt("ID"));
                user.setName(resultSet.getString("NAME"));
                user.setUsername(resultSet.getString("USERNAME"));
                user.setPassword(resultSet.getString("PASSWORD"));
                user.setBirthday(Date.valueOf(resultSet.getString("BIRTHDAY")));
                user.setSchool(resultSet.getString("SCHOOL"));

                return user;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //Trả về default User nếu không tìm được
        return new User();
    }

    /**
     * Thêm user vào CSDL
     * @param user user cần thêm vào CSDL
     * @return (1) một số tự nhiên biểu thị row count khi thực thi lệnh SQL này,
     * (2) -1 nếu không thực hiện được
     */
    @Override
    public int add(User user) {
        //Câu truy vấn SQL
        String query = "INSERT INTO USERS(NAME, USERNAME, PASSWORD, BIRTHDAY, SCHOOL) VALUES(?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setDate(4, user.getBirthday());
            preparedStatement.setString(5, user.getSchool());

            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    /**
     * Chỉnh sửa một user trong CSDL
     * @param user user cần chỉnh sửa
     * @return (1) một số tự nhiên biểu thị row count khi thực thi lệnh SQL này,
     * (2) -1 nếu không thực hiện được
     */
    @Override
    public int update(User user) {
        //Câu truy vấn SQL
        String query = "UPDATE USERS SET NAME = ?, USERNAME = ?, PASSWORD = ?, BIRTHDAY = ?, SCHOOL = ? WHERE ID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setDate(4, user.getBirthday());
            preparedStatement.setString(5, user.getSchool());
            preparedStatement.setInt(6, user.getId());

            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    /**
     * Xóa một user ra khỏi CSDL
     * @param id id của user cần xóa
     * @return (1) một số tự nhiên biểu thị row count khi thực thi lệnh SQL này,
     * (2) -1 nếu không thực hiện được
     */
    @Override
    public int delete(int id) {
        //Câu truy vấn SQL
        String query = "DELETE FROM USERS WHERE ID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }
}