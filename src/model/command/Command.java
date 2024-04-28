package model.command;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Định nghĩa một đối tượng Command để mã hóa, giải mã phục vụ truyền dữ liệu trong network
 * @author Lê Minh Triết
 * @since 16/04/2024
 * @version 1.0
 */
public class Command {
    private static final String START_TAGS = "<([a-zA-z]*)>";
    private static final String END_TAGS = "</\\1>";
    private static final String SPLIT_DATA_TAGS = "<;;;>";
    private static final String SPLIT_ATTRIBUTE_VALUE_TAGS = "<:::>";
    
    /**
     * Mã hóa để gửi cho server
     * @param serviceName tên service
     * @param paramMap Một map miêu tả các tham số cho service
     * @return Một String biểu diễn cho việc ra lệnh thực hiện service
     */
    public static String encode(String serviceName, Map<String, Object> paramMap) {
        //Thêm thẻ start
        String result = "<" + serviceName + ">";
        //Chuyển đổi các object tham số thành các chuỗi
        for(Map.Entry<String, Object> entry: paramMap.entrySet()) {
            result += entry.getKey() + SPLIT_ATTRIBUTE_VALUE_TAGS 
                    + entry.getValue() + SPLIT_DATA_TAGS;
        }
        //Thêm thẻ end
        result += "</" + serviceName + ">";
        
        return result;
    }
    
    /**
     * Giải mã để thực thi service
     * @param encodeString String đã được encode
     * @return Một Map chứa serviceName, các tham số của service và các giá trị tương ứng
     */
    public static Map<String, String> decode(String encodeString) {
        //Định nghĩa một Pattern để đọc encode
        Pattern pattern = Pattern.compile(START_TAGS + "(.*)" + END_TAGS);
        //Match encode với pattern
        Matcher matcher = pattern.matcher(encodeString);
        matcher.find();
        //Lấy dữ liệu
        String[] datas = matcher.group(2).split(SPLIT_DATA_TAGS);
        Map<String, String> decodeMap = new HashMap<>();
        decodeMap.put("serviceName", matcher.group(1));
        for(String str: datas) {
            String[] data = str.split(SPLIT_ATTRIBUTE_VALUE_TAGS);
            if(data.length <= 1) {
                decodeMap.put(data[0], "");
            } else {
                decodeMap.put(data[0], data[1]);
            }
        }
        
        return decodeMap;
    }
}