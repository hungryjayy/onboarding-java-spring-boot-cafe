package com.kakao.cafe.article.application;

import com.kakao.cafe.article.domain.Article;
import com.kakao.cafe.article.domain.ArticleRepository;
import com.kakao.cafe.article.dto.ArticleListResponse;
import com.kakao.cafe.article.dto.ArticleSaveRequest;
import com.kakao.cafe.article.dto.ArticleShowResponse;
import com.kakao.cafe.article.infra.ArticleRepositoryImpl;
import com.kakao.cafe.common.exception.EntityNotFoundException;
import com.kakao.cafe.user.domain.User;
import com.kakao.cafe.user.domain.UserRepository;
import com.kakao.cafe.user.infra.UserRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository = new ArticleRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();

    public void save(ArticleSaveRequest request) {
        log.info(this.getClass() + ": 게시글 작성");
        String authorName = request.writer;
        User author = userRepository.findByUserNameOrNull(authorName);
        if(author == null) {
            EntityNotFoundException.throwNotExistsByField(User.class, "userName", authorName);
        }
        Article article = request.toArticle(author);
        articleRepository.save(article);
    }

    public List<ArticleListResponse> findAll() {
        log.info(this.getClass() + ": 게시글 목록");
        List<Article> articles = articleRepository.findAll();
        return articles.stream()
                .map(ArticleListResponse::valueOf)
                .collect(Collectors.toList());
    }

    public ArticleShowResponse findById(String articleId) {
        log.info(this.getClass() + ": 게시글 상세보기");
        Article article = articleRepository.findByIdOrNull(articleId);
        if(article == null) {
            EntityNotFoundException.throwNotExistsByField(Article.class, "id", articleId);
        }
        return ArticleShowResponse.valueOf(article);
    }
}