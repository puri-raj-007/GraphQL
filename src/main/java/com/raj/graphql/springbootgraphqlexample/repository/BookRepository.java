package com.raj.graphql.springbootgraphqlexample.repository;

import com.raj.graphql.springbootgraphqlexample.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {

}
