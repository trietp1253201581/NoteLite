package model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.attributeconvert.NoteFilterConverter;

/**
 * Một transfer cho dữ liệu của các note
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class Note {
    private int id;
    private String author;
    private String header;
    private String content;
    private int lastModified;
    private Date lastModifiedDate;
    private List<String> filters;

    /**
     * Constructor và cài đặt dữ liệu default cho Note
     */
    public Note() {
        this.id = -1;
        this.author = "";
        this.header = "";
        this.content = "";
        this.lastModified = 0;
        this.lastModifiedDate = Date.valueOf(LocalDate.MIN);
        this.filters = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }

    public int getLastModified() {
        return lastModified;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLastModified(int lastModified) {
        this.lastModified = lastModified;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }
    
    /**
     * Kiểm tra xem một thể hiện Note có mang giá trị default không
     * @return (1) {@code true} nếu đây là default Note, (2) {@code false} nếu ngược lại
     */
    public boolean isDefaultValue() {
        return id == -1;
    }

    /**
     * Chuyển một Note thành String
     * @param note note cần chuyển
     * @return String thu được, có dạng {@code "*;;;*;;;*;;;*;;;*;;;*;;;*;;;*;;;///"}
     * trong đó * đại diện cho các thuộc tính
     */
    public static String toString(Note note) {
        String result = "";
        //Tạo ra một String biểu diễn cho note
        result += note.getId() + ";;;";
        result += note.getAuthor() + ";;;";
        result += note.getHeader() + ";;;";
        result += note.getContent() + ";;;";
        result += note.getLastModified() + ";;;";
        result += note.getLastModifiedDate() + ";;;";
        result += NoteFilterConverter.convertToString(note.getFilters()) + ";;;";
        result += "///";
        
        return result;    
    }
    
    /**
     * Chuyển một String sang một Note
     * @param str String cần chuyển có dạng {@code "*;;;*;;;*;;;*;;;*;;;*;;;*;;;*;;;///"}
     * trong đó * đại diện cho các thuộc tính
     * @return Note thu được
     */
    public static Note valueOf(String str) {       
        Note note = new Note();
        //Chia chuỗi thành các phần để xử lý
        String[] strarr = str.split(";;;");
        //Dựa vào dữ liệu từng phần dể set cho các thuộc tính của note
        note.setId(Integer.parseInt(strarr[0]));
        note.setAuthor(strarr[1]);
        note.setHeader(strarr[2]);
        note.setContent(strarr[3]);
        note.setLastModified(Integer.parseInt(strarr[4]));
        note.setLastModifiedDate(Date.valueOf(strarr[5]));
        note.setFilters(NoteFilterConverter.convertToList(strarr[6]));
        
        return note;      
    }
    
}
