package com.noteapp.dataaccess;

import com.noteapp.model.NetworkProperty;
import com.noteapp.model.datatransfer.Note;
import com.noteapp.model.datatransfer.NoteBlock;
import com.noteapp.model.datatransfer.NoteFilter;
import com.noteapp.dataaccess.connection.DatabaseConnection;
import com.noteapp.dataaccess.connection.MySQLDatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Triển khai các phương thức thao tác dữ liệu với Note
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class NoteDataAccess implements BasicDataAccess<Note, NoteKey, UserKey> {
    private final Connection connection;
    protected DatabaseConnection databaseConnection;
    
    protected BasicDataAccess<NoteBlock, NoteBlockKey, NoteKey> blockDataAccess;
    protected BasicDataAccess<NoteFilter, NoteFilterKey, NoteKey> filterDataAccess;

    /**
     * Khởi tạo và lấy connection tới Database
     */
    private NoteDataAccess() {
        String host = NetworkProperty.DATABASE_HOST;
        int port = NetworkProperty.DATABASE_PORT;
        String dbName = NetworkProperty.DATABASE_NAME;
        String username = NetworkProperty.DATABASE_USERNAME;
        String password = NetworkProperty.DATABASE_PASSWORD;
        databaseConnection = new MySQLDatabaseConnection
            (host, port, dbName, username, password);
        this.connection = databaseConnection.getConnection();
        this.blockDataAccess = NoteBlockDataAccess.getInstance();
        this.filterDataAccess = NoteFilterDataAccess.getInstance();
    }

    private static class SingletonHelper {
        private static final NoteDataAccess INSTANCE = new NoteDataAccess();
    }    
    
    /**
     * Lấy thể hiện duy nhất của lớp này
     * @return Instance duy nhất
     */
    public static NoteDataAccess getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    @Override
    public List<Note> getAll() throws DataAccessException {
        List<Note> notes = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT * FROM notes ORDER BY id, author, LASTMODIFIEDDATE";

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                Note note = new Note();
                //Set dữ liệu từ hàng vào note
                note.setId(resultSet.getInt("ID"));
                note.setAuthor(resultSet.getString("AUTHOR"));
                note.setHeader(resultSet.getString("HEADER"));
                note.setLastModified(resultSet.getInt("LASTMODIFIED"));
                note.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                note.setBlocks(blockDataAccess.getAll(new NoteKey(note.getId())));
                note.setFilters(filterDataAccess.getAll(new NoteKey(note.getId())));
                //Thêm note vào list
                notes.add(note);
            }
            return notes;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }       
    }
    
    @Override
    public List<Note> getAll(UserKey referKey) throws DataAccessException {
        List<Note> notes = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT nt.ID, AUTHOR, HEADER, LASTMODIFIED, LASTMODIFIEDDATE "
                + "FROM notes nt, users us "
                + "WHERE AUTHOR = us.USERNAME AND us.USERNAME = ? "
                + "ORDER BY LASTMODIFIED, LASTMODIFIEDDATE, nt.id, author";

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, referKey.getUsername());
            
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                Note note = new Note();
                //Set dữ liệu từ hàng vào note
                note.setId(resultSet.getInt("ID"));
                note.setAuthor(resultSet.getString("AUTHOR"));
                note.setHeader(resultSet.getString("HEADER"));
                note.setLastModified(resultSet.getInt("LASTMODIFIED"));
                note.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                note.setBlocks(blockDataAccess.getAll(new NoteKey(note.getId())));
                note.setFilters(filterDataAccess.getAll(new NoteKey(note.getId())));
                //Thêm note vào list
                notes.add(note);
            }
            return notes;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }       
    }

    @Override
    public Note get(NoteKey key) throws DataAccessException {
        Note note = new Note();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT nt.ID, AUTHOR, HEADER, LASTMODIFIED, LASTMODIFIEDDATE "
                + "FROM notes nt, users us "
                + "WHERE AUTHOR = us.USERNAME AND nt.ID = ?";

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, key.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //Set dữ liệu từ kết quả vào note
                note.setId(resultSet.getInt("ID"));
                note.setAuthor(resultSet.getString("AUTHOR"));
                note.setHeader(resultSet.getString("HEADER"));
                note.setLastModified(resultSet.getInt("LASTMODIFIED"));
                note.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                note.setBlocks(blockDataAccess.getAll(new NoteKey(note.getId())));
                note.setFilters(filterDataAccess.getAll(new NoteKey(note.getId())));
            }
            if(note.isDefaultValue()) {
                throw new NotExistDataException("This note is not exist!");
            }
            return note;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }

    @Override
    public Note add(Note note) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "INSERT INTO NOTES(ID, AUTHOR, HEADER, LASTMODIFIED, " +
            "LASTMODIFIEDDATE) VALUES(?,?,?,?,?)";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query, 
                    PreparedStatement.RETURN_GENERATED_KEYS);            
            preparedStatement.setInt(1, note.getId());
            preparedStatement.setString(2, note.getAuthor());
            preparedStatement.setString(3, note.getHeader());
            preparedStatement.setInt(4, note.getLastModified());
            preparedStatement.setDate(5, note.getLastModifiedDate());
            //Kiểm tra có add các thông tin vừa rồi được ko, nếu có thì add filter
            for(NoteBlock block: note.getBlocks()) {
                blockDataAccess.add(block, 
                        new NoteBlockKey(note.getId(), block.getOrd(), block.getHeader(), block.getEditor()));
            }
            for(NoteFilter filter: note.getFilters()) {
                filterDataAccess.add(filter, new NoteFilterKey(note.getId(), filter.getFilterContent()));
            }
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            note.setId(resultSet.getInt(1));
            return note;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public Note add(Note note, NoteKey key) throws DataAccessException {
        return this.add(note);
    }

    @Override
    public void update(Note note) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "UPDATE NOTES SET AUTHOR = ?, HEADER = ?, LASTMODIFIED = ?, " +
            "LASTMODIFIEDDATE = ? WHERE ID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);            
            preparedStatement.setString(1, note.getAuthor());
            preparedStatement.setString(2, note.getHeader());
            preparedStatement.setInt(3, note.getLastModified());
            preparedStatement.setDate(4, note.getLastModifiedDate());
            preparedStatement.setInt(5, note.getId());
            //Kiểm tra có add các thông tin vừa rồi được ko, nếu có thì add filter
            blockDataAccess.deleteAll(new NoteKey(note.getId()));
            for(NoteBlock block: note.getBlocks()) {
                blockDataAccess.add(block, 
                        new NoteBlockKey(note.getId(), block.getOrd(), block.getHeader(), block.getEditor()));
            }
            filterDataAccess.deleteAll(new NoteKey(note.getId()));
            for(NoteFilter filter: note.getFilters()) {
                filterDataAccess.add(filter, new NoteFilterKey(note.getId(), filter.getFilterContent()));
            }
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void update(Note note, NoteKey key) throws DataAccessException {
        this.update(note);
    }
    
    @Override
    public void delete(NoteKey key) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM NOTES WHERE ID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, key.getId());
            //Xóa các filter
            filterDataAccess.deleteAll(key);
            
            blockDataAccess.deleteAll(key);
            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void deleteAll(UserKey referKey) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM NOTES WHERE AUTHOR = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, referKey.getUsername());
            //Xóa các filter của các Note tương ứng
            for(Note note: this.getAll(referKey)) {
                filterDataAccess.deleteAll(new NoteKey(note.getId()));
                blockDataAccess.deleteAll(new NoteKey(note.getId()));
            }
            
            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
}