package com.noteliteserver.dataaccess;

import com.notelitemodel.NetworkProperty;
import com.notelitemodel.datatransfer.User;
import com.noteliteserver.dataaccess.connection.DatabaseConnection;
import com.noteliteserver.dataaccess.connection.MySQLDatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Triển khai các phương thức thao tác dữ liệu với User
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class UserDataAccess implements BasicDataAccess<User, UserKey, NullKey> {
    private final Connection connection;
    protected DatabaseConnection databaseConnection;

    private UserDataAccess() {
        String host = NetworkProperty.DATABASE_HOST;
        int port = NetworkProperty.DATABASE_PORT;
        String dbName = NetworkProperty.DATABASE_NAME;
        String username = NetworkProperty.DATABASE_USERNAME;
        String password = NetworkProperty.DATABASE_PASSWORD;
        databaseConnection = new MySQLDatabaseConnection
            (host, port, dbName, username, password);
        this.connection = databaseConnection.getConnection();
    }
    
    private static class SingletonHelper {
        private static final UserDataAccess INSTANCE = new UserDataAccess();
    }
    
    /**
     * Lấy thể hiện duy nhất của lớp này
     * @return Instance duy nhất
     */
    public static UserDataAccess getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    @Override
    public List<User> getAll() throws DataAccessException {
        List<User> users = new ArrayList<>();
        //Kiểm tra connection có phải null không
        if(connection == null) {
            throw new FailedExecuteException();
        }
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
                user.setName(resultSet.getString("NAME"));
                user.setUsername(resultSet.getString("USERNAME"));
                user.setPassword(resultSet.getString("PASSWORD"));
                user.setBirthday(Date.valueOf(resultSet.getString("BIRTHDAY")));
                user.setSchool(resultSet.getString("SCHOOL"));
                user.setGender(User.Gender.valueOf(resultSet.getString("GENDER")));

                users.add(user);
            }    
            //Nếu users rỗng thì ném ngoại lệ là danh sách trống
            if(users.isEmpty()) {
                throw new NotExistDataException("Empty users list!");
            }   
            return users;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }

    @Override
    public List<User> getAll(NullKey referKey) throws DataAccessException {
        return this.getAll();
    }

    @Override
    public User get(UserKey key) throws DataAccessException {
        User user = new User();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT * FROM USERS WHERE USERNAME = ?";
        try {
            //Thực thi truy vấn SQL, gán tham số cho USERNAME và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, key.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //Set dữ liệu từ hàng nhận được
                user.setName(resultSet.getString("NAME"));
                user.setUsername(resultSet.getString("USERNAME"));
                user.setPassword(resultSet.getString("PASSWORD"));
                user.setBirthday(Date.valueOf(resultSet.getString("BIRTHDAY")));
                user.setSchool(resultSet.getString("SCHOOL"));
                user.setGender(User.Gender.valueOf(resultSet.getString("GENDER")));
            }
            //Nếu ko tồn tại thì ném ra ngoại lệ
            if(user.isDefaultValue()) {
                throw new NotExistDataException("User is not exist!");
            }
            return user;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }       
    }

    @Override
    public void add(User user) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "INSERT INTO USERS(NAME, USERNAME, PASSWORD, BIRTHDAY, SCHOOL, GENDER) "
                + "VALUES(?,?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setDate(4, user.getBirthday());
            preparedStatement.setString(5, user.getSchool());
            preparedStatement.setString(6, user.getGender().toString());
            
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void add(User user, UserKey key) throws DataAccessException {
        this.add(user);
    }

    @Override
    public void update(User user) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "UPDATE USERS SET NAME = ?, PASSWORD = ?, "
                + "BIRTHDAY = ?, SCHOOL = ?, GENDER = ? WHERE USERNAME = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setDate(3, user.getBirthday());
            preparedStatement.setString(4, user.getSchool());
            preparedStatement.setString(5, user.getGender().toString());
            preparedStatement.setString(6, user.getUsername());

            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void update(User user, UserKey userKey) throws DataAccessException {
        this.update(user);
    }

    @Override
    public void delete(UserKey key) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM USERS WHERE USERNAME = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setString(1, key.getUsername());

            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override 
    public void deleteAll(NullKey referKey) throws DataAccessException {
        throw new FailedExecuteException();
    }
}