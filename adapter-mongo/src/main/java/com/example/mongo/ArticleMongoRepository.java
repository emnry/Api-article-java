package com.example.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleMongoRepository extends MongoRepository<ArticleMongo, String> {

    Optional<ArticleMongo> findByTitle(String title);
}
