package com.example.domain;

import java.util.List;

public interface IDAOArticle {

    Article findById(String id);

    List<Article> findAll();

    boolean delete(String id);

    Article save(Article article);
}
