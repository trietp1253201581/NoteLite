package com.noteliteserver.application;

import com.notelitemodel.NetworkProperty;
import com.noteliteserver.network.MultiThreadServer;
import com.noteliteserver.network.Server;
import java.io.IOException;

/**
 * Class để chạy server phục vụ user
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class NoteLiteServer {
    public static void main(String[] args) {
        try {
            //Tạo và chạy server
            Server server = new MultiThreadServer(NetworkProperty.PORT_NUMBER); 
            server.runServer();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }   
}