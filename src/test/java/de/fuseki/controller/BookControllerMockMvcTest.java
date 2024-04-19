package de.fuseki.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

public class BookControllerMockMvcTest extends AbstractControllerMvc{

    @Test
    @Sql("/3-test-books.sql")
    @Disabled
    public void mvcTestGettingAllBooks(){

    }
}
