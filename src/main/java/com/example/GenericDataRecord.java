package com.example;

import java.util.Map;
import java.util.HashMap;

public class GenericDataRecord {
    private Map<String, Object> fields;
    private String result;
    
    public GenericDataRecord() {
        this.fields = new HashMap<>();
    }
    
    public GenericDataRecord(Map<String, Object> fields) {
        this.fields = new HashMap<>(fields);
    }
    
    public Object getField(String fieldName) {
        return fields.get(fieldName);
    }
    
    public void setField(String fieldName, Object value) {
        fields.put(fieldName, value);
    }
    
    public Map<String, Object> getFields() {
        return fields;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    @Override
    public String toString() {
        return "GenericDataRecord{fields=" + fields + ", result='" + result + "'}";
    }
}