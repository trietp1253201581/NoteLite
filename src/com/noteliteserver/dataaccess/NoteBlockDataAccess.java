package com.noteliteserver.dataaccess;

import com.notelitemodel.NetworkProperty;
import com.notelitemodel.datatransfer.NoteBlock;
import com.noteliteserver.dataaccess.connection.DatabaseConnection;
import com.noteliteserver.dataaccess.connection.MySQLDatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */

public class NoteBlockDataAccess implements BasicDataAccess<NoteBlock, NoteBlockKey, NoteKey>{
    private final Connection connection;
    protected DatabaseConnection databaseConnection;

    /**
     * Khởi tạo và lấy connection tới Database
     */
    private NoteBlockDataAccess() {
        String host = NetworkProperty.DATABASE_HOST;
        int port = NetworkProperty.DATABASE_PORT;
        String dbName = NetworkProperty.DATABASE_NAME;
        String username = NetworkProperty.DATABASE_USERNAME;
        String password = NetworkProperty.DATABASE_PASSWORD;
        databaseConnection = new MySQLDatabaseConnection
            (host, port, dbName, username, password);
        this.connection = databaseConnection.getConnection();
    }

    private static class SingletonHelper {
        private static final NoteBlockDataAccess INSTANCE = new NoteBlockDataAccess();
    }    
    
    /**
     * Lấy thể hiện duy nhất của lớp này
     * @return Instance duy nhất
     */
    public static NoteBlockDataAccess getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    @Override 
    public List<NoteBlock> getAll() throws DataAccessException {
        List<NoteBlock> noteBlocks = new ArrayList<>();
        //Kiểm tra connection có phải null không
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT * FROM note_blocks";

        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Chuyển từng hàng dữ liệu sang noteBlock và thêm vào list
            while (resultSet.next()) {
                NoteBlock noteBlock = new NoteBlock();
                //Set dữ liệu cho noteBlock
                noteBlock.setId(resultSet.getInt("BLOCKID"));
                noteBlock.setContent(resultSet.getString("BLOCKCONTENT"));
                noteBlock.setBlockType(NoteBlock.BlockType.valueOf(resultSet.getString("BLOCKTYPE")));
                noteBlocks.add(noteBlock);
            }    
            //Nếu noteBlocks rỗng thì ném ngoại lệ là danh sách trống
            return noteBlocks;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override 
    public List<NoteBlock> getAll(NoteKey referKey) throws DataAccessException {
        List<NoteBlock> noteBlocks = new ArrayList<>();
        //Kiểm tra connection có phải null không
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT blockid, blockcontent, blocktype FROM "
                + "note_blocks nb, notes nt "
                + "WHERE nb.noteid = nt.id AND noteid = ?";

        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, referKey.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            //Chuyển từng hàng dữ liệu sang noteBlock và thêm vào list
            while (resultSet.next()) {
                NoteBlock noteBlock = new NoteBlock();
                //Set dữ liệu cho noteBlock
                noteBlock.setId(resultSet.getInt("BLOCKID"));
                noteBlock.setContent(resultSet.getString("BLOCKCONTENT"));
                noteBlock.setBlockType(NoteBlock.BlockType.valueOf(resultSet.getString("BLOCKTYPE")));
                noteBlocks.add(noteBlock);
            }    
            //Nếu noteBlocks rỗng thì ném ngoại lệ là danh sách trống
            return noteBlocks;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public NoteBlock get(NoteBlockKey key) throws DataAccessException {
        NoteBlock noteBlock = new NoteBlock();
        //Kiểm tra connection có phải null không
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "SELECT blockid, blockcontent, blocktype FROM "
                + "note_blocks nb, notes nt "
                + "WHERE nb.noteid = nt.id AND noteid = ? AND blockid = ?";

        try {
            //Thực thi truy vấn SQL và lấy kết quả là một bộ dữ liệu
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, key.getNoteId());
            preparedStatement.setInt(2, key.getBlockId());
            ResultSet resultSet = preparedStatement.executeQuery();
            //Chuyển từng hàng dữ liệu sang noteBlock và thêm vào list
            while (resultSet.next()) {
                //Set dữ liệu cho noteBlock
                noteBlock.setId(resultSet.getInt("BLOCKID"));
                noteBlock.setContent(resultSet.getString("BLOCKCONTENT"));
                noteBlock.setBlockType(NoteBlock.BlockType.valueOf(resultSet.getString("BLOCKTYPE")));
            }    
            //Nếu không tồn tại thì ném ngoại lệ
            if(noteBlock.isDefaultValue()) {
                throw new NotExistDataException("Block is not exist!");
            }   
            return noteBlock;
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void add(NoteBlock noteBlock) throws DataAccessException {
        throw new FailedExecuteException();
    }
    
    @Override
    public void add(NoteBlock noteBlock, NoteBlockKey key) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "INSERT INTO note_blocks(noteid, blockid, blockcontent, blocktype) "
                + "VALUES(?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setInt(1, key.getNoteId());
            preparedStatement.setInt(2, key.getBlockId());
            preparedStatement.setString(3, noteBlock.getContent());
            preparedStatement.setString(4, noteBlock.getBlockType().toString());
            
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    @Override
    public void update(NoteBlock noteBlock) throws DataAccessException {
        throw new FailedExecuteException();
    }
    
    @Override
    public void update(NoteBlock noteBlock, NoteBlockKey key) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "UPDATE note_blocks SET blockcontent=?, blocktype = ?"
                + "WHERE noteid = ? AND blockid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn 
            preparedStatement.setString(1, noteBlock.getContent());
            preparedStatement.setString(2, noteBlock.getBlockType().toString());
            preparedStatement.setInt(3, key.getNoteId());
            preparedStatement.setInt(4, key.getBlockId());
            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void delete(NoteBlockKey key) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM note_blocks WHERE noteid = ? AND blockid = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setInt(1, key.getNoteId());
            preparedStatement.setInt(2, key.getBlockId());

            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
    
    @Override
    public void deleteAll(NoteKey referKey) throws DataAccessException {
        //Kiểm tra null
        if(connection == null) {
            throw new FailedExecuteException();
        }
        //Câu truy vấn SQL
        String query = "DELETE FROM note_blocks WHERE noteid = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            //Set các tham số cho truy vấn
            preparedStatement.setInt(1, referKey.getId());

            if(preparedStatement.executeUpdate() <= 0) {
                throw new FailedExecuteException();
            }
        } catch (SQLException ex) {
            throw new FailedExecuteException();
        }
    }
}
