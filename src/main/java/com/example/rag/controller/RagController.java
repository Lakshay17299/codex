package com.example.rag.controller;

import com.example.rag.model.RagDocument;
import com.example.rag.service.RagService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rag")
public class RagController {

    @Autowired
    private RagService ragService;

    @PostMapping
    public RagDocument addDoc(@RequestBody RagDocument doc) {
        return ragService.save(doc);
    }

    @GetMapping("/search")
    public List<Document> search(@RequestParam String q) {
        return ragService.graphSearch(q);
    }

    @GetMapping("/similarity")
    public List<RagDocument> similarity(@RequestParam String q) {
        return ragService.similaritySearch(q);
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String q) {
        List<RagDocument> docs = ragService.similaritySearch(q);
        if (docs.isEmpty()) {
            return "I couldn't find an answer.";
        }
        return docs.get(0).getText();
    }

    @GetMapping("/chat-graph")
    public String chatGraph(@RequestParam String q) {
        return ragService.graphChat(q);


    }

    @GetMapping("/chat-llm")
    public String chatLlm(@RequestParam String q) {
        return ragService.chatWithFallback(q);
    }
}
