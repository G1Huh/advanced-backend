package com.lion.demo.service;

import com.lion.demo.entity.Book;
import com.lion.demo.entity.BookEs;
import com.lion.demo.repository.BookEsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookEsService {
    public static final int PAGE_SIZE = 10;
    @Autowired
    private BookEsRepository bookEsRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public BookEs findById(String bookId) {
        return bookEsRepository.findById(bookId).orElse(null);
    }

    public Page<BookEs> getPagedBooks(int page, String field, String query) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
        if (field.equals("title"))
            return bookEsRepository.findByTitleContaining("query", pageable);
        if (field.equals("author"))
            return bookEsRepository.findByAuthorContaining("query", pageable);
        if (field.equals("company"))
            return bookEsRepository.findByCompanyContaining("query", pageable);
        return bookEsRepository.findBySummaryContaining("query", pageable);
    }



    public void insertBookEs(BookEs bookEs) {
        bookEsRepository.save(bookEs);
    }

    public Page<BookEs> getBooksByKeyword(String keyword){
        Query query = NativeQuery.builder()
                .withQuery(buildMatchQuery("summary", keyword))
                .build();
        SearchHits<BookEs> searchhits = elasticsearchTemplate.search(query, BookEs.class);
        List<BookEs> bookEsList = searchhits.getSearchHits().stream().map(SearchHit::getContent).toList();

        // Total hits count
        long totalHits = searchhits.getTotalHits();
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        return new PageImpl<>(bookEsList, pageable, totalHits);
    }

    private Query buildMatchQuery(String field, String keyword){
        String queryString = String.format("{\"match\": {\"%s\" : \"%s\" }}", field, keyword);

        return new StringQuery(queryString);
    }

}
