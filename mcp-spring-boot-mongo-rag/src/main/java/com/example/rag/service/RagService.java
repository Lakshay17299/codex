package com.example.rag.service;

import com.example.rag.model.RagDocument;
import com.example.rag.util.EmbeddingUtil;
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
        // Placeholder for a $graphLookup or $search operation
        Document stage = new Document("$match", new Document("text", query));
        return mongoTemplate.getCollection("docs")
                .aggregate(List.of(stage))
                .into(new java.util.ArrayList<>());
    }

    public List<RagDocument> similaritySearch(String query) {
        double[] qVec = EmbeddingUtil.embed(query);
        List<RagDocument> all = mongoTemplate.findAll(RagDocument.class);
        return all.stream()
                .sorted((a, b) -> {
                    double simA = EmbeddingUtil.cosineSimilarity(qVec, a.getEmbedding());
                    double simB = EmbeddingUtil.cosineSimilarity(qVec, b.getEmbedding());
                    return Double.compare(simB, simA);
                })
                .limit(5)
                .toList();
    }

    public RagDocument save(RagDocument doc) {
        doc.setEmbedding(EmbeddingUtil.embed(doc.getText()));
        return mongoTemplate.save(doc);
    }
}
