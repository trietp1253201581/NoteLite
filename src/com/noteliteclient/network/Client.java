package com.noteliteclient.network;

import com.notelitemodel.NetworkProperty;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Cung cấp các phương thức để tạo, chạy, hủy một Client
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class Client {   
    private final Socket clientSocket;
    private String message;
    private String result;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    /**
     * Khởi tạo Client
     * @param host một {@code InetAddress} chứa địa chỉ máy chủ
     * @param portNumber cổng kết nối
     * @throws IOException
     */
    public Client(InetAddress host, int portNumber) throws IOException {
        this.clientSocket = new Socket(host, portNumber);
        dataInputStream = new DataInputStream(clientSocket.getInputStream());
        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public String getResult() {
        return result;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Hủy Client
     */
    public void closeClient() {
        try {
            //Thông báo kết thúc việc truyền, nhận dữ liệu tới Server
            dataOutputStream.writeUTF(NetworkProperty.END_CONNECTION_SIGNAL);
            //Đóng các stream
            dataInputStream.close();
            dataOutputStream.close();
            //đóng socket
            clientSocket.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    /**
     * Chạy Client
     * @throws IOException
     */
    public void runClient() throws IOException {
        //Tạo các đối tượng để nhận và truyền dữ liệu qua Stream
        dataInputStream = new DataInputStream(clientSocket.getInputStream());
        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        //Gửi dữ liệu về Server
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
        //Nhận dữ liệu từ Server và gán vào result
        result = dataInputStream.readUTF();
    }
}