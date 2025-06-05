package com.example.rag.service;

import com.example.rag.model.RagDocument;
import com.example.rag.util.EmbeddingUtil;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.example.rag.service.OpenAiService;

import java.util.List;

@Service
public class RagService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OpenAiService openAiService;

    public List<Document> graphSearch(String query) {
        Document match = new Document("$match", new Document("text", query));
        Document lookup = new Document("$graphLookup",
                new Document("from", "docs")
                        .append("startWith", "$relatedIds")
                        .append("connectFromField", "relatedIds")
                        .append("connectToField", "_id")
                        .append("as", "neighbors")
                        .append("maxDepth", 2));

        return mongoTemplate.getCollection("docs")
                .aggregate(List.of(match, lookup))
                .into(new java.util.ArrayList<>());
    }

    public String graphChat(String query) {
        List<Document> result = graphSearch(query);
        if (result.isEmpty()) {
            return "I couldn't find an answer.";
        }
        Document doc = result.get(0);
        String answer = doc.getString("text");
        @SuppressWarnings("unchecked")
        List<Document> neighbors = (List<Document>) doc.get("neighbors");
        if (neighbors != null && !neighbors.isEmpty()) {
            String next = neighbors.get(0).getString("text");
            if (next != null) {
                answer += " " + next;
            }
        }
        return answer;
    }

    public String chatWithFallback(String query) {
        List<Document> result = graphSearch(query);
        if (!result.isEmpty()) {
            Document doc = result.get(0);
            String answer = doc.getString("text");
            return answer == null ? "" : answer;
        }
        return openAiService.chat(query);
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
        if (doc.getRelatedIds() == null) {
            doc.setRelatedIds(java.util.Collections.emptyList());
        }

        return mongoTemplate.save(doc);
    }
}
