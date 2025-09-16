package com.example;

import com.opencsv.CSVReader;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class RuleEngine {
    
    public static void main(String[] args) throws Exception {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        
        List<DataRecord> records = loadDataFromCSV("data.csv");
        
        for (DataRecord record : records) {
            KieSession kieSession = kieContainer.newKieSession("ksession-rules");
            kieSession.insert(record);
            kieSession.fireAllRules();
            kieSession.dispose();
            
            System.out.println("Record: " + record.getRetailSubProduct() + 
                             " | Undrawn: " + record.getTotUdrwnAm() + 
                             " | Result: " + record.getResult());
        }
    }
    
    private static List<DataRecord> loadDataFromCSV(String filename) throws Exception {
        List<DataRecord> records = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            String[] nextLine;
            reader.readNext(); // Skip header
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length > 34 && !nextLine[21].isEmpty()) {
                    records.add(new DataRecord(nextLine));
                }
            }
        }
        return records;
    }
}