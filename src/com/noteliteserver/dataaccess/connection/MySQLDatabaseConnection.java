package com.noteliteserver.dataaccess.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Triển khai phương thức tới CSDL
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class MySQLDatabaseConnection implements DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/network";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Asensio1234@";
    /**
     * Lấy kết nối JDBC
     * @return Đối tượng {@code Connection} connect tới cơ sở dữ liệu MySQL
     */
    @Override
    public Connection getJDBCConnection() {
        //Gọi đối tượng Driver để kết nối
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e);
        }
        //Tạo 1 connection
        Connection connection = null;
        //Connect tới database với các thông tin đã cho
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println(e);
        }
        //Trả về connection
        return connection;
    }
}