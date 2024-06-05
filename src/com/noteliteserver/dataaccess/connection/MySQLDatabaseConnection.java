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
    private final String url;
    private final String username;
    private final String password; 

    public MySQLDatabaseConnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    public MySQLDatabaseConnection(String host, int port, String database, String username, String password) {
        url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.username = username;
        this.password = password;
    }
    
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
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println(e);
        }
        //Trả về connection
        return connection;
    }
}