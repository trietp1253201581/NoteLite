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

    @Override
    public List<ShareNote> getAllReceived(String receiver) throws DataAccessException {
        List<ShareNote> shareNotes = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
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
                shareNote.setNote(note);
                shareNote.setShareId(resultSet.getInt("SHAREID"));
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
                shareNote.setShareType(ShareNote.ShareType.valueOf(resultSet.getString("SHARETYPE")));
                //Thêm shareNote vào list
                shareNotes.add(shareNote);
            }
            return shareNotes;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }

    @Override
    public ShareNote get(int noteId, String receiver) throws DataAccessException {
        ShareNote shareNote = new ShareNote();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
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
                //Set dữ liệu cho shareNote
                Note note = noteDataAccess.get(noteId);
                shareNote.setNote(note);
                shareNote.setShareId(resultSet.getInt("SHAREID"));
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
                shareNote.setShareType(ShareNote.ShareType.valueOf(resultSet.getString("SHARETYPE")));
            }
            if(shareNote.isDefaultValue()) {
                throw new NotExistDataException("This sharenote is not exist!");
            }
            return shareNote;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }

    @Override
    public ShareNote get(int id) throws DataAccessException {
        ShareNote shareNote = new ShareNote();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
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
                int noteId = resultSet.getInt("NOTEID");
                //Set dữ liệu cho shareNote
                Note note = noteDataAccess.get(noteId);
                shareNote.setNote(note);
                shareNote.setShareId(resultSet.getInt("SHAREID"));
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
                shareNote.setShareType(ShareNote.ShareType.valueOf(resultSet.getString("SHARETYPE")));
            }
            if(shareNote.isDefaultValue()) {
                throw new NotExistDataException("This sharenote is not exist!");
            }
            return shareNote;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }

    @Override
    public void add(ShareNote shareNote) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
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
            
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }

    @Override
    public void update(ShareNote shareNote) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
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

            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }

    @Override
    public void delete(int id) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM SHARENOTES WHERE ID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void deleteAll(int noteId) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM SHARENOTES WHERE NOTEID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, noteId);

            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
}