package com.example.app;

import com.example.domain.Article;
import com.example.domain.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleRestController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Article>>> getAll() {
        List<Article> articles = articleService.getAllArticles();

        ApiResponse<List<Article>> response = new ApiResponse<>(
                2002,
                "La liste des articles a été récupérée avec succès",
                articles
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Article>> getById(@PathVariable String id) {
        Article article = articleService.getArticleById(id);

        if (article == null) {
            return ResponseEntity.ok(new ApiResponse<>(
                    7001,
                    "L'article n'existe pas",
                    null
            ));
        }

        return ResponseEntity.ok(new ApiResponse<>(
                2002,
                "L'article a été récupéré avec succès",
                article
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteArticle(@PathVariable String id) {
        boolean deleted = articleService.deleteArticle(id);

        if (!deleted) {
            return ResponseEntity.ok(new ApiResponse<>(
                    7001,
                    "L'article n'existe pas",
                    false
            ));
        }

        return ResponseEntity.ok(new ApiResponse<>(
                2002,
                "L'article a été supprimé avec succès",
                true
        ));
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Article>> saveArticle(@RequestBody Article article) {
        boolean isUpdate = article.getId() != null;

        Article saved = articleService.saveArticle(article);

        if (saved == null) {
            return ResponseEntity.ok(new ApiResponse<>(
                    7006,
                    "Le titre est déjà utilisé",
                    null
            ));
        }

        if (isUpdate) {
            return ResponseEntity.ok(new ApiResponse<>(
                    2003,
                    "Article modifié avec succès",
                    saved
            ));
        }

        return ResponseEntity.ok(new ApiResponse<>(
                2002,
                "Article créé avec succès",
                saved
        ));
    }
}
