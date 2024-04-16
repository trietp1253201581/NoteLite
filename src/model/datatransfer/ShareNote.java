package model.datatransfer;

import java.sql.Date;
import model.datatransfer.attributeconverter.NoteFilterConverter;

/**
 * Một transfer cho dữ liệu của các note để chia sẻ giữa các user
 * @author Lê Minh Triết
 * @since 06/04/2024
 * @version 1.0
 */

public class ShareNote extends Note {
    private int shareId;
    private String receiver;
    private ShareType shareType;
    
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
    
    /**
     * Set giá trị note cho các thuộc tính tương ứng của sharenote
     * @param note 
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
     * @return String thu được, có dạng {@code "*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;>*<;>///"}
     * trong đó * đại diện cho các thuộc tính
     */
    @Override
    public String toString() {
        String result = "";
        //Tạo ra một String biểu diễn cho note
        result += super.getId() + "<;>";
        result += super.getAuthor() + "<;>";
        result += super.getHeader() + "<;>";
        result += super.getContent() + "<;>";
        result += super.getLastModified() + "<;>";
        result += super.getLastModifiedDate() + "<;>";
        result += NoteFilterConverter.convertToString(super.getFilters()) + "<;>";
        result += shareId + "<;>";
        result += receiver + "<;>";
        result += shareType + "<;>";
        result += "///";
        
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
        String[] strarr = str.split("<;>");
        //Dựa vào dữ liệu từng phần dể set cho các thuộc tính của note
        shareNote.setId(Integer.parseInt(strarr[0]));
        shareNote.setAuthor(strarr[1]);
        shareNote.setHeader(strarr[2]);
        shareNote.setContent(strarr[3]);
        shareNote.setLastModified(Integer.parseInt(strarr[4]));
        shareNote.setLastModifiedDate(Date.valueOf(strarr[5]));
        shareNote.setFilters(NoteFilterConverter.convertToList(strarr[6]));
        shareNote.setShareId(Integer.parseInt(strarr[7]));
        shareNote.setReceiver(strarr[8]);
        shareNote.setShareType(ShareType.valueOf(strarr[9]));
        
        return shareNote;
    }
}
