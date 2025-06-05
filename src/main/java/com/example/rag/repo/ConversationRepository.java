package com.example.rag.repo;

import com.example.rag.model.ConversationEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConversationRepository extends MongoRepository<ConversationEntry, String> {
}
