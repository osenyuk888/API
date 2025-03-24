package api.clients;

import api.pojo.Author;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
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

    //Method to extract and parse the response into a List of Authors
    public List<Author> getAuthorsList() {
        String jsonResponse = getResponse().getBody().asString();

        Type listType = new TypeToken<List<Author>>() {}.getType();

        return new Gson().fromJson(jsonResponse, listType);
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