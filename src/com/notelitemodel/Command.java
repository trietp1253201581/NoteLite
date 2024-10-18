package com.notelitemodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Định nghĩa một đối tượng Command để mã hóa, giải mã phục vụ truyền dữ liệu trong network
 * @author Nhóm 23
 * @since 16/04/2024
 * @version 1.0
 */
public class Command {
    private static final String START_TAGS = "<([a-zA-z_0-9]*)>";
    private static final String END_TAGS = "</\\1>";
    
    /**
     * Mã hóa để gửi cho server
     * @param objectName tên của object
     * @param attributeMap Một map miêu tả các tham số cho service
     * @return Một String biểu diễn cho việc ra lệnh thực hiện service
     */
    public static String encode(String objectName, Map<String, Object> attributeMap) {
        //Thêm thẻ start
        String result = "<" + objectName + ">";
        //Chuyển đổi các object tham số thành các chuỗi
        for(Map.Entry<String, Object> entry: attributeMap.entrySet()) {
            result += "<" + entry.getKey() + ">";
            result += entry.getValue();
            result += "</" + entry.getKey() + ">";
        }
        //Thêm thẻ end
        result += "</" + objectName + ">";
        return result;
    }
    
    /**
     * Giải mã để thực thi service
     * @param encodeString String đã được encode
     * @return Một Map chứa serviceName, các tham số của service và các giá trị tương ứng
     */
    public static List<Map<String, String>> decode(String encodeString) {
        //Định nghĩa một Pattern để đọc encode
        Pattern pattern = Pattern.compile(START_TAGS + "(.*)" + END_TAGS);
        //Match encode với pattern
        Matcher matcher = pattern.matcher(encodeString);
        List<Map<String, String>> decodeMap = new ArrayList<>();
        while(matcher.find()) {
            Map<String, String> attributeMap = new HashMap<>();
            String attributeStr = matcher.group(2);
            Matcher attrMatcher = pattern.matcher(attributeStr);
            while(attrMatcher.find()) {
                attributeMap.put(attrMatcher.group(1), attrMatcher.group(2));
            }
            attributeMap.put("object", matcher.group(1));
            decodeMap.add(attributeMap);
        }
        return decodeMap;
    }
}