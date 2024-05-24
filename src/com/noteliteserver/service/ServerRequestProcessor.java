package com.noteliteserver.service;

import com.notelitemodel.Command;
import java.util.HashMap;
import java.util.Map;

/**
 * Xử lý request được nhận
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class ServerRequestProcessor {
    private ServerRequestProcessor() {
    }
    
    private static class SingletonHelper {
        private static final ServerRequestProcessor INSTANCE = new ServerRequestProcessor();
    }
    
    public static ServerRequestProcessor getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    /**
     * Xử lý request
     * @param command Một String miêu tả request
     * @return Kết quả của việc xử lý request của service được gọi
     */
    public String process(String command) {
        //Lấy serviceName và data
        Map<String, String> dataMap = Command.decode(command);
        String serviceName = dataMap.get("serviceName");
        Map<String, Object> resultMap;
        try {
            //Gọi service
            ServerService serverService = ServerServiceInvoker.invoke(serviceName);
            //Thiết lập data cho service
            serverService.setData(dataMap);
            //Thực thi service và lấy kết quả
            resultMap = serverService.execute();
        } catch (IllegalArgumentException ex) {
            resultMap = new HashMap<>();
            resultMap.put("ServerServiceError", ex.getMessage());
        }   
        return Command.encode("Result", resultMap);
    }
}