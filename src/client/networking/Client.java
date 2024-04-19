package client.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Cung cấp các phương thức để tạo, chạy, hủy một Client
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class Client {   
    private Socket clientSocket;
    private InetAddress address;
    private InetAddress host;
    private int portNumber;
    private String message;
    private String result;
    public static final String END_STRING_COMMAND = "end";

    /**
     * Khởi tạo Client
     * @param host một {@code InetAddress} chứa địa chỉ máy chủ
     * @param portNumber cổng kết nối
     * @throws IOException
     */
    public Client(InetAddress host, int portNumber) throws IOException {
        this.host = host;
        this.portNumber = portNumber;
        this.clientSocket = new Socket(host, portNumber);
        this.address = InetAddress.getLocalHost();
    }

    public InetAddress getAddress() {
        return address;
    }

    public String getMessage() {
        return message;
    }

    public String getResult() {
        return result;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Hủy Client
     * @throws IOException
     */
    public void closeClient() throws IOException {
        clientSocket.close();
    }
    
    /**
     * Chạy Client
     * @throws IOException
     */
    public void runClient() throws IOException {
        //Tạo các đối tượng để nhận và truyền dữ liệu qua Stream
        DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        //Gửi dữ liệu về Server
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
        //Nhận dữ liệu từ Server và gán vào result
        result = dataInputStream.readUTF();
        //Thông báo kết thúc việc truyền, nhận dữ liệu tới Server
        dataOutputStream.writeUTF(END_STRING_COMMAND);
        //Đóng các stream
        dataInputStream.close();
        dataOutputStream.close();
    }
}
