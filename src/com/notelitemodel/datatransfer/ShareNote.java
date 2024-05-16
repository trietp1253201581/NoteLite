package com.notelitemodel.datatransfer;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Một transfer cho dữ liệu của các note để chia sẻ giữa các user
 * @author Nhóm 23
 * @since 06/04/2024
 * @version 1.0
 */

public class ShareNote extends Note {
    private int shareId;
    private String receiver;
    private ShareType shareType;
    
    /**
     * Định nghĩa các kiểu Share
     */
    public static enum ShareType {
        READ_ONLY, CAN_EDIT;
    }
    
    /**
     * Constructor và cài đặt dữ liệu default cho ShareNote
     */
    public ShareNote() {
        super();
        this.shareId = -1;
        this.receiver = "";
        this.shareType = ShareType.READ_ONLY;
    }
    
    public int getShareId() {
        return shareId;
    }

    public String getReceiver() {
        return receiver;
    }
    
    public ShareType getShareType() {
        return shareType;
    }
    
    public void setShareId(int shareId) {
        this.shareId = shareId;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    
    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.shareId;
        hash = 83 * hash + Objects.hashCode(this.receiver);
        hash = 83 * hash + Objects.hashCode(this.shareType);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final ShareNote other = (ShareNote) obj;
        final Note otherNote = (Note) other;
        if(!super.equals(otherNote)) {
            return false;
        }
        if(this.shareId != other.shareId) {
            return false;
        }
        if(!Objects.equals(this.receiver, other.receiver)) {
            return false;
        }
        return this.shareType == other.shareType;
    }
    
    /**
     * Set giá trị note cho các thuộc tính tương ứng của sharenote
     * @param note note cần set up
     */
    public void setNote(Note note) {
        super.setId(note.getId());
        super.setAuthor(note.getAuthor());
        super.setHeader(note.getHeader());
        super.setContent(note.getContent());
        super.setLastModified(note.getLastModified());
        super.setLastModifiedDate(note.getLastModifiedDate());
        super.setFilters(note.getFilters());
    }
    
    /**
     * Chuyển một ShareNote thành một String
     * @return String thu được, có dạng {@code "*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;><///><///>"}
     * trong đó * đại diện cho các thuộc tính
     */
    @Override
    public String toString() {
        String result = "";
        //Tạo ra một String biểu diễn cho note
        result += super.getId() + SPLIT_ATTRIBUTE_TAGS;
        result += super.getAuthor() + SPLIT_ATTRIBUTE_TAGS;
        result += super.getHeader() + SPLIT_ATTRIBUTE_TAGS;
        result += super.getContent() + SPLIT_ATTRIBUTE_TAGS;
        result += super.getLastModified() + SPLIT_ATTRIBUTE_TAGS;
        result += super.getLastModifiedDate() + SPLIT_ATTRIBUTE_TAGS;
        result += FiltersConverter.convertToString(super.getFilters()) + SPLIT_ATTRIBUTE_TAGS;
        result += shareId + SPLIT_ATTRIBUTE_TAGS;
        result += receiver + SPLIT_ATTRIBUTE_TAGS;
        result += shareType + SPLIT_ATTRIBUTE_TAGS;
        result += END_TAGS + END_TAGS;
        
        return result;
    }
    
    /**
     * Chuyển một String sang một ShareNote
     * @param str String cần chuyển có dạng {@code "*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;>///"}
     * trong đó * đại diện cho các thuộc tính
     * @return ShareNote thu được
     */
    public static ShareNote valueOf(String str) {
        ShareNote shareNote = new ShareNote();
        //Chia chuỗi thành các phần để xử lý
        String[] strarr = str.split(SPLIT_ATTRIBUTE_TAGS);
        //Dựa vào dữ liệu từng phần dể set cho các thuộc tính của note
        shareNote.setId(Integer.parseInt(strarr[0]));
        shareNote.setAuthor(strarr[1]);
        shareNote.setHeader(strarr[2]);
        shareNote.setContent(strarr[3]);
        shareNote.setLastModified(Integer.parseInt(strarr[4]));
        shareNote.setLastModifiedDate(Date.valueOf(strarr[5]));
        shareNote.setFilters(FiltersConverter.convertToList(strarr[6]));
        shareNote.setShareId(Integer.parseInt(strarr[7]));
        shareNote.setReceiver(strarr[8]);
        shareNote.setShareType(ShareType.valueOf(strarr[9]));
        return shareNote;
    }
    
    /**
     * Cung cấp các phương thức chuyển từ string sang list các ShareNote
     */
    public static class ListOfShareNotesConverter {
        private static final String SPLIT_TAGS = "<##>";
        
        /**
         * Chuyển 1 List các ShareNote thành String
         * @param notes list các ShareNote cần chuyển
         * @return String thu được
         */
        public static String convertToString(List<? extends ShareNote> notes) {
            return ListOfNotesConverter.convertToString(notes);
        }
        
        /**
         * Chuyển 1 string thành List các ShareNote
         * @param strShareNotes String miêu tả list
         * @return List thu được
         */
        public static List<ShareNote> convertToList(String strShareNotes) {
            List<ShareNote> result = new ArrayList<>();
            if("".equals(strShareNotes)) {
                return result;
            }
            String[] strShareNoteArrays = strShareNotes.split(SPLIT_TAGS);
            for(String strShareNote: strShareNoteArrays) {
                result.add(valueOf(strShareNote));
            }
            return result;
        }
    }
}