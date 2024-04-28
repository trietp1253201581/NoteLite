package client.service;

import client.networking.Client;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.command.Command;
import model.datatransfer.Note;
import model.datatransfer.ShareNote;
import model.datatransfer.User;

/**
 * Cung cấp các service cần kết nối với server
 * @author Lê Minh Triết
 * @since 05/04/2024
 * @version 1.0
 */
public class ClientServerService {
    private String serviceName;
    private Client client;
    private Map<String, Object> paramMap;
    private Map<String, String> resultMap;
    
    /**
     * Các lỗi có thể gặp đối với service
     */
    public static enum ErrorType {
        ALREADY_EXISTS,
        NOT_EXISTS,
        CAN_NOT_EXECUTE, 
        FALSE_INFORMATION,
        FAILED_CONNECT_TO_SERVER,
        UNSUPPORTED_SERVICE;
    }
    
    /**
     * Kiểm tra thông tin đăng nhập
     * @param username username được nhập
     * @param password password được nhập
     * @return Kết quả thực thi service này từ server     
     * @throws client.service.ClientServerServiceErrorException     
     */
    public User checkLogin(String username, String password) throws ClientServerServiceErrorException {
        //Tạo thông tin cho process
        serviceName = "CheckLogin";
        paramMap = new HashMap<>();
        paramMap.put("username", username);
        paramMap.put("password", password);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Kiểm tra xem có lỗi gửi từ server không, nếu có thì ném ra ngoại lệ
        if(resultMap.containsKey("ServerServiceError")) {
            throw new ClientServerServiceErrorException(
                    ErrorType.valueOf(resultMap.get("ServerServiceError")));
        }
        //Cuối cùng lấy User
        User user = User.valueOf(resultMap.get("User"));
        return user;
    }
    
    /**
     * Tạo một user mới
     * @param user user mới
     * @return Kết quả thực thi service này từ server     
     * @throws client.service.ClientServerServiceErrorException     
     */
    public User createUser(User user) throws ClientServerServiceErrorException {
        //Tạo thông tin cho process
        serviceName = "CreateUser";
        paramMap = new HashMap<>();
        paramMap.put("user", user);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Kiểm tra xem có lỗi gửi từ server không, nếu có thì ném ra ngoại lệ
        if(resultMap.containsKey("ServerServiceError")) {
            throw new ClientServerServiceErrorException(
                    ErrorType.valueOf(resultMap.get("ServerServiceError")));
        }
        //Cuối cùng lấy User
        User newUser = User.valueOf(resultMap.get("User"));
        return newUser;
    }
    
    /**
     * Cập nhật User
     * @param user user cần cập nhật
     * @return Kết quả thực thi service này từ server    
     * @throws client.service.ClientServerServiceErrorException    
     */
    public User updateUser(User user) throws ClientServerServiceErrorException {
        //Tạo thông tin cho process
        serviceName = "UpdateUser";
        paramMap = new HashMap<>();
        paramMap.put("user", user);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Kiểm tra xem có lỗi gửi từ server không, nếu có thì ném ra ngoại lệ
        if(resultMap.containsKey("ServerServiceError")) {
            throw new ClientServerServiceErrorException(
                    ErrorType.valueOf(resultMap.get("ServerServiceError")));
        }
        //Cuối cùng lấy User
        User updatedUser = User.valueOf(resultMap.get("User"));
        return updatedUser;
    }
    
    /**
     * Mở Note chỉnh sửa gần nhất
     * @param author user sở hữu note
     * @return Kết quả thực thi service này từ server    
     * @throws client.service.ClientServerServiceErrorException    
     */
    public Note openLastNote(String author) throws ClientServerServiceErrorException {
        //Tạo thông tin cho process
        serviceName = "OpenLastNote";
        paramMap = new HashMap<>();
        paramMap.put("author", author);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Kiểm tra xem có lỗi gửi từ server không, nếu có thì ném ra ngoại lệ
        if(resultMap.containsKey("ServerServiceError")) {
            throw new ClientServerServiceErrorException(
                    ErrorType.valueOf(resultMap.get("ServerServiceError")));
        }
        //Trả về Note được mở
        Note note = Note.valueOf(resultMap.get("Note"));
        return note;
    }
    
    /**
     * Tạo một Note mới
     * @param note Note mới
     * @return Kết quả thực thi service này từ server     
     * @throws client.service.ClientServerServiceErrorException     
     */
    public Note createNote(Note note) throws ClientServerServiceErrorException {
        //Tạo thông tin cho process
        serviceName = "CreateNote";
        paramMap = new HashMap<>();
        paramMap.put("note", note);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Kiểm tra xem có lỗi gửi từ server không, nếu có thì ném ra ngoại lệ
        if(resultMap.containsKey("ServerServiceError")) {
            throw new ClientServerServiceErrorException(
                    ErrorType.valueOf(resultMap.get("ServerServiceError")));
        }
        //Trả về Note được mở
        Note newNote = Note.valueOf(resultMap.get("Note"));
        return newNote;
    }

    /**
     * Mở Note
     * @param author user sở hữu note
     * @param header header của note cần mở
     * @return Kết quả thực thi service này từ server     
     * @throws client.service.ClientServerServiceErrorException     
     */
    public Note openNote(String author, String header) throws ClientServerServiceErrorException {
        //Tạo thông tin cho process
        serviceName = "OpenNote";
        paramMap = new HashMap<>();
        paramMap.put("author", author);
        paramMap.put("header", header);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Kiểm tra xem có lỗi gửi từ server không, nếu có thì ném ra ngoại lệ
        if(resultMap.containsKey("ServerServiceError")) {
            throw new ClientServerServiceErrorException(
                    ErrorType.valueOf(resultMap.get("ServerServiceError")));
        }
        //Trả về Note được mở
        Note note = Note.valueOf(resultMap.get("Note"));
        return note;
    }
    
    /**
     * Xóa một Note
     * @param author user sở hữu note
     * @param header header của note cần xóa
     * @return Kết quả thực thi service này từ server     
     * @throws client.service.ClientServerServiceErrorException     
     */
    public Note deleteNote(String author, String header) throws ClientServerServiceErrorException {
        //Tạo thông tin cho process
        serviceName = "DeleteNote";
        paramMap = new HashMap<>();
        paramMap.put("author", author);
        paramMap.put("header", header);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Kiểm tra xem có lỗi gửi từ server không, nếu có thì ném ra ngoại lệ
        if(resultMap.containsKey("ServerServiceError")) {
            throw new ClientServerServiceErrorException(
                    ErrorType.valueOf(resultMap.get("ServerServiceError")));
        }
        //Trả về Note được mở
        Note note = Note.valueOf(resultMap.get("Note"));
        return note;
    }
    
    /**
     * Lưu Note
     * @param note note cần lưu
     * @return Kết quả thực thi service này từ server
     * @throws client.service.ClientServerServiceErrorException
     */
    public Note saveNote(Note note) throws ClientServerServiceErrorException {
        //Tạo thông tin cho process
        serviceName = "SaveNote";
        paramMap = new HashMap<>();
        paramMap.put("note", note);
        //Gửi yêu cầu và nhận phản hồi
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Kiểm tra xem có lỗi gửi từ server không, nếu có thì ném ra ngoại lệ
        if(resultMap.containsKey("ServerServiceError")) {
            throw new ClientServerServiceErrorException(
                    ErrorType.valueOf(resultMap.get("ServerServiceError")));
        }
        //Trả về Note được mở
        Note savedNote = Note.valueOf(resultMap.get("Note"));
        return savedNote;
    }
    
    /**
     * Lấy tất cả các note của user
     * @param author user sở hữu note
     * @return Kết quả thực thi service này từ server
     * @throws client.service.ClientServerServiceErrorException
     */
    public List<Note> getAllNotes(String author) throws ClientServerServiceErrorException {
        //Tạo thông tin cho process
        serviceName = "GetAllNotes";
        paramMap = new HashMap<>();
        paramMap.put("author", author);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Kiểm tra xem có lỗi gửi từ server không, nếu có thì ném ra ngoại lệ
        if(resultMap.containsKey("ServerServiceError")) {
            throw new ClientServerServiceErrorException(
                    ErrorType.valueOf(resultMap.get("ServerServiceError")));
        }
        //Trả về List các Note của author
        String listNoteString = resultMap.get("ListNote");
        listNoteString = listNoteString.substring(1, listNoteString.length() - 1);
        String[] listNoteArray = listNoteString.split(Note.END_TAGS + ", ");//Ký tự ngăn cách giữa các thành phần list
        List<Note> notes = new ArrayList<>();
        for(String noteString: listNoteArray) {
            notes.add(Note.valueOf(noteString));
        }
        return notes;
    }
    
    /**
     * Send Note
     * @param shareNote ShareNote biểu diễn việc share note
     * @return Kết quả thực thi service này từ server 
     * @throws client.service.ClientServerServiceErrorException 
     */
    public ShareNote sendNote(ShareNote shareNote) throws ClientServerServiceErrorException {
        //Tạo thông tin
        serviceName = "SendNote";
        paramMap = new HashMap<>();
        paramMap.put("shareNote", shareNote);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Kiểm tra xem có lỗi gửi từ server không, nếu có thì ném ra ngoại lệ
        if(resultMap.containsKey("ServerServiceError")) {
            throw new ClientServerServiceErrorException(
                    ErrorType.valueOf(resultMap.get("ServerServiceError")));
        }
        //Trả về ShareNote được mở
        ShareNote newShareNote = ShareNote.valueOf(resultMap.get("ShareNote"));
        return newShareNote;
    }
    
    /**
     * Lấy tất cả các note được share
     * @param receiver người nhận
     * @return Kết quả thực thi service này từ server
     * @throws client.service.ClientServerServiceErrorException
     */
    public List<ShareNote> getAllReceivedNotes(String receiver) throws ClientServerServiceErrorException {
        //Tạo thông tin
        serviceName = "GetAllReceivedNotes";
        paramMap = new HashMap<>();
        paramMap.put("receiver", receiver);
        //Gửi yêu cầu và nhận phản hồi
        requestAndGetReply();
        //Kiểm tra xem có lỗi gửi từ server không, nếu có thì ném ra ngoại lệ
        if(resultMap.containsKey("ServerServiceError")) {
            throw new ClientServerServiceErrorException(
                    ErrorType.valueOf(resultMap.get("ServerServiceError")));
        }
        //Trả về List các Note của author
        String listShareNoteString = resultMap.get("ListShareNote");
        listShareNoteString = listShareNoteString.substring(1, listShareNoteString.length() - 1);
        String[] listShareNoteArray = listShareNoteString.split(ShareNote.END_TAGS + ", ");//Ký tự ngăn cách giữa các thành phần list
        List<ShareNote> shareNotes = new ArrayList<>();
        for(String shareNoteString: listShareNoteArray) {
            shareNotes.add(ShareNote.valueOf(shareNoteString));
        }
        return shareNotes;
    }
    
    /**
     * Gửi yêu cầu và nhận phản hồi
     */
    private void requestAndGetReply() throws ClientServerServiceErrorException {
        try {
            //Tạo 1 client để truyền dữ liệu
            String host = "localhost";
            int port = 2222;
            client = new Client(InetAddress.getByName(host), port);
            //Truyền message vào client và chạy client
            String message = Command.encode(serviceName, paramMap);
            client.setMessage(message);
            client.runClient();
            client.closeClient();
            //Lấy reply từ server
            resultMap = new HashMap<>();
            String resultString = client.getResult();
            resultMap = Command.decode(resultString);
        } catch (UnknownHostException ex) {
            throw new ClientServerServiceErrorException(ErrorType.FAILED_CONNECT_TO_SERVER);
        } catch (IOException ex) {
            throw new ClientServerServiceErrorException(ErrorType.FAILED_CONNECT_TO_SERVER);
        }
    }
}