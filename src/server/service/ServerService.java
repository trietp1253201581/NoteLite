package server.service;

import java.util.Map;

/**
 * Định nghĩa các phương thức xử lý service bên server
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public interface ServerService {   

    /**
     * Set data cho các service qua một String
     * @param paramMap Một Map miêu tả các param và giá trị của chúng
     */
    void setData(Map<String, String> paramMap);

    /**
     * Thực thi service
     * @return Kết quả của việc thực thi
     */
    Map<String, Object> execute();   
}