package com.example.rag.repo;

import com.example.rag.model.GraphNode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GraphNodeRepository extends MongoRepository<GraphNode, String> {
    GraphNode findByName(String name);
}
