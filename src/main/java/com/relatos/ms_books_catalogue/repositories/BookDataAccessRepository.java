package com.relatos.ms_books_catalogue.repositories;

import com.relatos.ms_books_catalogue.controllers.response.BookResponse;
import com.relatos.ms_books_catalogue.controllers.response.commons.GeneralResponse;
import com.relatos.ms_books_catalogue.domains.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookDataAccessRepository {

    private final BookRepository bookRepository;
    private final ElasticsearchOperations elasticClient;

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public boolean delete(Book book) {
        bookRepository.delete(book);
        return true;
    }

    public Optional<Book> findById(Integer id) {
        return bookRepository.findById(id);
    }

    public List<BookResponse> findBooks(String title, String isbn, String authorName, String authorLastName, String categoryName,
                                                         LocalDateTime publishedDate, Boolean visibility, Double rating,
                                                         int page, int size) {

        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();

        if (StringUtils.isNotBlank(title)) {
            querySpec.must(QueryBuilders.matchQuery("title", title));
        }

        if (StringUtils.isNotBlank(isbn)) {
            querySpec.must(QueryBuilders.termQuery("ISBN.keyword", isbn.toLowerCase()));
        }

        if (StringUtils.isNotBlank(authorName)) {
            querySpec.must(QueryBuilders.termQuery("author.firstName", authorName));
        }

        if (StringUtils.isNotBlank(authorLastName)) {
            querySpec.must(QueryBuilders.termQuery("author.lastName", authorLastName));
        }

        if (categoryName != null) {
            querySpec.must(QueryBuilders.nestedQuery("category",
                    QueryBuilders.termQuery("category.categoryName", categoryName),
                    org.apache.lucene.search.join.ScoreMode.None));
        }

        if (publishedDate != null) {
            querySpec.must(QueryBuilders.termQuery("publishedDate", publishedDate.toString()));
        }

        if (visibility != null) {
            querySpec.must(QueryBuilders.termQuery("visibility", visibility));
        }

        if (rating != null) {
            querySpec.must(QueryBuilders.termQuery("rating", rating));
        }

        if (!querySpec.hasClauses()) {
            querySpec.must(QueryBuilders.matchAllQuery());
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(querySpec)
                .withPageable(PageRequest.of(page, size));

        Query query = queryBuilder.build();
        SearchHits<Book> result = elasticClient.search(query, Book.class);

        List<Book> books = result.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();

        return books.stream()
                .map(BookResponse::new)
                .toList();
    }

}
