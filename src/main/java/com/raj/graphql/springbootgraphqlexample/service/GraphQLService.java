package com.raj.graphql.springbootgraphqlexample.service;

import com.raj.graphql.springbootgraphqlexample.model.Book;
import com.raj.graphql.springbootgraphqlexample.repository.BookRepository;
import com.raj.graphql.springbootgraphqlexample.service.datafetcher.AllBooksDataFetcher;
import com.raj.graphql.springbootgraphqlexample.service.datafetcher.BookDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@Service
public class GraphQLService {

    @Value("classpath:books.graphql")
    Resource resource;

    private GraphQL graphQL;
    @Autowired
    private AllBooksDataFetcher allBooksDataFetcher;
    @Autowired
    private BookDataFetcher bookDataFetcher;

    @Autowired
    BookRepository bookRepository;

    @PostConstruct
    private void loadSchema() throws IOException{
        loadDataIntoHSQL();
        //get the schema
        File schemaFile = resource.getFile();
        //parse schema
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buldRuntimeWiring();
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry,wiring);
        graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private void loadDataIntoHSQL() {
        Stream.of(
                new Book("111", "Clouds", "Kindle Edition", new String[]{"writer1", "writer9"}, "Jan 2018"),
                new Book("222", "Java", "Forever Edition", new String[]{"javaWriter", "java1Writer"}, "Dec 000 1"),
                new Book("333", "Spring", "Superb Edition", new String[]{"SpringWriter", "Spring2Writer"}, "Mar 2000"),
                new Book("444", "Angular", "UI Edition", new String[]{"angularWriter", "angular3Writer"}, "Aug 2016")
        ).forEach(book -> {
            bookRepository.save(book);
        });
    }

    private RuntimeWiring buldRuntimeWiring(){
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("allBooks",allBooksDataFetcher)
                        .dataFetcher("book",bookDataFetcher))
                        .build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }



}
