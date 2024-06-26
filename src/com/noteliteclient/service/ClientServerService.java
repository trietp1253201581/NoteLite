package com.noteliteclient.service;

import com.noteliteclient.network.Client;
import com.notelitemodel.Command;
import com.notelitemodel.datatransfer.Note;
import com.notelitemodel.datatransfer.ShareNote;
import com.notelitemodel.datatransfer.User;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cung cấp các service cần kết nối với server
 * @author Nhóm 23
 * @since 05/04/2024
 * @version 1.0
 */
public class ClientServerService {
    private String serviceName;
    private Client client;
    private Map<String, Object> paramMap;
    private Map<String, String> resultMap;
    
    public static enum ServerService {
        CheckLogin,
        UpdateUser,
        CreateUser,
        CreateNote,
        SaveNote,
        DeleteNote,
        GetAllNotes,
        OpenNote,
        OpenLastNote,
        SendNote,
        GetAllReceivedNotes
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    /**
     * Kiểm tra thông tin đăng nhập
     * @param username username được nhập
     * @param password password được nhập
     * @return Kết quả thực thi service này từ server     
     * @throws ClientServerServiceException nếu không thể kết nối tới server
     * hoặc không thực thi được câu lệnh, hoặc các thông tin bị sai.
     */
    public User checkLogin(String username, String password) throws ClientServerServiceException {
        //Tạo thông tin cho process
        serviceName = ServerService.CheckLogin.toString();
        paramMap = new HashMap<>();
        paramMap.put("username", username);
        paramMap.put("password", password);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Cuối cùng lấy User
        User user = User.valueOf(resultMap.get("User"));
        return user;
    }
    
    /**
     * Tạo một user mới
     * @param user user mới
     * @return Kết quả thực thi service này từ server     
     * @throws ClientServerServiceException nếu không thể kết nối tới server, hoặc 
     * không thực thi được lệnh, hoặc User này đã tồn tại.
     */
    public User createUser(User user) throws ClientServerServiceException {
        //Tạo thông tin cho process
        serviceName = ServerService.CreateUser.toString();
        paramMap = new HashMap<>();
        paramMap.put("user", user);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Cuối cùng lấy User
        User newUser = User.valueOf(resultMap.get("User"));
        return newUser;
    }
    
    /**
     * Cập nhật User
     * @param user user cần cập nhật
     * @return Kết quả thực thi service này từ server    
     * @throws ClientServerServiceException nếu không thể kết nối tới server, hoặc
     * không thực thi được lệnh.
     */
    public User updateUser(User user) throws ClientServerServiceException {
        //Tạo thông tin cho process
        serviceName = ServerService.UpdateUser.toString();
        paramMap = new HashMap<>();
        paramMap.put("user", user);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Cuối cùng lấy User
        User updatedUser = User.valueOf(resultMap.get("User"));
        return updatedUser;
    }
    
    /**
     * Mở Note chỉnh sửa gần nhất
     * @param author user sở hữu note
     * @return Kết quả thực thi service này từ server    
     * @throws ClientServerServiceException nếu không thể kết nối tới server, hoặc
     * không thực thi được lệnh, hoặc author này chưa có note nào.  
     */
    public Note openLastNote(String author) throws ClientServerServiceException {
        //Tạo thông tin cho process
        serviceName = ServerService.OpenLastNote.toString();
        paramMap = new HashMap<>();
        paramMap.put("author", author);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về Note được mở
        Note note = Note.valueOf(resultMap.get("Note"));
        return note;
    }
    
    /**
     * Tạo một Note mới
     * @param note Note mới
     * @return Kết quả thực thi service này từ server     
     * @throws ClientServerServiceException nếu không thể kết nối tới server, hoặc
     * không thực thi được lệnh, hoặc note này đã tồn tại    
     */
    public Note createNote(Note note) throws ClientServerServiceException {
        //Tạo thông tin cho process
        serviceName = ServerService.CreateNote.toString();
        paramMap = new HashMap<>();
        paramMap.put("note", note);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về Note được mở
        Note newNote = Note.valueOf(resultMap.get("Note"));
        return newNote;
    }

    /**
     * Mở Note
     * @param author user sở hữu note
     * @param header header của note cần mở
     * @return Kết quả thực thi service này từ server     
     * @throws ClientServerServiceException nếu không thể kết nối tới server, hoặc
     * không thực thi được lệnh, hoặc note cần mở không tồn tại.     
     */
    public Note openNote(String author, String header) throws ClientServerServiceException {
        //Tạo thông tin cho process
        serviceName = ServerService.OpenNote.toString();
        paramMap = new HashMap<>();
        paramMap.put("author", author);
        paramMap.put("header", header);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về Note được mở
        Note note = Note.valueOf(resultMap.get("Note"));
        return note;
    }
    
    /**
     * Xóa một Note
     * @param author user sở hữu note
     * @param header header của note cần xóa
     * @return Kết quả thực thi service này từ server     
     * @throws ClientServerServiceException nếu không thể kết nối tới server, hoặc
     * không thực thi được lệnh, hoặc note cần xóa không tồn tại    
     */
    public Note deleteNote(String author, String header) throws ClientServerServiceException {
        //Tạo thông tin cho process
        serviceName = ServerService.DeleteNote.toString();
        paramMap = new HashMap<>();
        paramMap.put("author", author);
        paramMap.put("header", header);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về Note được xóa
        Note note = Note.valueOf(resultMap.get("Note"));
        return note;
    }
    
    /**
     * Lưu Note
     * @param note note cần lưu
     * @return Kết quả thực thi service này từ server
     * @throws ClientServerServiceException nếu không thể kết nối tới server, hoặc
     * không thực thi được lệnh.
     */
    public Note saveNote(Note note) throws ClientServerServiceException {
        //Tạo thông tin cho process
        serviceName = ServerService.SaveNote.toString();
        paramMap = new HashMap<>();
        paramMap.put("note", note);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về Note được lưu
        Note savedNote = Note.valueOf(resultMap.get("Note"));
        return savedNote;
    }
    
    /**
     * Lấy tất cả các note của user
     * @param author user sở hữu note
     * @return Kết quả thực thi service này từ server
     * @throws ClientServerServiceException nếu không thể kết nối tới server, hoặc
     * không thực thi được lệnh.
     */
    public List<Note> getAllNotes(String author) throws ClientServerServiceException {
        //Tạo thông tin cho process
        serviceName = ServerService.GetAllNotes.toString();
        paramMap = new HashMap<>();
        paramMap.put("author", author);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về List các Note của author
        String listNoteString = resultMap.get("ListNote");
        List<Note> notes = Note.ListOfNotesConverter.convertToList(listNoteString);
        return notes;
    }
    
    /**
     * Send Note
     * @param shareNote ShareNote biểu diễn việc share note
     * @return Kết quả thực thi service này từ server 
     * @throws ClientServerServiceException nếu không thể kết nối tới server, hoặc
     * không thực thi được lệnh.
     */
    public ShareNote sendNote(ShareNote shareNote) throws ClientServerServiceException {
        //Tạo thông tin
        serviceName = ServerService.SendNote.toString();
        paramMap = new HashMap<>();
        paramMap.put("shareNote", shareNote);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về ShareNote được mở
        ShareNote newShareNote = ShareNote.valueOf(resultMap.get("ShareNote"));
        return newShareNote;
    }
    
    /**
     * Lấy tất cả các note được share
     * @param receiver người nhận
     * @return Kết quả thực thi service này từ server
     * @throws ClientServerServiceException nếu không thể kết nối tới server, hoặc
     * không thực thi được lệnh.
     */
    public List<ShareNote> getAllReceivedNotes(String receiver) throws ClientServerServiceException {
        //Tạo thông tin
        serviceName = ServerService.GetAllReceivedNotes.toString();
        paramMap = new HashMap<>();
        paramMap.put("receiver", receiver);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Trả về List các Note của author
        String listShareNoteString = resultMap.get("ListShareNote");
        List<ShareNote> shareNotes = ShareNote.ListOfShareNotesConverter.convertToList(listShareNoteString);
        return shareNotes;
    }
    
    /**
     * Gửi yêu cầu và nhận phản hồi
     */
    private void requestAndGetReply() throws ClientServerServiceException {
        try {
            //Truyền message vào client và chạy client
            String message = Command.encode(serviceName, paramMap);
            client.setMessage(message);
            client.runClient();
            //Lấy reply từ server
            resultMap = new HashMap<>();
            String resultString = client.getResult();
            resultMap = Command.decode(resultString);
            //Kiểm tra xem có lỗi gửi từ server không, nếu có thì ném ra ngoại lệ
            if(resultMap.containsKey("ServerServiceError")) {
                throw new ClientServerServiceException(resultMap.get("ServerServiceError"));
            }
        } catch (UnknownHostException ex) {
            throw new ClientServerServiceException("Failed connect to server");
        } catch (IOException ex) {
            throw new ClientServerServiceException("Failed connect to server");
        }
    }
    
    /**
     * Tạo kết nối tới server
     * @param host định danh của host
     * @param port số hiệu cổng
     * @throws ClientServerServiceException 
     */
    public void createConnectToServer(String host, int port) throws ClientServerServiceException {
        try {
            client = new Client(InetAddress.getByName(host), port);
        } catch (UnknownHostException ex) {
            throw new ClientServerServiceException("Failed connect to server");
        } catch (IOException ex) {
            throw new ClientServerServiceException("Failed connect to server");
        }
    }
    
    /**
     * Ngắt kết nối tới server
     */
    public void removeConnectToServer() {
        if(client != null) {
            client.closeClient();
        }    
    }
}