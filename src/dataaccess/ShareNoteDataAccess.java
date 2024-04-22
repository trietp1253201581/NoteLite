package dataaccess;

import dataaccess.connection.DatabaseConnection;
import dataaccess.connection.MySQLDatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.datatransfer.Note;
import model.datatransfer.ShareNote;
import model.datatransfer.ShareType;
import model.datatransfer.attributeconverter.NoteFilterConverter;

/**
 * Triển khai các phương thức thao tác dữ liệu với ShareNote
 * @author Lê Minh Triết
 * @since 06/04/2024
 * @version 1.0
 */
public class ShareNoteDataAccess implements SpecialShareNoteDataAccess {
    private final Connection connection;

    private ShareNoteDataAccess() {
        DatabaseConnection connectSQLDatabase = new MySQLDatabaseConnection();
        this.connection = connectSQLDatabase.getJDBCConnection();
    }
    
    private static class SingletonHelper {
        public static final ShareNoteDataAccess INSTANCE = new ShareNoteDataAccess();
    }
    
    /**
     * Lấy thể hiện duy nhất của lớp này
     * @return Instance duy nhất
     */
    public static ShareNoteDataAccess getInstance() {
        return SingletonHelper.INSTANCE;
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
        String query = "SELECT nt.ID, us1.USERNAME as AUTHOR, HEADER, CONTENT, "
                + "LASTMODIFIED, LASTMODIFIEDDATE, FILTERS, SHAREID, us2.USERNAME as RECEIVER, "
                + "SHARETYPE FROM notes nt, sharenotes sh, users us1, users us2 "
                + "WHERE nt.USERID = us1.ID AND sh.RECEIVERID = us2.ID AND nt.ID = sh.NOTEID "
                + "AND us2.USERNAME = ?";
        
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
                shareNote.setAuthor(resultSet.getString("AUTHOR"));
                shareNote.setHeader(resultSet.getString("HEADER"));
                shareNote.setContent(resultSet.getString("CONTENT"));
                shareNote.setLastModified(resultSet.getInt("LASTMODIFIED"));
                shareNote.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                shareNote.setFilters(NoteFilterConverter.convertToList(resultSet.getString("FILTERS")));
                shareNote.setShareId(resultSet.getInt("SHAREID"));
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
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
     * @param note Note được share
     * @param receiver người nhận
     * @return ShareNote duy nhất được lấy
     */
    @Override
    public ShareNote get(Note note, String receiver) {
        //Kiểm tra null
        if(connection == null) {
            return new ShareNote();
        }
        //Câu truy vấn SQL
        String query = "SELECT nt.ID, us1.USERNAME as AUTHOR, HEADER, CONTENT, "
                + "LASTMODIFIED, LASTMODIFIEDDATE, FILTERS, SHAREID, us2.USERNAME as RECEIVER, "
                + "SHARETYPE FROM notes nt, sharenotes sh, users us1, users us2 "
                + "WHERE nt.USERID = us1.ID AND sh.RECEIVERID = us2.ID AND nt.ID = sh.NOTEID "
                + "AND nt.ID = ? AND us2.USERNAME = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, note.getId());
            preparedStatement.setString(2, receiver);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ShareNote shareNote = new ShareNote();
                //Set dữ liệu cho shareNote
                shareNote.setId(resultSet.getInt("ID"));
                shareNote.setAuthor(resultSet.getString("AUTHOR"));
                shareNote.setHeader(resultSet.getString("HEADER"));
                shareNote.setContent(resultSet.getString("CONTENT"));
                shareNote.setLastModified(resultSet.getInt("LASTMODIFIED"));
                shareNote.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                shareNote.setFilters(NoteFilterConverter.convertToList(resultSet.getString("FILTERS")));
                shareNote.setShareId(resultSet.getInt("SHAREID"));
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
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
        String query = "SELECT nt.ID, us1.USERNAME as AUTHOR, HEADER, CONTENT, "
                + "LASTMODIFIED, LASTMODIFIEDDATE, FILTERS, SHAREID, us2.USERNAME as RECEIVER, "
                + "SHARETYPE FROM notes nt, sharenotes sh, users us1, users us2 "
                + "WHERE nt.USERID = us1.ID AND sh.RECEIVERID = us2.ID AND nt.ID = sh.NOTEID "
                + "AND sh.SHAREID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ShareNote shareNote = new ShareNote();
                //Set dữ liệu cho shareNote
                shareNote.setId(resultSet.getInt("ID"));
                shareNote.setAuthor(resultSet.getString("AUTHOR"));
                shareNote.setHeader(resultSet.getString("HEADER"));
                shareNote.setContent(resultSet.getString("CONTENT"));
                shareNote.setLastModified(resultSet.getInt("LASTMODIFIED"));
                shareNote.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                shareNote.setFilters(NoteFilterConverter.convertToList(resultSet.getString("FILTERS")));
                shareNote.setShareId(resultSet.getInt("SHAREID"));
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
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
        String query = "INSERT INTO SHARENOTES(NOTEID, RECEIVERID, SHARETYPE)"
                + "VALUES(?, ?, ?)";
        
        try {
            //Lấy các dữ liệu từ bảng khác
            SpecialUserDataAccess userDataAccess = UserDataAccess.getInstance();
            int receiverId = userDataAccess.get(shareNote.getReceiver()).getId();
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, shareNote.getId());
            preparedStatement.setInt(2, receiverId);
            preparedStatement.setString(3, shareNote.getShareType().toString());
            
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
        String query = "UPDATE SHARENOTES SET NOTEID = ?, RECEIVER = ?, "
                + "SHARETYPE = ? WHERE SHAREID = ?";

        try {
            //Lấy các dữ liệu từ bảng khác
            SpecialUserDataAccess userDataAccess = UserDataAccess.getInstance();
            int receiverId = userDataAccess.get(shareNote.getReceiver()).getId();
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, shareNote.getId());
            preparedStatement.setInt(2, receiverId);
            preparedStatement.setString(3, shareNote.getShareType().toString());
            preparedStatement.setInt(4, shareNote.getShareId());

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
        String query = "DELETE FROM SHARENOTES WHERE SHAREID = ?";

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
