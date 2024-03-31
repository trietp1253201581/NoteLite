package server.networking;

/**
 * Định nghĩa các phương thức điều khiển Server
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public interface Server {  

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
