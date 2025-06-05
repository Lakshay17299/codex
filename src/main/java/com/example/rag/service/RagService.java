package com.example.rag.service;

import com.example.rag.model.RagDocument;
import com.example.rag.model.ConversationEntry;
import com.example.rag.repo.ConversationRepository;
import com.example.rag.repo.GraphNodeRepository;
import com.example.rag.repo.GraphRelationRepository;
import com.example.rag.model.GraphNode;
import com.example.rag.model.GraphRelation;

    @Autowired
    private GraphNodeRepository nodeRepository;

    @Autowired
    private GraphRelationRepository relationRepository;

        Document regex = new Document("$regex", query).append("$options", "i");
        Document match = new Document("$match", new Document("text", regex));
    private String answerFact(String question) {
        java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("Who won(?: in)? (.+)\??", java.util.regex.Pattern.CASE_INSENSITIVE)
                .matcher(question);
        if (m.matches()) {
            String event = m.group(1).trim();
            GraphNode eventNode = nodeRepository.findByName(event);
            if (eventNode != null) {
                List<GraphRelation> rels = relationRepository.findByToAndType(eventNode.getId(), "won");
                if (!rels.isEmpty()) {
                    GraphNode winner = nodeRepository.findById(rels.get(0).getFrom()).orElse(null);
                    if (winner != null) {
                        return winner.getName() + " won " + event;
                    }
                }
            }
        }
        return null;
    }

        String factAns = answerFact(query);
        if (factAns != null) {
            return factAns;
        }

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

    @Autowired
    private ConversationRepository conversationRepository;
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

        // check previous conversation entries for a similar question
        double[] qVec = EmbeddingUtil.embed(query);
        List<ConversationEntry> convs = conversationRepository.findAll();
        ConversationEntry best = null;
        double bestSim = 0.0;
        for (ConversationEntry c : convs) {
            double sim = EmbeddingUtil.cosineSimilarity(qVec, c.getEmbedding());
            if (sim > bestSim) {
                bestSim = sim;
                best = c;
            }
        }
        if (best != null && bestSim > 0.8) {
            return best.getAnswer();
        }

        String answer = openAiService.chat(query);
        ConversationEntry entry = new ConversationEntry();
        entry.setQuestion(query);
        entry.setAnswer(answer);
        entry.setEmbedding(qVec);
        conversationRepository.save(entry);
        return answer;

    }

        RagDocument saved = mongoTemplate.save(doc);

        java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("(.+) won(?: in)? (.+)", java.util.regex.Pattern.CASE_INSENSITIVE)
                .matcher(doc.getText());
        if (m.matches()) {
            String subjectName = m.group(1).trim();
            String objectName = m.group(2).trim();
            GraphNode subj = nodeRepository.findByName(subjectName);
            if (subj == null) {
                subj = nodeRepository.save(new GraphNode(subjectName));
            }
            GraphNode obj = nodeRepository.findByName(objectName);
            if (obj == null) {
                obj = nodeRepository.save(new GraphNode(objectName));
            }
            GraphRelation rel = new GraphRelation();
            rel.setFrom(subj.getId());
            rel.setTo(obj.getId());
            rel.setType("won");
            relationRepository.save(rel);
        }

        return saved;
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
