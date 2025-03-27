package testng.api.tests;

import api.clients.BooksClient;
import api.data.providers.TestDataProvider;
import api.pojo.Book;
import api.support.BookUtils;
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
        BooksClient booksClient = new BooksClient();
        Book book = Book.builder().build();

        booksClient.createBook(book)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(BOOK_JSON_SCHEMA)
                .verifyJsonBody(book);
    }

    @Test(description = "Verify book record list can be gotten")
    public void verifyBookRecordListCanBeGotten() {
        BooksClient booksClient = new BooksClient();
        booksClient.getBooks()
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(BOOKS_JSON_SCHEMA);
    }

    @Test(description = "Verify book record can be gotten")
    public void verifyBookRecordCanBeGotten() {
        //prepare
        BooksClient booksClient = new BooksClient();
        booksClient.getBooks();
        int bookId = BookUtils.getLastBookIdFromList(BookUtils.getBooksList(booksClient.getResponse()));

        //test
        booksClient.getBook(bookId)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(BOOK_JSON_SCHEMA)
                .verifyBookId(bookId);
    }

    @Test(description = "Verify book record can be deleted")
    public void verifyBookRecordCanBeDeleted() {
        //prepare
        BooksClient booksClient = new BooksClient();
        booksClient.getBooks();
        int bookId = BookUtils.getLastBookIdFromList(BookUtils.getBooksList(booksClient.getResponse()));

        booksClient.deleteBook(bookId)
                .verifyStatusCode(HttpStatus.SC_OK);

        booksClient.getBook(bookId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify book record cannot be deleted if already deleted")
    public void verifyBookRecordCannotBeDeletedIfAlreadyDeleted() {
        BooksClient booksClient = new BooksClient();
        booksClient.getBooks();
        int bookId = BookUtils.getLastBookIdFromList(BookUtils.getBooksList(booksClient.getResponse()));

        booksClient.deleteBook(bookId)
                .verifyStatusCode(HttpStatus.SC_OK);

        booksClient.deleteBook(bookId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify book record can be updated")
    public void verifyBookRecordCanBeUpdated() {
        //prepare
        BooksClient booksClient = new BooksClient();
        booksClient.getBooks();
        int bookId = BookUtils.getLastBookIdFromList(BookUtils.getBooksList(booksClient.getResponse()));
        Book book = Book.builder().build();

        booksClient.updateBook(bookId, book)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(BOOK_JSON_SCHEMA)
                .verifyJsonBody(book);
    }

    //negative tests
    @Test(description = "Verify that a book record cannot be retrieved using ID 0")
    public void verifyBookRecordCannotBeGottenForIdZero() {
        BooksClient booksClient = new BooksClient();
        int bookId = 0;

        booksClient.getBook(bookId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify that a book record cannot be retrieved using a negative ID (-1)")
    public void verifyBookRecordCannotBeGottenForNegativeId() {
        BooksClient booksClient = new BooksClient();
        final int bookId = -1;

        booksClient.getBook(bookId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify that a book record cannot be retrieved using the maximum integer value as an ID")
    public void verifyBookRecordCannotBeGottenForMaxIntId() {
        BooksClient booksClient = new BooksClient();
        booksClient.getBook(Integer.MAX_VALUE)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify book record cannot be created with all null fields")
    public void verifyBookRecordCannotBeCreatedWithAllNullFields() {
        //prepare
        BooksClient booksClient = new BooksClient();
        Book book = Book.builder().id(null)
                .title(null)
                .description(null)
                .pageCount(null)
                .excerpt(null)
                .publishDate(null)
                .build();

        booksClient.createBook(book)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify book record cannot be created with a negative ID")
    public void verifyBookRecordCannotBeCreatedWithNegativeID() {
        //prepare
        BooksClient booksClient = new BooksClient();
        Book book = Book.builder().id(-1).build(); //other fields are correct

        //test
        booksClient.createBook(book)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify book record cannot be created with zero ID")
    public void verifyBookRecordCannotBeCreatedWithZeroID() {
        //prepare
        BooksClient booksClient = new BooksClient();
        Book book = Book.builder().id(0).build(); //other fields are correct

        //test
        booksClient.createBook(book)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify book record cannot be created with a negative page count")
    public void verifyBookRecordCannotBeCreatedWithNegativePageCount() {
        //prepare
        BooksClient booksClient = new BooksClient();
        Book book = Book.builder().pageCount(-1).build(); //other fields are correct

        //test
        booksClient.createBook(book)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify book record cannot be created with zero page count")
    public void verifyBookRecordCannotBeCreatedWithZeroPageCount() {
        //prepare
        BooksClient booksClient = new BooksClient();
        Book book = Book.builder().pageCount(0).build(); //other fields are correct

        //test
        booksClient.createBook(book)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    //Test method that uses DataProvider to test invalid publish dates
    @Test(description = "Verify book record cannot be created with an invalid publish date",
            dataProvider = "invalidPublishDates", dataProviderClass = TestDataProvider.class)
    public void verifyBookRecordCannotBeCreatedWithInvalidPublishDate(String invalidPublishDate) {
        //prepare
        BooksClient booksClient = new BooksClient();
        Book book = Book.builder().publishDate(invalidPublishDate).build();

        //test
        booksClient.createBook(book)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify book record cannot be updated with a negative ID")
    public void verifyBookRecordCannotBeUpdatedWithNegativeID() {
        //prepare
        BooksClient booksClient = new BooksClient();
        Book updatedBook = Book.builder().id(-1).title("Negative ID").build();

        //test
        booksClient.updateBook(-1, updatedBook)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify book record cannot be updated with zero ID")
    public void verifyBookRecordCannotBeUpdatedWithZeroID() {
        //prepare
        BooksClient booksClient = new BooksClient();
        Book updatedBook = Book.builder().id(0).title("Zero id").build();

        //test
        booksClient.updateBook(0, updatedBook)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify book record cannot be updated with a negative page count")
    public void verifyBookRecordCannotBeUpdatedWithNegativePageCount() {
        //prepare
        BooksClient booksClient = new BooksClient();
        booksClient.getBooks();
        int bookId = BookUtils.getLastBookIdFromList(BookUtils.getBooksList(booksClient.getResponse()));
        Book updatedBook = Book.builder().id(bookId).pageCount(-100).build();

        //test
        booksClient.updateBook(bookId, updatedBook)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify book record cannot be updated with zero page count")
    public void verifyBookRecordCannotBeUpdatedWithZeroPageCount() {
        //prepare
        BooksClient booksClient = new BooksClient();
        booksClient.getBooks();
        int bookId = BookUtils.getLastBookIdFromList(BookUtils.getBooksList(booksClient.getResponse()));
        Book updatedBook = Book.builder().id(bookId).pageCount(0).build();

        //test
        booksClient.updateBook(bookId, updatedBook)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify book record cannot be updated with an invalid publish date",
            dataProvider = "invalidPublishDates", dataProviderClass = TestDataProvider.class)
    public void verifyBookRecordCannotBeUpdatedWithInvalidPublishDate(String invalidPublishDate) {
        //prepare
        BooksClient booksClient = new BooksClient();
        booksClient.getBooks();
        int bookId = BookUtils.getLastBookIdFromList(BookUtils.getBooksList(booksClient.getResponse()));

        Book updatedBook = Book.builder()
                .id(bookId)
                .publishDate(invalidPublishDate)
                .build();

        booksClient.updateBook(bookId, updatedBook)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify book record cannot be deleted with non-existing ID")
    public void verifyBookRecordCannotBeDeletedWithNonExistingID() {
        //prepare
        BooksClient booksClient = new BooksClient();
        int bookId = Integer.MAX_VALUE; // ID that does not exist take it using API or DB

        //test
        booksClient.deleteBook(bookId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify book record cannot be deleted with negative ID")
    public void verifyBookRecordCannotBeDeletedWithNegativeID() {
        BooksClient booksClient = new BooksClient();
        int bookId = -1;

        booksClient.deleteBook(bookId)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify book record cannot be deleted with zero ID")
    public void verifyBookRecordCannotBeDeletedWithZeroID() {
        BooksClient booksClient = new BooksClient();
        int bookId = 0;

        booksClient.deleteBook(bookId)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
