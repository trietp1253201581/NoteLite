package com.noteapp.dataaccess;

import com.noteapp.model.NetworkProperty;
import com.noteapp.model.datatransfer.Note;
import com.noteapp.model.datatransfer.ShareNote;
import com.noteapp.dataaccess.connection.DatabaseConnection;
import com.noteapp.dataaccess.connection.MySQLDatabaseConnection;
import com.noteapp.model.datatransfer.NoteBlock;
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
public class ShareNoteDataAccess implements BasicDataAccess<ShareNote, ShareNoteKey, ShareNoteKey> {
    private final Connection connection;
    protected DatabaseConnection databaseConnection;
    
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;
    protected BasicDataAccess<NoteBlock, NoteBlockKey, NoteKey> blockDataAccess;

    private ShareNoteDataAccess() {
        String host = NetworkProperty.DATABASE_HOST;
        int port = NetworkProperty.DATABASE_PORT;
        String dbName = NetworkProperty.DATABASE_NAME;
        String username = NetworkProperty.DATABASE_USERNAME;
        String password = NetworkProperty.DATABASE_PASSWORD;
        databaseConnection = new MySQLDatabaseConnection
            (host, port, dbName, username, password);
        this.connection = databaseConnection.getConnection();
        noteDataAccess = NoteDataAccess.getInstance();
        blockDataAccess = NoteBlockDataAccess.getInstance();
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
    public List<ShareNote> getAll() throws DataAccessException {
        List<ShareNote> shareNotes = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT NOTEID, RECEIVER, SHARETYPE "
                + "FROM sharenotes sh, users us, notes nt "
                + "WHERE sh.RECEIVER = us.USERNAME AND NOTEID = nt.Id "
                + "ORDER BY noteid, receiver, sharetype";

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                ShareNote shareNote = new ShareNote();
                int noteId = resultSet.getInt("NOTEID");
                Note note = noteDataAccess.get(new NoteKey(noteId));
                shareNote.setNote(note);
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
                shareNote.setShareType(ShareNote.ShareType.valueOf(resultSet.getString("SHARETYPE")));
                //Thêm note vào list
                shareNotes.add(shareNote);
            }
            return shareNotes;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }       
    }
    
    @Override
    public List<ShareNote> getAll(ShareNoteKey referKey) throws DataAccessException {
        List<ShareNote> shareNotes = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT NOTEID, RECEIVER, SHARETYPE "
                + "FROM sharenotes sh, users us, notes nt "
                + "WHERE sh.RECEIVER = us.USERNAME AND NOTEID = nt.Id "
                + "AND RECEIVER = ? ORDER BY NOTEID, RECEIVER, SHARETYPE";

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, referKey.getReceiver());
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                ShareNote shareNote = new ShareNote();
                int noteId = resultSet.getInt("NOTEID");
                Note note = noteDataAccess.get(new NoteKey(noteId));
                shareNote.setNote(note);
                shareNote.setReceiver(resultSet.getString("RECEIVER"));
                shareNote.setShareType(ShareNote.ShareType.valueOf(resultSet.getString("SHARETYPE")));
                //Thêm note vào list
                shareNotes.add(shareNote);
            }
            return shareNotes;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }       
    }

    @Override
    public ShareNote get(ShareNoteKey key) throws DataAccessException {
        ShareNote shareNote = new ShareNote();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT NOTEID, RECEIVER, SHARETYPE "
                + "FROM sharenotes sh, users us, notes nt "
                + "WHERE sh.RECEIVER = us.USERNAME AND NOTEID = nt.Id "
                + "AND NOTEID = ? AND RECEIVER = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, key.getNoteId());
            preparedStatement.setString(2, key.getReceiver());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {                
                int noteId = resultSet.getInt("NOTEID");
                Note note = noteDataAccess.get(new NoteKey(noteId));
                shareNote.setNote(note);
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
    public ShareNote add(ShareNote shareNote) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "INSERT INTO SHARENOTES(NOTEID, RECEIVER, SHARETYPE)"
                + "VALUES(?, ?, ?)";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, shareNote.getId());
            preparedStatement.setString(2, shareNote.getReceiver());
            preparedStatement.setString(3, shareNote.getShareType().toString());
            if(shareNote.getShareType() == ShareNote.ShareType.CAN_EDIT) {
                for(NoteBlock block: shareNote.getBlocks()) {
                    block.setEditor(shareNote.getReceiver());
                    blockDataAccess.add(block, 
                            new NoteBlockKey(shareNote.getId(), block.getOrd(), block.getHeader(), block.getEditor()));
                }
            }
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            return shareNote;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public ShareNote add(ShareNote shareNote, ShareNoteKey key) throws DataAccessException {
        return this.add(shareNote);
    }

    @Override
    public void update(ShareNote shareNote) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "UPDATE SHARENOTES SET SHARETYPE = ? "
                + "WHERE NOTEID = ? AND RECEIVER = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, shareNote.getShareType().toString());
            preparedStatement.setInt(2, shareNote.getId());
            preparedStatement.setString(3, shareNote.getReceiver());

            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void update(ShareNote shareNote, ShareNoteKey key) throws DataAccessException {
        this.update(shareNote);
    }

    @Override
    public void delete(ShareNoteKey key) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM SHARENOTES WHERE NOTEID = ? AND RECEIVER = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, key.getNoteId());
            preparedStatement.setString(2, key.getReceiver());

            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void deleteAll(ShareNoteKey referKey) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM SHARENOTES WHERE NOTEID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, referKey.getNoteId());

            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
}