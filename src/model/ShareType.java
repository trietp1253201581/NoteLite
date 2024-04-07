package model;

/**
 * Định nghĩa các type cho ShareNote
 * @author Lê Minh Triết
 * @since 06/04/2024
 * @version 1.0
 */
public enum ShareType {
    CAN_EDIT, READ_ONLY;
    
    /**
     * Chuyển một ShareType thành String
     * @param shareType ShareType cần chuyển
     * @return String thu được
     */
    public static String toString(ShareType shareType) {
        if(shareType == ShareType.READ_ONLY) {
            return "ReadOnly";
        } else {
            return "CanEdit";
        }
    }
    
    /**
     * Chuyển một String thành ShareType
     * @param str Share
     * @return 
     */
    public static ShareType toShareType(String str) {
        if(str.equals("ReadOnly")) {
            return ShareType.READ_ONLY;
        } else {
            return ShareType.CAN_EDIT;
        }
    }
}
