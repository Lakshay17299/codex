package com.example.rag.repo;

import com.example.rag.model.RagDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RagRepository extends MongoRepository<RagDocument, String> {
    // Custom graph queries could be added here
}
