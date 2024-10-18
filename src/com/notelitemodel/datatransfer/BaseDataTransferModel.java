package com.notelitemodel.datatransfer;

import com.notelitemodel.Command;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public abstract class BaseDataTransferModel {
    public abstract Map<String, Object> getAttributeMap();
    
    public abstract boolean isDefaultValue();
    
    public String toString(String objectName) {
        Map<String, Object> attributeMap = this.getAttributeMap();
        return Command.encode(objectName, attributeMap);
    }
    
    public static class ListConverter {
        public static String convertToString(List<? extends BaseDataTransferModel> models) {
            String result = "";
            for(int i=0; i<models.size(); i++) {
                String objectName = models.get(i).getClass().getSimpleName();
                result += models.get(i).toString(objectName + "_" + i);
            }
            return result;
        }
    }
}
