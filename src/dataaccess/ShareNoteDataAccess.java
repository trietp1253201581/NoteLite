package dataaccess;

import dataaccess.connection.MySQLDatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ShareNote;
import model.ShareType;
import dataaccess.connection.DatabaseConnection;

/**
 * Triển khai các phương thức thao tác dữ liệu với ShareNote
 * @author Lê Minh Triết
 * @since 06/04/2024
 * @version 1.0
 */
public class ShareNoteDataAccess implements SpecialShareNoteDataAccess {
    private final Connection connection;
     
    /**
     * Khởi tạo và lấy connection tới Database
     */
    public ShareNoteDataAccess() {
        DatabaseConnection connectSQLDatabase = new MySQLDatabaseConnection();
        this.connection = connectSQLDatabase.getJDBCConnection();
    }
    
    /**
     * Lấy tất cả các ShareNote được gửi tới người nhận
     * @param receiver username của người nhận
     * @return Một list chứa các ShareNote gửi tới người nhận
     */
    @Override
    public List<ShareNote> getAllReceived(String receiver) {
        List<ShareNote> shareNotes = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            return shareNotes;
        }
        //Câu truy vấn SQL
        String query = "SELECT shr.ID, user1.USERNAME as SENDER, user2.USERNAME as RECEIVER, HEADER, SHARETYPE "
                + "FROM sharenote shr, notes nt, users user1, users user2 "
                + "WHERE shr.SENDERID = user1.ID AND shr.RECEIVERID = user2.ID "
                + "AND shr.NOTEID = nt.ID AND user2.USERNAME = ?";
        
        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, receiver);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                ShareNote shareNote = new ShareNote();
                //Set dữ liệu từ hàng vào shareNote
                shareNote.setId(resultSet.getInt("ID"));
                shareNote.setSender(resultSet.getString("SENDER"));
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
                shareNote.setHeader(resultSet.getString("HEADER"));
                shareNote.setShareType(ShareType.valueOf(resultSet.getString("SHARETYPE")));
                //Thêm shareNote vào list
                shareNotes.add(shareNote);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return shareNotes;
    }
    
    /**
     * Lấy một ShareNote
     * @param sender người gửi
     * @param receiver người nhận
     * @param header header của ShareNote
     * @return ShareNote duy nhất được lấy
     */
    @Override
    public ShareNote get(String sender, String receiver, String header) {
        //Kiểm tra null
        if(connection == null) {
            return new ShareNote();
        }
        //Câu truy vấn SQL
        String query = "SELECT shr.ID, user1.USERNAME as SENDER, user2.USERNAME as RECEIVER, HEADER, SHARETYPE "
                + "FROM sharenote shr, notes nt, users user1, users user2 "
                + "WHERE shr.SENDERID = user1.ID AND shr.RECEIVERID = user2.ID "
                + "AND shr.NOTEID = nt.ID AND user1.USERNAME = ?"
                + "AND user2.USERNAME = ? AND nt.HEADER = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, sender);
            preparedStatement.setString(2, receiver);
            preparedStatement.setString(3, header);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ShareNote shareNote = new ShareNote();
                //Set dữ liệu cho shareNote
                shareNote.setId(resultSet.getInt("ID"));
                shareNote.setSender(resultSet.getString("SENDER"));
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
                shareNote.setHeader(resultSet.getString("HEADER"));
                shareNote.setShareType(ShareType.valueOf(resultSet.getString("SHARETYPE")));
                
                return shareNote;
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        //Trả về default shareNote nếu không tìm được
        return new ShareNote();
    }
    
    /**
     * Lấy sharenoteinfo với id cho trước
     * @param id id của ShareNote cần lấy 
     * @return (1) ShareNote có id đã cho, (2) default ShareNote nếu id này không tồn tại
     */
    @Override
    public ShareNote get(int id) {
        //Kiểm tra null
        if(connection == null) {
            return new ShareNote();
        }
        //Câu truy vấn SQL
        String query = "SELECT shr.ID, user1.USERNAME as SENDER, user2.USERNAME as RECEIVER, HEADER, SHARETYPE "
                + "FROM sharenote shr, notes nt, users user1, users user2 "
                + "WHERE shr.SENDERID = user1.ID AND shr.RECEIVERID = user2.ID "
                + "AND shr.NOTEID = nt.ID AND shr.ID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ShareNote shareNote = new ShareNote();
                //Set dữ liệu cho shareNote
                shareNote.setId(resultSet.getInt("ID"));
                shareNote.setSender(resultSet.getString("SENDER"));
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
                shareNote.setHeader(resultSet.getString("HEADER"));
                shareNote.setShareType(ShareType.valueOf(resultSet.getString("SHARETYPE")));
                
                return shareNote;
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        //Trả về default shareNote nếu không tìm được
        return new ShareNote();
    }
    
    /**
     * Thêm ShareNote vào CSDL
     * @param shareNote ShareNote cần thêm vào CSDL
     * @return (1) một số tự nhiên biểu thị row count khi thực thi lệnh SQL này,
     * (2) -1 nếu không thực hiện được
     */
    @Override
    public int add(ShareNote shareNote) {
        //Kiểm tra null
        if(connection == null) {
            return -1;
        }
        //Câu truy vấn SQL
        String query = "INSERT INTO SHARENOTE(SENDERID, RECEIVERID, NOTEID, SHARETYPE)"
                + "VALUES(?, ?, ?, ?)";
        
        try {
            //Lấy các dữ liệu từ bảng khác
            SpecialUserDataAccess userDataAccess = new UserDataAccess();
            SpecialNoteDataAccess noteDataAccess = new NoteDataAccess();
            int senderId = userDataAccess.get(shareNote.getSender()).getId();
            int receiverId = userDataAccess.get(shareNote.getReceiver()).getId();
            int noteId = noteDataAccess.get(shareNote.getSender(), shareNote.getHeader()).getId();
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, senderId);
            preparedStatement.setInt(2, receiverId);
            preparedStatement.setInt(3, noteId);
            preparedStatement.setString(4, shareNote.getShareType().toString());
            
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return -1;
    }
    
    /**
     * Sửa ShareNote trong CSDL
     * @param shareNote ShareNote cần sửa
     * @return (1) một số tự nhiên biểu thị row count khi thực thi lệnh SQL này,
     * (2) -1 nếu không thực hiện được
     */
    @Override
    public int update(ShareNote shareNote) {
        //Kiểm tra null
        if(connection == null) {
            return -1;
        }
        //Câu truy vấn SQL
        String query = "UPDATE SHARENOTE SET SENDERID = ?, RECEIVERID = ?, "
                + "NOTEID = ?, SHARETYPE = ? WHERE ID = ?";

        try {
            //Lấy các dữ liệu từ bảng khác
            SpecialUserDataAccess userDataAccess = new UserDataAccess();
            SpecialNoteDataAccess noteDataAccess = new NoteDataAccess();
            int senderId = userDataAccess.get(shareNote.getSender()).getId();
            int receiverId = userDataAccess.get(shareNote.getReceiver()).getId();
            int noteId = noteDataAccess.get(shareNote.getSender(), shareNote.getHeader()).getId();
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, senderId);
            preparedStatement.setInt(2, receiverId);
            preparedStatement.setInt(3, noteId);
            preparedStatement.setString(4, shareNote.getShareType().toString());
            preparedStatement.setInt(5, shareNote.getId());

            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return -1;
    }
    
    /**
     * Xóa ShareNote trong CSDL
     * @param id id của ShareNote cần xóa
     * @return (1) một số tự nhiên biểu thị row count khi thực thi lệnh SQL này,
     * (2) -1 nếu không thực hiện được
     */
    @Override
    public int delete(int id) {
        //Kiểm tra null
        if(connection == null) {
            return -1;
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM SHARENOTE WHERE ID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return -1;
    }
}
