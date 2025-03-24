package testng.api.tests;

import api.clients.BooksClient;
import api.pojo.Book;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.io.File;

public class BooksControllerTests extends BaseApiTest {
    private static final File BOOK_JSON_SCHEMA = new File("src/test/resources/" +
            "json/schemas/books/book.json");
    private static final File BOOKS_JSON_SCHEMA = new File("src/test/resources/" +
            "json/schemas/books/books.json");

    //positive tests
    @Test(description = "Verify book record can be created")
    public void verifyBookRecordCanBeCreated() {
        Book book = Book.builder().build();
        new BooksClient()
                .createBook(book)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(BOOK_JSON_SCHEMA)
                .verifyJsonBody(book);
    }

    @Test(description = "Verify book record list can be gotten")
    public void verifyBookRecordListCanBeGotten() {
        new BooksClient()
                .getBooks()
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(BOOKS_JSON_SCHEMA);
    }

    @Test(description = "Verify book record can be gotten")
    public void verifyBookRecordCanBeGotten() {
        new BooksClient()
                .getBook(1)
                .verifyStatusCode(HttpStatus.SC_ACCEPTED) //failing this test case on purpose
                .verifyBodyMatchesJsonSchema(BOOK_JSON_SCHEMA)
                .verifyBookId(1);
    }

    @Test(description = "Verify book record can be deleted")
    public void verifyBookRecordCanBeDeleted() {
        new BooksClient()
                .deleteBook(0)
                .verifyStatusCode(HttpStatus.SC_OK);
    }

    @Test(description = "Verify book record can be updated")
    public void verifyBookRecordCanBeUpdated() {
        Book book = Book.builder().build();
        new BooksClient()
                .updateBook(0, book)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(BOOK_JSON_SCHEMA)
                .verifyJsonBody(book);
    }
}
