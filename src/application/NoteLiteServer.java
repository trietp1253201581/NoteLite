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
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //Tạo và chạy server
        Server server = new MultiThreadServer(2222);
        server.runServer(); 
    }   
}
