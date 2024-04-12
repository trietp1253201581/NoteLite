package client.service;

import client.networking.ClientRequestProcessor;
import java.io.IOException;
import model.Note;
import model.RequestCommand;
import model.ShareNote;
import model.User;

/**
 * Cung cấp các service cần kết nối với server
 * @author Lê Minh Triết
 * @since 05/04/2024
 * @version 1.0
 */
public class ClientServerService {
    private static String serviceName;
    private static String data;
    
    /**
     * Kiểm tra thông tin đăng nhập
     * @param username username được nhập
     * @param password password được nhập
     * @return Kết quả thực thi service này từ server     
     */
    public static String checkLogin(String username, String password)  {
        //Tạo thông tin cho process
        serviceName = "CheckLogin";
        data = username + ";;;" + password;
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Tạo một user mới
     * @param newUser user mới
     * @return Kết quả thực thi service này từ server     
     */
    public static String createUser(User newUser)  {
        //Tạo thông tin cho process
        serviceName = "CreateUser";
        data = User.toString(newUser);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Cập nhật User
     * @param user user cần cập nhật
     * @return Kết quả thực thi service này từ server    
     */
    public static String updateUser(User user)  {
        //Tạo thông tin cho process
        serviceName = "UpdateUser";
        data = User.toString(user);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Mở Note chỉnh sửa gần nhất
     * @param author user sở hữu note
     * @return Kết quả thực thi service này từ server    
     */
    public static String openLastNote(String author)  {
        //Tạo thông tin cho process
        serviceName = "OpenLastNote";
        data = author;
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Tạo một Note mới
     * @param newNote Note mới
     * @return Kết quả thực thi service này từ server     
     */
    public static String createNote(Note newNote)  {
        //Tạo thông tin cho process
        serviceName = "CreateNote";
        data = Note.toString(newNote);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }

    /**
     * Mở Note
     * @param author user sở hữu note
     * @param header header của note cần mở
     * @return Kết quả thực thi service này từ server     
     */
    public static String openNote(String author, String header)  {
        //Tạo thông tin cho process
        serviceName = "OpenNote";
        data = author + ";;;" + header;
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Xóa một Note
     * @param author user sở hữu note
     * @param header header của note cần xóa
     * @return Kết quả thực thi service này từ server     
     */
    public static String deleteNote(String author, String header)  {
        //Tạo thông tin cho process
        serviceName = "DeleteNote";
        data = author + ";;;" + header;
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Lưu Note
     * @param note note cần lưu
     * @return Kết quả thực thi service này từ server
     */
    public static String saveNote(Note note) {
        //Tạo thông tin cho process
        serviceName = "SaveNote";
        data = Note.toString(note);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Lấy tất cả các note của user
     * @param author user sở hữu note
     * @return Kết quả thực thi service này từ server
     */
    public static String getAllNotes(String author) {
        //Tạo thông tin cho process
        serviceName = "GetAllNotes";
        data = author;
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Send Note
     * @param shareNote ShareNote biểu diễn việc share note
     * @return Kết quả thực thi service này từ server 
     */
    public static String sendNote(ShareNote shareNote) {
        //Tạo thông tin
        serviceName = "SendNote";
        data = ShareNote.toString(shareNote);
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Lấy tất cả các note được share
     * @param receiver người nhận
     * @return Kết quả thực thi service này từ server
     */
    public static String getAllReceivedNotes(String receiver) {
        //Tạo thông tin
        serviceName = "GetAllReceivedNotes";
        data = receiver;
        //Gửi yêu cầu và nhận phản hồi
        return requestAndGetReply();
    }
    
    /**
     * Gửi yêu cầu và nhận phản hồi
     */
    private static String requestAndGetReply() {
        //Tạo request
        RequestCommand requestCommand = new RequestCommand(serviceName, data);
        //Gửi yêu cầu, nhận phản hồi
        return ClientRequestProcessor.process(requestCommand);
    }
}
