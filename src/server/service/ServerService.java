package server.service;

/**
 * Định nghĩa các phương thức xử lý service bên server
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public interface ServerService {   

    /**
     * Set data cho các service qua một String
     * @param data String miêu tả data phụ thuộc vào từng service
     */
    void setData(String data);

    /**
     * Thực thi service
     * @return Kết quả của việc thực thi
     */
    String execute();   
}