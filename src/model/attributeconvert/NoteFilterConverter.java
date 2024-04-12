package model.attributeconvert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cung cấp các phương thức để convert một list các filter thành string và ngược lại
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class NoteFilterConverter {

    /**
     * Chuyển list các filter thành String
     * @param filters list các filter
     * @return String có gồm các filter string, ngăn cách bởi {@code ##}
     */
    public static String convertToString(List<String> filters) {
        String result = "";
        //Với mỗi filter string, thêm vào result filter string đó và dấu ##
        for (String filter: filters) {
            result += filter + "##";
        }

        return result;
    }

    /**
     * Chuyển một String thành list các filter
     * @param strFilter String có dạng gồm nhiều {@code .##}, mỗi thành phần là 1 filter string
     * @return list filter thu được
     */
    public static List<String> convertToList(String strFilter) {
        //Chuyển String thành các thành phần filter string (do được ngăn cách bởi ##)
        String[] filters = strFilter.split("##");
        List<String> result = new ArrayList<>();
        //Chuyển từ String[] thành list
        result.addAll(Arrays.asList(filters));

        return result;
    }
}