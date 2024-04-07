package model.dataconvert;

/**
 * Chuyển một text hiển thị trên GUI sang một text lưu trong CSDL và ngược lại
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class ConvertContent {
    
    /**
     * Chuyển một text ở GUI sang text lưu trong CSDL
     * @param areaText text ở GUI
     * @return text sau khi chuyển
     */
    public static String convertToDBText(String areaText) {
        //Chia thành các dòng
        String[] texts = areaText.split("\\n");
        String dbText = "";
        //Chuyển ký tự \n ở cuối dòng thành ##endline##
        for (String text: texts) {
            dbText += text + "##endline##";
        }

        return dbText;
    }

    /**
     * Chuyển một text trong CSDL sang một text hiển thị trên GUI
     * @param dbText text trong CSDL
     * @return text sau khi chuyển
     */
    public static String convertToObjectText(String dbText) {
        //Chia thành các phần ngăn cách bởi ##endline##, mỗi phần là một dòng trên text ở GUI
        String[] texts = dbText.split("##endline##");
        String areaText = "";
        //Thêm ký tự \n vào cuối mỗi phần tử
        for (String text: texts) {
            areaText += text + "\n";
        }

        return areaText;
    }
}