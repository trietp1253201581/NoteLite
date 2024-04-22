package server.service;

import java.util.Map;
import model.command.Command;

/**
 * Xử lý request được nhận
 * @author Lê Minh Triết
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
        //Gọi service
        ServerService serverService = ServerServiceInvoker.invoke(serviceName);
        //Thiết lập data cho service
        serverService.setData(dataMap);
        //Thực thi service và lấy kết quả
        Map<String, Object> resultMap = serverService.execute();     
        return Command.encode("Result", resultMap);
    }
}
