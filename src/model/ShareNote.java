package model;

/**
 * Một transfer cho dữ liệu của các lượt chia sẻ note giữa các user
 * @author Lê Minh Triết
 * @since 06/04/2024
 * @version 1.0
 */

public class ShareNote {
    private int id;
    private String sender;
    private String receiver;
    private String header;
    private ShareType shareType;
    
    /**
     * Constructor và cài đặt dữ liệu default cho ShareNote
     */
    public ShareNote() {
        this.id = -1;
        this.sender = "";
        this.receiver = "";
        this.header = "";
        this.shareType = ShareType.READ_ONLY;
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getHeader() {
        return header;
    }
    
    public ShareType getShareType() {
        return shareType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setHeader(String header) {
        this.header = header;
    }
    
    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }
    
    /**
     * Kiểm tra xem một thể hiện ShareNote có mang giá trị default không
     * @return (1) {@code true} nếu đây là default ShareNote, (2) {@code false} nếu ngược lại
     */
    public boolean isDefaultValue() {
        return id == -1;
    }
    
    /**
     * Chuyển một ShareNote thành một String
     * @param shareNote ShareNote cần chuyển
     * @return String thu được, có dạng {@code "*;;;*;;;*;;;*;;;*;;;///"}
     * trong đó * đại diện cho các thuộc tính
     */
    public static String toString(ShareNote shareNote) {
        String result = "";
        //Tạo ra một String biểu diễn cho note
        result += shareNote.getId() + ";;;";
        result += shareNote.getSender() + ";;;";
        result += shareNote.getReceiver() + ";;;";
        result += shareNote.getHeader() + ";;;";
        result += ShareType.toString(shareNote.getShareType()) + ";;;";
        result += "///";
        
        return result;
    }
    
    /**
     * Chuyển một String sang một ShareNote
     * @param str String cần chuyển có dạng {@code "*;;;*;;;*;;;*;;;*;;;///"}
     * trong đó * đại diện cho các thuộc tính
     * @return ShareNote thu được
     */
    public static ShareNote valueOf(String str) {
        ShareNote shareNote = new ShareNote();
        //Chia chuỗi thành các phần để xử lý
        String[] strarr = str.split(";;;");
        //Dựa vào dữ liệu từng phần dể set cho các thuộc tính của note
        shareNote.setId(Integer.parseInt(strarr[0]));
        shareNote.setSender(strarr[1]);
        shareNote.setReceiver(strarr[2]);
        shareNote.setHeader(strarr[3]);
        shareNote.setShareType(ShareType.toShareType(strarr[4]));
        
        return shareNote;
    }
}
