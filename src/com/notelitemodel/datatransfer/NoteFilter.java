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
public class NoteFilter extends BaseDataTransferModel {
    private String filterContent;

    public NoteFilter() {
        this.filterContent = "";
    }

    public NoteFilter(String filterContent) {
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
        final NoteFilter other = (NoteFilter) obj;
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
    
    public static NoteFilter valueOf(String str) {
        Map<String, String> attributeStrMap = Command.decode(str).get(0);
        return valueOf(attributeStrMap);
    }
    
    public static NoteFilter valueOf(Map<String, String> attributeStrMap) {
        NoteFilter filter = new NoteFilter();
        filter.setFilterContent(attributeStrMap.get("filterContent"));
        return filter;
    }
    
    public static class ListConverter {
        public static String convertToString(List<? extends NoteFilter> models) {
            return BaseDataTransferModel.ListConverter.convertToString(models);
        }
        
        public static List<NoteFilter> convertToList(String listOfFilterStr) {
            List<Map<String, String>> listOfFilterMaps = Command.decode(listOfFilterStr);
            List<NoteFilter> filters = new ArrayList<>();
            for(Map<String, String> filterMap: listOfFilterMaps) {
                NoteFilter newFilter = NoteFilter.valueOf(filterMap);
                filters.add(newFilter);
            }
            return filters;
        }
    }
}
