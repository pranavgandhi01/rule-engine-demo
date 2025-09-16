package com.example;

import com.opencsv.CSVReader;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.FileReader;
import java.util.*;
import java.util.stream.IntStream;

public class GenericRuleEngine {
    
    private final KieContainer kieContainer;
    private final String sessionName;
    
    public GenericRuleEngine(String sessionName) {
        KieServices kieServices = KieServices.Factory.get();
        this.kieContainer = kieServices.getKieClasspathContainer();
        this.sessionName = sessionName;
    }
    
    public List<GenericDataRecord> processRecords(List<GenericDataRecord> records) {
        return records.parallelStream()
            .map(this::applyRules)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    private GenericDataRecord applyRules(GenericDataRecord record) {
        KieSession kieSession = kieContainer.newKieSession(sessionName);
        try {
            kieSession.insert(record);
            kieSession.fireAllRules();
            return record;
        } finally {
            kieSession.dispose();
        }
    }
    
    public static List<GenericDataRecord> loadFromCSV(String filename) throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            String[] headers = reader.readNext();
            List<GenericDataRecord> records = new ArrayList<>();
            
            String[] row;
            while ((row = reader.readNext()) != null) {
                Map<String, Object> fields = new HashMap<>();
                final String[] finalRow = row;
                IntStream.range(0, Math.min(headers.length, finalRow.length))
                    .forEach(i -> fields.put(headers[i], parseValue(finalRow[i])));
                records.add(new GenericDataRecord(fields));
            }
            return records;
        }
    }
    
    private static Object parseValue(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return value.trim();
        }
    }
    
    public static void main(String[] args) throws Exception {
        GenericRuleEngine engine = new GenericRuleEngine("ksession-rules");
        List<GenericDataRecord> records = loadFromCSV("data.csv");
        
        List<GenericDataRecord> results = engine.processRecords(records);
        results.forEach(System.out::println);
    }
}