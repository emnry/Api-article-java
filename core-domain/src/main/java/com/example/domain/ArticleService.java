package com.example.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArticleService {

    @Autowired
    private IDAOArticle daoArticle;

    public Article getArticleById(String id) {
        return daoArticle.findById(id);
    }

    public List<Article> getAllArticles() {
        return daoArticle.findAll();
    }

    public boolean deleteArticle(String id) {
        return daoArticle.delete(id);
    }

    public Article saveArticle(Article article) {
        return daoArticle.save(article);
    }
}
