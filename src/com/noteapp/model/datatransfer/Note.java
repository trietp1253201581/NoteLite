package com.noteapp.model.datatransfer;

import com.noteapp.model.Command;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Một transfer cho dữ liệu của các note
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class Note extends BaseDataTransferModel {
    private int id;
    private String author;
    private String header;
    private List<NoteBlock> blocks;
    private int lastModified;
    private Date lastModifiedDate;
    private List<NoteFilter> filters;
    
    /**
     * Constructor và cài đặt dữ liệu default cho Note
     */
    public Note() {
        this.id = -1;
        this.author = "";
        this.header = "";
        this.blocks = new ArrayList<>();
        this.lastModified = 0;
        this.lastModifiedDate = Date.valueOf(LocalDate.MIN);
        this.filters = new ArrayList<>();
    }

    public Note(int id, String author, String header, List<NoteBlock> blocks, int lastModified, Date lastModifiedDate, List<NoteFilter> filters) {
        this.id = id;
        this.author = author;
        this.header = header;
        this.blocks = blocks;
        this.lastModified = lastModified;
        this.lastModifiedDate = lastModifiedDate;
        this.filters = filters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<NoteBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<NoteBlock> blocks) {
        this.blocks = blocks;
    }

    public int getLastModified() {
        return lastModified;
    }

    public void setLastModified(int lastModified) {
        this.lastModified = lastModified;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<NoteFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<NoteFilter> filters) {
        this.filters = filters;
    }
    
    /**
     * Kiểm tra xem một thể hiện Note có mang giá trị default không
     * @return (1) {@code true} nếu đây là default Note, (2) {@code false} nếu ngược lại
     */
    @Override
    public boolean isDefaultValue() {
        return id == -1;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.id;
        hash = 73 * hash + Objects.hashCode(this.author);
        hash = 73 * hash + Objects.hashCode(this.header);
        hash = 73 * hash + Objects.hashCode(this.blocks);
        hash = 73 * hash + this.lastModified;
        hash = 73 * hash + Objects.hashCode(this.lastModifiedDate);
        hash = 73 * hash + Objects.hashCode(this.filters);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Note other = (Note) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.lastModified != other.lastModified) {
            return false;
        }
        if (!Objects.equals(this.author, other.author)) {
            return false;
        }
        if (!Objects.equals(this.header, other.header)) {
            return false;
        }
        if (!Objects.equals(this.blocks, other.blocks)) {
            return false;
        }
        if (!Objects.equals(this.lastModifiedDate, other.lastModifiedDate)) {
            return false;
        }
        return Objects.equals(this.filters, other.filters);
    }
    
    @Override
    public Map<String, Object> getAttributeMap() {
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("id", this.id);
        attributeMap.put("author", this.author);
        attributeMap.put("header", this.header);
        attributeMap.put("lastModified", this.lastModified);
        attributeMap.put("lastModifiedDate", this.lastModifiedDate);
        attributeMap.put("blocks", NoteBlock.ListConverter.convertToString(blocks));
        attributeMap.put("filters", NoteFilter.ListConverter.convertToString(filters));
        return attributeMap;
    }

    /**
     * Chuyển một Note thành String
     * @return String thu được, có dạng {@code "*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;><///><///>"}
     * trong đó * đại diện cho các thuộc tính
     */
    @Override
    public String toString() {
        String objectName = "Note";
        return super.toString(objectName);
    }
    
    /**
     * Chuyển một String sang một Note
     * @param str String cần chuyển có dạng {@code "*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;>///"}
     * trong đó * đại diện cho các thuộc tính
     * @return Note thu được
     */
    public static Note valueOf(String str) {       
        Map<String, String> attributeStrMap = Command.decode(str).get(0);
        return valueOf(attributeStrMap);
    }
    
    public static Note valueOf(Map<String, String> attributeStrMap) {
        Note note = new Note();
        note.setId(Integer.parseInt(attributeStrMap.get("id")));
        note.setAuthor(attributeStrMap.get("author"));
        note.setHeader(attributeStrMap.get("header"));
        note.setLastModified(Integer.parseInt(attributeStrMap.get("lastModified")));
        note.setLastModifiedDate(Date.valueOf(attributeStrMap.get("lastModifiedDate")));
        note.setBlocks(NoteBlock.ListConverter.convertToList(attributeStrMap.get("blocks")));
        note.setFilters(NoteFilter.ListConverter.convertToList(attributeStrMap.get("filters")));
        return note;
    }
    
    /**
     * Class cung cấp các phương thức để chuyển 1 list các note thành string và ngược lại
     */
    public static class ListConverter {
        public static String convertToString(List<? extends Note> models) {
            return BaseDataTransferModel.ListConverter.convertToString(models);
        }
        
        public static List<Note> convertToList(String listOfNoteStr) {
            List<Map<String, String>> listOfNoteMaps = Command.decode(listOfNoteStr);
            List<Note> notes = new ArrayList<>();
            for(Map<String, String> noteMap: listOfNoteMaps) {
                Note newNote = Note.valueOf(noteMap);
                notes.add(newNote);
            }
            return notes;
        }
    }
}
