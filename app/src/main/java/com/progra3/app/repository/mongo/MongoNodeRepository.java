package com.progra3.app.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoNodeRepository extends MongoRepository<NodeDocument, String> {
}	