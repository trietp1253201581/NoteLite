package dataaccess;

import dataaccess.connectdatabase.ConnectDatabase;
import dataaccess.connectdatabase.ConnectMySQLDatabase;
import model.datatransfer.ConvertContent;
import model.datatransfer.ConvertFilter;
import model.Note;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Triển khai các phương thức thao tác dữ liệu với Note
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class NoteDataAccess implements SpecialNoteDataAccess {
    private Connection connection;

    /**
     * Khởi tạo và lấy connection tới Database
     */
    public NoteDataAccess() {
        ConnectDatabase connectSQLDatabase = new ConnectMySQLDatabase();
        this.connection = connectSQLDatabase.getJDBCConnection();
    }

    /**
     * Lấy tất cả các note của một user
     * @param userId id của user cần lấy note
     * @return một list chứa các note của user
     */
    @Override
    public List<Note> getAll(int userId) {
        List<Note> notes = new ArrayList<>();
        //Câu truy vấn SQL
        String query = "SELECT * FROM NOTES WHERE USERID = ?";

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            //Duyệt các hàng kết quả
            while (resultSet.next()) {
                Note note = new Note();
                //Set dữ liệu từ hàng vào note
                note.setId(resultSet.getInt("ID"));
                note.setUserId(resultSet.getInt("USERID"));
                note.setHeader(resultSet.getString("HEADER"));
                note.setContent(ConvertContent.convertToObjectText(resultSet.getString("CONTENT")));
                note.setLastModified(resultSet.getInt("LASTMODIFIED"));
                note.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                note.setFilters(ConvertFilter.convertToList(resultSet.getString("FILTERS")));
                //Thêm note vào list
                notes.add(note);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return notes;
    }

    /**
     * Lấy note của user với header cho trước
     * @param userId id của user sở hữu note
     * @param header header của note
     * @return (1) note lấy được nếu user này có note mang header đã cho, 
     * (2) giá trị default của note nếu user không có note mang header này  
     */
    @Override
    public Note get(int userId, String header) {
        //Câu truy vấn SQL
        String query = "SELECT * FROM NOTES WHERE USERID = ? AND HEADER = ?";

        try {
            //Set các tham số, thực thi truy vấn và lấy kết quả
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, header);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Note note = new Note();
                //Set dữ liệu từ kết quả vào note
                note.setId(resultSet.getInt("ID"));
                note.setUserId(resultSet.getInt("USERID"));
                note.setHeader(resultSet.getString("HEADER"));
                note.setContent(ConvertContent.convertToObjectText(resultSet.getString("CONTENT")));
                note.setLastModified(resultSet.getInt("LASTMODIFIED"));
                note.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                note.setFilters(ConvertFilter.convertToList(resultSet.getString("FILTERS")));

                return note;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //Trả về giá trị default nếu không lấy được
        return new Note();
    }

    /**
     * Lấy note được chỉnh sửa mới nhật của user
     * @param userId id của user
     * @return (1) note được chỉnh sửa mói nhất, (2) default note nếu user chưa có note nào
     */
    @Override
    public Note getLast(int userId) {
        //Câu truy vấn SQL
        String query = "SELECT * FROM NOTES WHERE USERID = ? AND LASTMODIFIED = 1";

        try {
            //Set tham số và thực thi lệnh SQL
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Note note = new Note();
                //Set dữ liệu nhận được vào note
                note.setId(resultSet.getInt("ID"));
                note.setUserId(resultSet.getInt("USERID"));
                note.setHeader(resultSet.getString("HEADER"));
                note.setContent(ConvertContent.convertToObjectText(resultSet.getString("CONTENT")));
                note.setLastModified(resultSet.getInt("LASTMODIFIED"));
                note.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                note.setFilters(ConvertFilter.convertToList(resultSet.getString("FILTERS")));

                return note;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //Trả về default note nếu không tìm được
        return new Note();
    }

    /**
     * Lấy note với id cho trước
     * @param id id của note cần lấy 
     * @return (1) note có id đã cho, (2) default note nếu id này không tồn tại
     */
    @Override
    public Note get(int id) {
        //Câu truy vấn SQL
        String query = "SELECT * FROM NOTES WHERE ID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Note note = new Note();
                //Set dữ liệu cho note
                note.setId(resultSet.getInt("ID"));
                note.setUserId(resultSet.getInt("USERID"));
                note.setHeader(resultSet.getString("HEADER"));
                note.setContent(ConvertContent.convertToObjectText(resultSet.getString("CONTENT")));
                note.setLastModified(resultSet.getInt("LASTMODIFIED"));
                note.setLastModifiedDate(Date.valueOf(resultSet.getString("LASTMODIFIEDDATE")));
                note.setFilters(ConvertFilter.convertToList(resultSet.getString("FILTERS")));

                return note;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //Trả về default note nếu không tìm được
        return new Note();
    }
    
    /**
     * Thêm note vào CSDL
     * @param note note cần thêm vào CSDL
     * @return (1) một số tự nhiên biểu thị row count khi thực thi lệnh SQL này,
     * (2) -1 nếu không thực hiện được
     */
    @Override
    public int add(Note note) {
        //Câu truy vấn SQL
        String query = "INSERT INTO NOTES(USERID, HEADER, CONTENT, LASTMODIFIED, " +
            "LASTMODIFIEDDATE, FILTERS) VALUES(?,?,?,?,?,?)";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);            
            preparedStatement.setInt(1, note.getUserId());
            preparedStatement.setString(2, note.getHeader());
            preparedStatement.setString(3, ConvertContent.convertToDBText(note.getContent()));
            preparedStatement.setInt(4, note.getLastModified());
            preparedStatement.setDate(5, note.getLastModifiedDate());
            preparedStatement.setString(6, ConvertFilter.convertToString(note.getFilters()));

            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    /**
     * Sửa note trong CSDL
     * @param note note cần chỉnh sửa
     * @return (1) một số tự nhiên biểu thị row count khi thực thi lệnh SQL này,
     * (2) -1 nếu không thực hiện được
     */
    @Override
    public int update(Note note) {
        //Câu truy vấn SQL
        String query = "UPDATE NOTES SET USERID = ?, HEADER = ?, CONTENT = ?, LASTMODIFIED = ?, " +
            "LASTMODIFIEDDATE = ?, FILTERS = ? WHERE ID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, note.getUserId());
            preparedStatement.setString(2, note.getHeader());
            preparedStatement.setString(3, ConvertContent.convertToDBText(note.getContent()));
            preparedStatement.setInt(4, note.getLastModified());
            preparedStatement.setDate(5, note.getLastModifiedDate());
            preparedStatement.setString(6, ConvertFilter.convertToString(note.getFilters()));
            preparedStatement.setInt(7, note.getId());

            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    /**
     * Xóa một note ra khỏi CSDL
     * @param id id của note cần xóa
     * @return (1) một số tự nhiên biểu thị row count khi thực thi lệnh SQL này,
     * (2) -1 nếu không thực hiện được
     */
    @Override
    public int delete(int id) {
        //Câu truy vấn SQL
        String query = "DELETE FROM NOTES WHERE ID = ?";

        try {
            //Set tham số và thực thi truy vấn
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }
}