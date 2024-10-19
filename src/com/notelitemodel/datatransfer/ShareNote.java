package com.notelitemodel.datatransfer;

import com.notelitemodel.Command;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Một transfer cho dữ liệu của các note để chia sẻ giữa các user
 * @author Nhóm 23
 * @since 06/04/2024
 * @version 1.0
 */

public class ShareNote extends Note {
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
        this.receiver = "";
        this.shareType = ShareType.READ_ONLY;
    }

    public ShareNote(String receiver, ShareType shareType) {
        super();
        this.receiver = receiver;
        this.shareType = shareType;
    }

    public ShareNote(String receiver, ShareType shareType, int id, String author, String header, List<NoteBlock> blocks, int lastModified, Date lastModifiedDate, List<NoteFilter> filters) {
        super(id, author, header, blocks, lastModified, lastModifiedDate, filters);
        this.receiver = receiver;
        this.shareType = shareType;
    }

    public String getReceiver() {
        return receiver;
    }
    
    public ShareType getShareType() {
        return shareType;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    
    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.receiver);
        hash = 53 * hash + Objects.hashCode(this.shareType);
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
        super.setBlocks(note.getBlocks());
        super.setLastModified(note.getLastModified());
        super.setLastModifiedDate(note.getLastModifiedDate());
        super.setFilters(note.getFilters());
    }
    
    @Override
    public Map<String, Object> getAttributeMap() {
        Map<String, Object> attributeMap = super.getAttributeMap();
        attributeMap.put("receiver", this.receiver);
        attributeMap.put("shareType", this.shareType);
        return attributeMap;
    }
    
    /**
     * Chuyển một ShareNote thành một String
     * @return String thu được, có dạng {@code "*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;><///><///>"}
     * trong đó * đại diện cho các thuộc tính
     */
    @Override
    public String toString() {
        String objectName = "ShareNote";
        return super.toString(objectName);
    }
    
    /**
     * Chuyển một String sang một ShareNote
     * @param str String cần chuyển có dạng {@code "*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;>///"}
     * trong đó * đại diện cho các thuộc tính
     * @return ShareNote thu được
     */
    public static ShareNote valueOf(String str) {
        Map<String, String> attributeStrMap = Command.decode(str).get(0);
        return valueOf(attributeStrMap);
    }
    
    public static ShareNote valueOf(Map<String, String> attributeStrMap) {
        Note note = Note.valueOf(attributeStrMap);
        ShareNote shareNote = new ShareNote();
        shareNote.setNote(note);
        shareNote.setReceiver(attributeStrMap.get("receiver"));
        shareNote.setShareType(ShareType.valueOf(attributeStrMap.get("shareType")));
        return shareNote;
    }
    
    /**
     * Cung cấp các phương thức chuyển từ string sang list các ShareNote
     */
    public static class ListConverter {
        public static String convertToString(List<? extends ShareNote> models) {
            return BaseDataTransferModel.ListConverter.convertToString(models);
        }
        
        public static List<ShareNote> convertToList(String listOfShareNoteStr) {
            List<Map<String, String>> listOfShareNoteMaps = Command.decode(listOfShareNoteStr);
            List<ShareNote> shareNotes = new ArrayList<>();
            for(Map<String, String> shareNoteMap: listOfShareNoteMaps) {
                ShareNote newShareNote = ShareNote.valueOf(shareNoteMap);
                shareNotes.add(newShareNote);
            }
            return shareNotes;
        }
    }
}