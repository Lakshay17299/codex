package com.example.rag.repo;

import com.example.rag.model.GraphRelation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GraphRelationRepository extends MongoRepository<GraphRelation, String> {
    List<GraphRelation> findByToAndType(String to, String type);
    List<GraphRelation> findByFromAndType(String from, String type);
}
