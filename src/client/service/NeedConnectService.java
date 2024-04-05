package client.service;

import client.networking.ClientRequest;
import java.io.IOException;
import model.Note;
import model.RequestCommand;
import model.User;

/**
 * Cung cấp các service cần kết nối với server
 * @author Lê Minh Triết
 * @since 05/04/2024
 * @version 1.0
 */
public class NeedConnectService {
    private static String serviceName;
    private static String data;
    private static String replyFromServer;
    
    /**
     * Kiểm tra thông tin đăng nhập
     * @param username username được nhập
     * @param password password được nhập
     * @return Kết quả thực thi service này từ server
     * @throws IOException 
     */
    public static String checkLogin(String username, String password) throws IOException {
        //Tạo thông tin cho requestAndGetReply
        serviceName = "CheckLogin";
        data = username + ";;;" + password;
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về phản hồi
        return replyFromServer;
    }
    
    /**
     * Tạo một user mới
     * @param newUser user mới
     * @return Kết quả thực thi service này từ server
     * @throws IOException 
     */
    public static String createUser(User newUser) throws IOException {
        //Tạo thông tin cho requestAndGetReply
        serviceName = "CreateUser";
        data = User.toString(newUser);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về phản hồi
        return replyFromServer;
    }
    
    /**
     * Cập nhật User
     * @param user user cần cập nhật
     * @return Kết quả thực thi service này từ server
     * @throws IOException 
     */
    public static String updateUser(User user) throws IOException {
        //Tạo thông tin cho requestAndGetReply
        serviceName = "UpdateUser";
        data = User.toString(user);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về phản hồi
        return replyFromServer;
    }
    
    /**
     * Mở Note chỉnh sửa gần nhất
     * @param userId id của user
     * @return Kết quả thực thi service này từ server
     * @throws IOException 
     */
    public static String openLastNote(int userId) throws IOException {
        //Tạo thông tin cho requestAndGetReply
        serviceName = "OpenLastNote";
        data = String.valueOf(userId);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về phản hồi
        return replyFromServer;
    }
    
    /**
     * Tạo một Note mới
     * @param newNote Note mới
     * @return Kết quả thực thi service này từ server
     * @throws IOException 
     */
    public static String createNote(Note newNote) throws IOException {
        //Tạo thông tin cho requestAndGetReply
        serviceName = "CreateNote";
        data = Note.toString(newNote);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về phản hồi
        return replyFromServer;
    }

    /**
     * Mở Note
     * @param userId id của user
     * @param header header của note cần mở
     * @return Kết quả thực thi service này từ server
     * @throws IOException 
     */
    public static String openNote(int userId, String header) throws IOException {
        //Tạo thông tin cho requestAndGetReply
        serviceName = "OpenNote";
        data = userId + ";;;" + header;
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về phản hồi
        return replyFromServer;
    }
    
    /**
     * Xóa một Note
     * @param userId id của user
     * @param header header của note cần xóa
     * @return Kết quả thực thi service này từ server
     * @throws IOException 
     */
    public static String deleteNote(int userId, String header) throws IOException {
        //Tạo thông tin cho requestAndGetReply
        serviceName = "DeleteNote";
        data = userId + ";;;" + header;
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về phản hồi
        return replyFromServer;
    }
    
    /**
     * Lưu Note
     * @param note note cần lưu
     * @return Kết quả thực thi service này từ server
     * @throws IOException 
     */
    public static String saveNote(Note note) throws IOException {
        //Tạo thông tin cho requestAndGetReply
        serviceName = "CreateNote";
        data = Note.toString(note);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về phản hồi
        return replyFromServer;
    }
    
    /**
     * Lấy tất cả các note của user
     * @param userId id của user
     * @return Kết quả thực thi service này từ server
     * @throws IOException 
     */
    public static String getAllNotes(int userId) throws IOException {
        //Tạo thông tin cho requestAndGetReply
        serviceName = "GetAllNotes";
        data = String.valueOf(userId);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về phản hồi
        return replyFromServer;
    }
    
    /**
     * Gửi yêu cầu và nhận phản hồi
     * @throws IOException 
     */
    private static void requestAndGetReply() throws IOException{
        //Tạo request
        RequestCommand requestCommand = new RequestCommand(serviceName, data);
        //Gửi và nhận về phản hồi từ server
        replyFromServer = ClientRequest.requestAndGetReply(requestCommand);
    }
}
