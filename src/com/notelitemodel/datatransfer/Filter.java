package com.notelitemodel.datatransfer;

import com.notelitemodel.Command;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author admin
 */
public class Filter extends BaseDataTransferModel {
    private String filterContent;

    public Filter() {
        this.filterContent = "";
    }

    public Filter(String filterContent) {
        this.filterContent = filterContent;
    }

    public String getFilterContent() {
        return filterContent;
    }

    public void setFilterContent(String filterContent) {
        this.filterContent = filterContent;
    }
    
    @Override
    public boolean isDefaultValue() {
        return "".equals(this.filterContent);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.filterContent);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Filter other = (Filter) obj;
        return Objects.equals(this.filterContent, other.filterContent);
    }
    
    @Override
    public Map<String, Object> getAttributeMap() {
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("filterContent", this.filterContent);
        return attributeMap;
    }

    @Override
    public String toString() {
        String objectName = "Filter";
        return super.toString(objectName);
    }
    
    public static Filter valueOf(String str) {
        Map<String, String> attributeStrMap = Command.decode(str).get(0);
        return valueOf(attributeStrMap);
    }
    
    public static Filter valueOf(Map<String, String> attributeStrMap) {
        Filter filter = new Filter();
        filter.setFilterContent(attributeStrMap.get("filterContent"));
        return filter;
    }
    
    public static class ListConverter {
        public static String convertToString(List<? extends Filter> models) {
            return BaseDataTransferModel.ListConverter.convertToString(models);
        }
        
        public static List<Filter> convertToList(String listOfFilterStr) {
            List<Map<String, String>> listOfFilterMaps = Command.decode(listOfFilterStr);
            List<Filter> filters = new ArrayList<>();
            for(Map<String, String> filterMap: listOfFilterMaps) {
                Filter newFilter = Filter.valueOf(filterMap);
                filters.add(newFilter);
            }
            return filters;
        }
    }
}
