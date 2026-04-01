package com.example.mongo;

import com.example.domain.Article;
import com.example.domain.IDAOArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DAOArticleMongo implements IDAOArticle {

    @Autowired
    private ArticleMongoRepository articleMongoRepository;

    private Article toArticle(ArticleMongo document) {
        return new Article(document.getId(), document.getTitle(), document.getDescription());
    }

    @Override
    public Article findById(String id) {
        return articleMongoRepository.findById(id)
                .map(this::toArticle)
                .orElse(null);
    }

    @Override
    public List<Article> findAll() {
        return articleMongoRepository.findAll()
                .stream()
                .map(this::toArticle)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(String id) {
        if (!articleMongoRepository.existsById(id)) {
            return false;
        }
        articleMongoRepository.deleteById(id);
        return true;
    }

    @Override
    public Article save(Article article) {
        if (article.getId() == null) {
            // Création : vérifier que le titre n'existe pas déjà
            boolean titleTaken = articleMongoRepository.findByTitle(article.getTitle()).isPresent();
            if (titleTaken) {
                return null;
            }

            ArticleMongo document = new ArticleMongo();
            document.setId(UUID.randomUUID().toString());
            document.setTitle(article.getTitle());
            document.setDescription(article.getDescription());

            articleMongoRepository.save(document);
            return toArticle(document);

        } else {
            // Mise à jour : vérifier que le titre n'est pas utilisé par un autre article
            boolean titleTakenByOther = articleMongoRepository.findByTitle(article.getTitle())
                    .filter(existing -> !existing.getId().equals(article.getId()))
                    .isPresent();

            if (titleTakenByOther) {
                return null;
            }

            ArticleMongo document = articleMongoRepository.findById(article.getId()).orElse(null);
            if (document == null) {
                return null;
            }

            document.setTitle(article.getTitle());
            document.setDescription(article.getDescription());

            articleMongoRepository.save(document);
            return toArticle(document);
        }
    }
}
