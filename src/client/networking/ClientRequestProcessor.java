package client.networking;

import client.service.ClientServerServiceErrorType;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import model.RequestCommand;

/**
 * Cung cấp phương thức để yêu cầu server
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class ClientRequestProcessor {   
    //Khởi tạo host và cổng kết nối
    private static final String HOST = "localhost";
    private static final int PORT_NUMBER = 2222;
   
    /**
     * Gửi yêu cầu tới server và nhận lại phản hồi
     * @param requestCommand một {@code RequestCommand} miêu tả process từ user 
     * @return phản hồi từ server
     */
    public static String process(RequestCommand requestCommand) { 
        try {
            //Tạo 1 client để truyền dữ liệu
            Client client = new Client(InetAddress.getByName(HOST), PORT_NUMBER);
            //Truyền message vào client và chạy client
            client.setMessage(RequestCommand.toString(requestCommand));
            client.runClient();
            client.closeClient();
            //Lấy reply từ server
            return client.getResult();
        } catch (UnknownHostException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return ClientServerServiceErrorType.FAILED_CONNECT_TO_SERVER.toString();
    }  
}
