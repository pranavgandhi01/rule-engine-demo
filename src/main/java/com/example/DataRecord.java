package com.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataRecord {
    private LocalDate openDt;
    private Integer totOrgnTermCn;
    private Double totUdrwnAm;
    private String baselPipelineIn;
    private String recourseMtgCd;
    private Double baselisCrConvFctrRt;
    private String nondCnclIn;
    private Integer totTermMoCn;
    private LocalDate maturityDt;
    private String retailSubProduct;
    private String result;
    
    public DataRecord() {}
    
    public DataRecord(String[] csvRow) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.openDt = LocalDate.parse(csvRow[21], formatter);
        this.totOrgnTermCn = Integer.parseInt(csvRow[22]);
        this.totUdrwnAm = Double.parseDouble(csvRow[23]);
        this.baselPipelineIn = csvRow[24];
        this.recourseMtgCd = csvRow[25];
        this.baselisCrConvFctrRt = Double.parseDouble(csvRow[26]);
        this.nondCnclIn = csvRow[27];
        this.totTermMoCn = Integer.parseInt(csvRow[28]);
        this.maturityDt = LocalDate.parse(csvRow[29], formatter);
        this.retailSubProduct = csvRow[34];
    }
    
    // Getters and setters
    public LocalDate getOpenDt() { return openDt; }
    public void setOpenDt(LocalDate openDt) { this.openDt = openDt; }
    
    public Integer getTotOrgnTermCn() { return totOrgnTermCn; }
    public void setTotOrgnTermCn(Integer totOrgnTermCn) { this.totOrgnTermCn = totOrgnTermCn; }
    
    public Double getTotUdrwnAm() { return totUdrwnAm; }
    public void setTotUdrwnAm(Double totUdrwnAm) { this.totUdrwnAm = totUdrwnAm; }
    
    public String getBaselPipelineIn() { return baselPipelineIn; }
    public void setBaselPipelineIn(String baselPipelineIn) { this.baselPipelineIn = baselPipelineIn; }
    
    public String getRecourseMtgCd() { return recourseMtgCd; }
    public void setRecourseMtgCd(String recourseMtgCd) { this.recourseMtgCd = recourseMtgCd; }
    
    public Double getBaselisCrConvFctrRt() { return baselisCrConvFctrRt; }
    public void setBaselisCrConvFctrRt(Double baselisCrConvFctrRt) { this.baselisCrConvFctrRt = baselisCrConvFctrRt; }
    
    public String getNondCnclIn() { return nondCnclIn; }
    public void setNondCnclIn(String nondCnclIn) { this.nondCnclIn = nondCnclIn; }
    
    public Integer getTotTermMoCn() { return totTermMoCn; }
    public void setTotTermMoCn(Integer totTermMoCn) { this.totTermMoCn = totTermMoCn; }
    
    public LocalDate getMaturityDt() { return maturityDt; }
    public void setMaturityDt(LocalDate maturityDt) { this.maturityDt = maturityDt; }
    
    public String getRetailSubProduct() { return retailSubProduct; }
    public void setRetailSubProduct(String retailSubProduct) { this.retailSubProduct = retailSubProduct; }
    
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}