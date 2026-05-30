package com.eventledger.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Metadata {
    private String source;
    private String batchId;
 
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
}
