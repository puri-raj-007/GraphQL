package com.raj.graphql.springbootgraphqlexample.service.datafetcher;

import com.raj.graphql.springbootgraphqlexample.model.Book;
import com.raj.graphql.springbootgraphqlexample.repository.BookRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllBooksDataFetcher implements DataFetcher<List<Book>>{

    @Autowired
    BookRepository bookRepository;

    @Override
    public List<Book> get(DataFetchingEnvironment dataFetcherEnvironment){
        return bookRepository.findAll();
    }

}
