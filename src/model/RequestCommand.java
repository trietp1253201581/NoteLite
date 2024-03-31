package model;

/**
 * Định nghĩa một đối tượng để biểu diễn các request
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class RequestCommand {
    private String serviceName;
    private String data;
    
    /**
     * Khởi tạo một RequestCommand với tên service và data dưới dạng String
     * @param serviceName tên service
     * @param data dữ liệu dưới dạng String
     */
    public RequestCommand(String serviceName, String data) {
        this.serviceName = serviceName;
        this.data = data;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getData() {
        return data;
    }

    /**
     * Chuyển RequestCommand thành String
     * @param requestCommand RequestCommand cần chuyển
     * @return String dưới dạng {@code "serviceName:::data"}
     */
    public static String toString(RequestCommand requestCommand) {
        return requestCommand.getServiceName() + ":::" + requestCommand.getData();
    }
     
    /**
     * Chuyển String thành RequestCommand
     * @param str String dưới dạng {@code "serviceName:::data"}
     * @return Một RequestCommand thể hiện cho String request đó
     */
    public static RequestCommand valueOf(String str) {
        //Chia String thành các phần tương ứng
        String[] strarr = str.split(":::");
        String serviceName = strarr[0];
        String data = strarr[1];
        //Tạo một RequestCommand
        RequestCommand requestCommand = new RequestCommand(serviceName, data);
        
        return requestCommand;
    }
}