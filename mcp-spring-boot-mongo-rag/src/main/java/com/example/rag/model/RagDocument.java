package com.example.rag.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "docs")
public class RagDocument {
    @Id
    private String id;
    private String text;
    private double[] embedding;
    private java.util.List<String> relatedIds;


    public RagDocument() {
    }

    public RagDocument(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double[] getEmbedding() {
        return embedding;
    }

    public void setEmbedding(double[] embedding) {
        this.embedding = embedding;
    }

    public java.util.List<String> getRelatedIds() {
        return relatedIds;
    }

    public void setRelatedIds(java.util.List<String> relatedIds) {
        this.relatedIds = relatedIds;
    }

}
