package com.noteliteserver.network;

/**
 * Định nghĩa các phương thức điều khiển Server
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public interface Server {  
    
    /**
     * Các status của server
     */
    enum ServerStatus{
        CLOSE, WAITING, SUCCESS; 
    }
    
    /**
     * Chạy Server
     */
    void runServer();   

    /**
     * Đóng Server
     */
    void closeServer();

    /**
     * Xem status của Server
     * @return Một ServerStatus miêu tả trạng thái của server
     */
    ServerStatus getStatus();   
}