package com.noteliteserver.dataaccess;

import com.notelitemodel.datatransfer.Note;
import com.noteliteserver.dataaccess.connection.DatabaseConnection;
import com.noteliteserver.dataaccess.connection.MySQLDatabaseConnection;
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
public class NoteDataAccess implements SpecialNoteDataAccess {
    private final Connection connection;
    protected DatabaseConnection databaseConnection;

    /**
     * Khởi tạo và lấy connection tới Database
     */
    private NoteDataAccess() {
        databaseConnection = new MySQLDatabaseConnection
            ("localhost", 3306, "notelitedb", "root", "Asensio1234@");
        this.connection = databaseConnection.getConnection();
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
    public List<Note> getAll(String author) throws DataAccessException {
        List<Note> notes = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT nt.ID, USERNAME as AUTHOR, HEADER, CONTENT, LASTMODIFIED, LASTMODIFIEDDATE "
                + "FROM notes nt, users us "
                + "WHERE nt.USERID = us.ID AND USERNAME = ?";

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, author);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                Note note = new Note();
                //Set dữ liệu từ hàng vào note
                note.setId(resultSet.getInt("ID"));
                note.setAuthor(resultSet.getString("AUTHOR"));
                note.setHeader(resultSet.getString("HEADER"));
                note.setContent(resultSet.getString("CONTENT"));
                note.setLastModified(resultSet.getInt("LASTMODIFIED"));
                note.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                note.setFilters(getFiltersOfNote(note.getId()));
                //Thêm note vào list
                notes.add(note);
            }
            return notes;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }       
    }

    @Override
    public Note get(String author, String header) throws DataAccessException {
        Note note = new Note();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT nt.ID, USERNAME as AUTHOR, HEADER, CONTENT, LASTMODIFIED, LASTMODIFIEDDATE "
                + "FROM notes nt, users us "
                + "WHERE nt.USERID = us.ID AND USERNAME = ? AND HEADER = ?";

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, author);
            preparedStatement.setString(2, header);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //Set dữ liệu từ kết quả vào note
                note.setId(resultSet.getInt("ID"));
                note.setAuthor(resultSet.getString("AUTHOR"));
                note.setHeader(resultSet.getString("HEADER"));
                note.setContent(resultSet.getString("CONTENT"));
                note.setLastModified(resultSet.getInt("LASTMODIFIED"));
                note.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                note.setFilters(getFiltersOfNote(note.getId()));
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
    public Note getLast(String author) throws DataAccessException {
        Note note = new Note();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT nt.ID, USERNAME as AUTHOR, HEADER, CONTENT, LASTMODIFIED, LASTMODIFIEDDATE "
                + "FROM notes nt, users us "
                + "WHERE nt.USERID = us.ID AND USERNAME = ? AND LASTMODIFIED >= 0 "
                + "ORDER BY LASTMODIFIED DESC, LASTMODIFIEDDATE DESC LIMIT 1";

        try {
            //Set tham số và thực thi lệnh SQL
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, author);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //Set dữ liệu nhận được vào note
                note.setId(resultSet.getInt("ID"));
                note.setAuthor(resultSet.getString("AUTHOR"));
                note.setHeader(resultSet.getString("HEADER"));
                note.setContent(resultSet.getString("CONTENT"));
                note.setLastModified(resultSet.getInt("LASTMODIFIED"));
                note.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                note.setFilters(getFiltersOfNote(note.getId()));
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
    public Note get(int id) throws DataAccessException {
        Note note = new Note();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT nt.ID, USERNAME as AUTHOR, HEADER, CONTENT, LASTMODIFIED, LASTMODIFIEDDATE "
                + "FROM notes nt, users us "
                + "WHERE nt.USERID = us.ID AND nt.ID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                //Set dữ liệu cho note
                note.setId(resultSet.getInt("ID"));
                note.setAuthor(resultSet.getString("AUTHOR"));
                note.setHeader(resultSet.getString("HEADER"));
                note.setContent(resultSet.getString("CONTENT"));
                note.setLastModified(resultSet.getInt("LASTMODIFIED"));
                note.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                note.setFilters(getFiltersOfNote(note.getId()));
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
    public void add(Note note) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "INSERT INTO NOTES(USERID, HEADER, CONTENT, LASTMODIFIED, " +
            "LASTMODIFIEDDATE) VALUES(?,?,?,?,?)";

        try {
            //Lấy dữ liệu từ bảng khác
            SpecialUserDataAccess userDataAccess = UserDataAccess.getInstance();
            int userId = userDataAccess.get(note.getAuthor()).getId();
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);            
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, note.getHeader());
            preparedStatement.setString(3, note.getContent());
            preparedStatement.setInt(4, note.getLastModified());
            preparedStatement.setDate(5, note.getLastModifiedDate());
            //Kiểm tra có add các thông tin vừa rồi được ko, nếu có thì add filter
            int addRs = preparedStatement.executeUpdate();
            if(addRs > 0 && (!note.getFilters().isEmpty())) {
                //Lấy id của note vừa tạo
                int newNoteId = get(note.getAuthor(), note.getHeader()).getId();
                for(String filter: note.getFilters()) {
                    try {
                        addFiltersOfNote(newNoteId, filter);
                    } catch (DataAccessException ex) {
                        deleteFiltersOfNote(newNoteId);
                        delete(newNoteId);
                        throw new FailedExecuteException();
                    }
                }             
            }
            if(addRs <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }

    @Override
    public void update(Note note) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "UPDATE NOTES SET USERID = ?, HEADER = ?, CONTENT = ?, LASTMODIFIED = ?, " +
            "LASTMODIFIEDDATE = ? WHERE ID = ?";

        try {
            //Lấy dữ liệu từ bảng khác
            SpecialUserDataAccess userDataAccess = UserDataAccess.getInstance();
            int userId = userDataAccess.get(note.getAuthor()).getId();
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);            
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, note.getHeader());
            preparedStatement.setString(3, note.getContent());
            preparedStatement.setInt(4, note.getLastModified());
            preparedStatement.setDate(5, note.getLastModifiedDate());
            preparedStatement.setInt(6, note.getId());
            //Xóa toàn bộ filter cũ     
            deleteFiltersOfNote(note.getId());
            //Thêm các filter mới
            for(String filter: note.getFilters()) {
                addFiltersOfNote(note.getId(), filter);
            }

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
        String query = "DELETE FROM NOTES WHERE ID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            //Xóa các filter
            deleteFiltersOfNote(id);
            
            if(preparedStatement.executeUpdate() < 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    private List<String> getFiltersOfNote(int id) throws DataAccessException {
        List<String> filters = new ArrayList<>();
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        String query = "SELECT FILTER FROM NOTE_FILTERS WHERE NOTEID = ?";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                filters.add(resultSet.getString("FILTER"));
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
        return filters;
    }
    
    private void addFiltersOfNote(int noteId, String newFilter) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        String query = "INSERT INTO NOTE_FILTERS VALUES(?, ?)";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, noteId);
            preparedStatement.setString(2, newFilter);
            if(newFilter.equals("ilo")) {
                throw new FailedExecuteException();
            }
            
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    private void deleteFiltersOfNote(int noteId) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM NOTE_FILTERS WHERE NOTEID = ?";

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