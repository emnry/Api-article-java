package com.example.jpa;

import com.example.domain.Article;
import com.example.domain.IDAOArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DAOArticleJpa implements IDAOArticle {

    @Autowired
    private ArticleJpaRepository articleJpaRepository;

    private Article toArticle(ArticleJpa entity) {
        return new Article(entity.getId(), entity.getTitle(), entity.getDescription());
    }

    @Override
    public Article findById(String id) {
        return articleJpaRepository.findById(id)
                .map(this::toArticle)
                .orElse(null);
    }

    @Override
    public List<Article> findAll() {
        return articleJpaRepository.findAll()
                .stream()
                .map(this::toArticle)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(String id) {
        if (!articleJpaRepository.existsById(id)) {
            return false;
        }
        articleJpaRepository.deleteById(id);
        return true;
    }

    @Override
    public Article save(Article article) {
        if (article.getId() == null) {
            // Création : vérifier que le titre n'existe pas déjà
            boolean titleTaken = articleJpaRepository.findByTitle(article.getTitle()).isPresent();
            if (titleTaken) {
                return null;
            }

            ArticleJpa entity = new ArticleJpa();
            entity.setId(UUID.randomUUID().toString());
            entity.setTitle(article.getTitle());
            entity.setDescription(article.getDescription());

            articleJpaRepository.save(entity);
            return toArticle(entity);

        } else {
            // Mise à jour : vérifier que le titre n'est pas utilisé par un autre article
            boolean titleTakenByOther = articleJpaRepository.findByTitle(article.getTitle())
                    .filter(existing -> !existing.getId().equals(article.getId()))
                    .isPresent();

            if (titleTakenByOther) {
                return null;
            }

            ArticleJpa entity = articleJpaRepository.findById(article.getId()).orElse(null);
            if (entity == null) {
                return null;
            }

            entity.setTitle(article.getTitle());
            entity.setDescription(article.getDescription());

            articleJpaRepository.save(entity);
            return toArticle(entity);
        }
    }
}
