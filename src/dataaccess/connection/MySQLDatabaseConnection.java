package dataaccess.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Triển khai phương thức tới CSDL
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class MySQLDatabaseConnection implements DatabaseConnection {
    
    /**
     * Lấy kết nối JDBC
     * @return Đối tượng {@code Connection} connect tới cơ sở dữ liệu MySQL
     */
    @Override
    public Connection getJDBCConnection() {
        //Các thông tin để kết nối
        String url = "jdbc:mysql://localhost:3306/network";
        String username = "root";
        String password = "Asensio1234@";
        //Gọi đối tượng Driver để kết nối
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Tạo 1 connection
        Connection connection = null;
        //Connect tới database với các thông tin đã cho
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Trả về connection
        return connection;
    }
}
