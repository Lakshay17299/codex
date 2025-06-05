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
}
