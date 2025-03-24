package api.clients;

import api.pojo.Book;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class BooksClient extends Client<BooksClient> {
    private static final String END_POINT = "/api/v1/Books";

    @Override
    protected BooksClient getType() {
        return this;
    }

    @Step("Create book")
    public BooksClient createBook(Book book) {
        setResponse(given().contentType(ContentType.JSON).body(book).post(END_POINT));
        return this;
    }

    @Step("Get books")
    public BooksClient getBooks() {
        setResponse(given().get(END_POINT));
        return this;
    }

    @Step("Delete book")
    public BooksClient deleteBook(int id) {
        setResponse(given().pathParam("id", id).delete(END_POINT + "/{id}"));
        return this;
    }

    @Step("Get book")
    public BooksClient getBook(int id) {
        setResponse(given().pathParam("id", id).get(END_POINT + "/{id}"));
        return this;
    }

    @Step("Update book")
    public BooksClient updateBook(int id, Book book) {
        book.setId(id);
        setResponse(given().pathParam("id", id).contentType(ContentType.JSON).body(book).put(END_POINT + "/{id}"));
        return this;
    }

    @Step("Verify book id")
    public void verifyBookId(int expectedId) {
        int actualId = getResponse().getBody().as(Book.class).getId();
        Assert.assertEquals(actualId, expectedId);
    }
}
