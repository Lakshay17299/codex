package com.example.rag.service;

import com.example.rag.model.RagDocument;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Document> graphSearch(String query) {
        // This is a placeholder for a $graphLookup or $search operation
        Document stage = new Document("$match", new Document("text", query));
        return mongoTemplate.getCollection("docs")
                .aggregate(List.of(stage))
                .into(new java.util.ArrayList<>());
    }

    public RagDocument save(RagDocument doc) {
        return mongoTemplate.save(doc);
    }
}
