package api.clients;

import api.pojo.Author;
import io.qameta.allure.Step;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.util.List;

import static io.restassured.RestAssured.given;

public class AuthorsClient extends Client<AuthorsClient> {

    private static final String AUTHORS_ENDPOINT = "/api/v1/Authors";

    @Override
    protected AuthorsClient getType() {
        return this;
    }

    @Step("Create author")
    public AuthorsClient createAuthor(Author author) {
        setResponse(given().contentType(ContentType.JSON).body(author).post(AUTHORS_ENDPOINT));
        return this;
    }

    @Step("Get authors")
    public AuthorsClient getAuthors() {
        setResponse(given().get(AUTHORS_ENDPOINT));
        return this;
    }

    @Step("Delete author")
    public AuthorsClient deleteAuthor(int id) {
        setResponse(given().pathParam("id", id).delete(AUTHORS_ENDPOINT + "/{id}"));
        return this;
    }

    @Step("Get authors books")
    public AuthorsClient getAuthorsBooks(int bookId) {
        setResponse(given().pathParam("id", bookId).get(AUTHORS_ENDPOINT + "/authors/books"
                + "/{id}"));
        return this;
    }

    @Step("Get author")
    public AuthorsClient getAuthor(int id) {
        setResponse(given().pathParam("id", id).get(AUTHORS_ENDPOINT + "/{id}"));
        return this;
    }

    @Step("Update author")
    public AuthorsClient updateAuthor(int id, Author author) {
        author.setId(id);
        setResponse(given().pathParam("id", id).contentType(ContentType.JSON).body(author)
                .put(AUTHORS_ENDPOINT + "/{id}"));
        return this;
    }
}