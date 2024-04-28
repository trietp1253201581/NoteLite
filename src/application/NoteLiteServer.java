package application;

import java.io.IOException;
import server.networking.MultiThreadServer;
import server.networking.Server;

/**
 * Class để chạy server phục vụ user
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class NoteLiteServer {
    //Cổng kết nối
    public static final int PORT_NUMBER = 2222;

    public static void main(String[] args) {
        try {
            //Tạo và chạy server
            Server server = new MultiThreadServer(PORT_NUMBER); 
            server.runServer();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }   
}