package com.noteliteserver.dataaccess;

import com.notelitemodel.datatransfer.Note;
import com.notelitemodel.datatransfer.ShareNote;
import com.noteliteserver.dataaccess.connection.DatabaseConnection;
import com.noteliteserver.dataaccess.connection.MySQLDatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Triển khai các phương thức thao tác dữ liệu với ShareNote
 * @author Nhóm 23
 * @since 06/04/2024
 * @version 1.0
 */
public class ShareNoteDataAccess implements SpecialShareNoteDataAccess {
    private final Connection connection;
    private final SpecialNoteDataAccess noteDataAccess;

    private ShareNoteDataAccess() {
        DatabaseConnection connectSQLDatabase = new MySQLDatabaseConnection();
        this.connection = connectSQLDatabase.getJDBCConnection();
        noteDataAccess = NoteDataAccess.getInstance();
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
        String query = "SELECT sh.ID as SHAREID, NOTEID, us.USERNAME as RECEIVER, "
                + "SHARETYPE FROM sharenotes sh, users us "
                + "WHERE sh.RECEIVERID = us.ID AND us.USERNAME = ?";
        
        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, receiver);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                ShareNote shareNote = new ShareNote();
                //Set dữ liệu từ hàng vào shareNote
                int noteId = resultSet.getInt("NOTEID");
                Note note = noteDataAccess.get(noteId);
                if(!note.isDefaultValue()) {
                    shareNote.setNote(note);
                    shareNote.setShareId(resultSet.getInt("SHAREID"));
                    shareNote.setReceiver(resultSet.getString("RECEIVER"));
                    shareNote.setShareType(ShareNote.ShareType.valueOf(resultSet.getString("SHARETYPE")));
                }
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
     * @param noteId id của Note được share
     * @param receiver người nhận
     * @return ShareNote duy nhất được lấy
     */
    @Override
    public ShareNote get(int noteId, String receiver) {
        //Kiểm tra null
        if(connection == null) {
            return new ShareNote();
        }
        //Câu truy vấn SQL
        String query = "SELECT sh.ID as SHAREID, NOTEID, us.USERNAME as RECEIVER, "
                + "SHARETYPE FROM sharenotes sh, users us "
                + "WHERE sh.RECEIVERID = us.ID AND NOTEID = ? AND us.USERNAME = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, noteId);
            preparedStatement.setString(2, receiver);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ShareNote shareNote = new ShareNote();
                //Set dữ liệu cho shareNote
                Note note = noteDataAccess.get(noteId);
                if(note.isDefaultValue()) {
                    return new ShareNote();
                }
                shareNote.setNote(note);
                shareNote.setShareId(resultSet.getInt("SHAREID"));
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
                shareNote.setShareType(ShareNote.ShareType.valueOf(resultSet.getString("SHARETYPE")));
                
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
        String query = "SELECT sh.ID as SHAREID, NOTEID, us.USERNAME as RECEIVER, "
                + "SHARETYPE FROM sharenotes sh, users us "
                + "WHERE sh.RECEIVERID = us.ID AND sh.ID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ShareNote shareNote = new ShareNote();
                //Set dữ liệu cho shareNote
                int noteId = resultSet.getInt("NOTEID");
                Note note = noteDataAccess.get(noteId);
                if(note.isDefaultValue()) {
                    return new ShareNote();
                }
                shareNote.setNote(note);
                shareNote.setShareId(resultSet.getInt("SHAREID"));
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
                shareNote.setShareType(ShareNote.ShareType.valueOf(resultSet.getString("SHARETYPE")));
                
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
        String query = "UPDATE SHARENOTES SET NOTEID = ?, RECEIVERID = ?, "
                + "SHARETYPE = ? WHERE ID = ?";

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
        String query = "DELETE FROM SHARENOTES WHERE ID = ?";

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
    public static void main(String[] args) {
        ShareNoteDataAccess dataAccess = new ShareNoteDataAccess();
        ShareNote shareNotes = dataAccess.get(2030, "phuongsc268");
        System.out.println(shareNotes);
    }
}