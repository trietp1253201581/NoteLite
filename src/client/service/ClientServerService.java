package client.service;

import client.networking.Client;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import model.datatransfer.Note;
import model.datatransfer.ShareNote;
import model.datatransfer.User;
import model.command.Command;

/**
 * Cung cấp các service cần kết nối với server
 * @author Lê Minh Triết
 * @since 05/04/2024
 * @version 1.0
 */
public class ClientServerService {
    private String serviceName;
    private String message;
    private Client client;
    private Map<String, Object> paramMap;
    
    /**
     * Kiểm tra thông tin đăng nhập
     * @param username username được nhập
     * @param password password được nhập
     * @return Kết quả thực thi service này từ server     
     */
    public String checkLogin(String username, String password) {
        //Tạo thông tin cho process
        serviceName = "CheckLogin";
        paramMap = new HashMap<>();
        paramMap.put("username", username);
        paramMap.put("password", password);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Tạo một user mới
     * @param user user mới
     * @return Kết quả thực thi service này từ server     
     */
    public String createUser(User user) {
        //Tạo thông tin cho process
        serviceName = "CreateUser";
        paramMap = new HashMap<>();
        paramMap.put("user", user);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Cập nhật User
     * @param user user cần cập nhật
     * @return Kết quả thực thi service này từ server    
     */
    public String updateUser(User user) {
        //Tạo thông tin cho process
        serviceName = "UpdateUser";
        paramMap = new HashMap<>();
        paramMap.put("user", user);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Mở Note chỉnh sửa gần nhất
     * @param author user sở hữu note
     * @return Kết quả thực thi service này từ server    
     */
    public String openLastNote(String author) {
        //Tạo thông tin cho process
        serviceName = "OpenLastNote";
        paramMap = new HashMap<>();
        paramMap.put("author", author);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Tạo một Note mới
     * @param note Note mới
     * @return Kết quả thực thi service này từ server     
     */
    public String createNote(Note note) {
        //Tạo thông tin cho process
        serviceName = "CreateNote";
        paramMap = new HashMap<>();
        paramMap.put("note", note);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }

    /**
     * Mở Note
     * @param author user sở hữu note
     * @param header header của note cần mở
     * @return Kết quả thực thi service này từ server     
     */
    public String openNote(String author, String header) {
        //Tạo thông tin cho process
        serviceName = "OpenNote";
        paramMap = new HashMap<>();
        paramMap.put("author", author);
        paramMap.put("header", header);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Xóa một Note
     * @param author user sở hữu note
     * @param header header của note cần xóa
     * @return Kết quả thực thi service này từ server     
     */
    public String deleteNote(String author, String header) {
        //Tạo thông tin cho process
        serviceName = "DeleteNote";
        paramMap = new HashMap<>();
        paramMap.put("author", author);
        paramMap.put("header", header);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Lưu Note
     * @param note note cần lưu
     * @return Kết quả thực thi service này từ server
     */
    public String saveNote(Note note) {
        //Tạo thông tin cho process
        serviceName = "SaveNote";
        paramMap = new HashMap<>();
        paramMap.put("note", note);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Lấy tất cả các note của user
     * @param author user sở hữu note
     * @return Kết quả thực thi service này từ server
     */
    public String getAllNotes(String author) {
        //Tạo thông tin cho process
        serviceName = "GetAllNotes";
        paramMap = new HashMap<>();
        paramMap.put("author", author);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Send Note
     * @param shareNote ShareNote biểu diễn việc share note
     * @return Kết quả thực thi service này từ server 
     */
    public String sendNote(ShareNote shareNote) {
        //Tạo thông tin
        serviceName = "SendNote";
        paramMap = new HashMap<>();
        paramMap.put("shareNote", shareNote);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Lấy tất cả các note được share
     * @param receiver người nhận
     * @return Kết quả thực thi service này từ server
     */
    public String getAllReceivedNotes(String receiver) {
        //Tạo thông tin
        serviceName = "GetAllReceivedNotes";
        paramMap = new HashMap<>();
        paramMap.put("receiver", receiver);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Gửi yêu cầu và nhận phản hồi
     */
    private String requestAndGetReply() {
        //Gửi yêu cầu, nhận phản hồi
        try {
            //Tạo 1 client để truyền dữ liệu
            String host = "localhost";
            int port = 2222;
            client = new Client(InetAddress.getByName(host), port);
            //Truyền message vào client và chạy client
            message = Command.encode(serviceName, paramMap);
            client.setMessage(message);
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
