package server.service;

import model.RequestCommand;

/**
 * Xử lý request được nhận
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class RequestProcess {

    /**
     * Xử lý request
     * @param requestCommand Một RequestCommand miêu tả request
     * @return Kết quả của việc xử lý request của service được gọi
     */
    public static String process(RequestCommand requestCommand) {
        //Lấy serviceName và data
        String serviceName = requestCommand.getServiceName();
        String data = requestCommand.getData();
        //Gọi service
        ServerService serverService = ServerServiceInvoker.invoke(serviceName);
        //Thiết lập data cho service
        serverService.setData(data);
        //Thực thi service và lấy kết quả
        return serverService.execute();     
    }
}
