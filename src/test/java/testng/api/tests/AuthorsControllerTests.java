package testng.api.tests;

import api.clients.AuthorsClient;
import api.pojo.Author;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

public class AuthorsControllerTests extends BaseApiTest {
    private static final File AUTHORS_JSON_SCHEMA = new File("src/test/resources/" +
            "json/schemas/authors/authors.json");
    private static final File AUTHOR_JSON_SCHEMA = new File("src/test/resources/" +
            "json/schemas/authors/author.json");

    @Test(description = "Verify author record can be created")
    public void verifyAuthorRecordCanBeCreated() {
        Author author = Author.builder().build();
        new AuthorsClient()
                .createAuthor(author)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(AUTHOR_JSON_SCHEMA)
                .verifyJsonBody(author);
    }

    @Test(description = "Verify authors record list can be gotten")
    public void verifyAuthorsRecordListCanBeGotten() {
        new AuthorsClient()
                .getAuthors()
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(AUTHORS_JSON_SCHEMA);
    }

    @Test(description = "Verify author record can be gotten")
    public void verifyAuthorRecordCanBeGotten() {
        new AuthorsClient()
                .getAuthor(1)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(AUTHOR_JSON_SCHEMA);
    }

    @Test(description = "Verify author record can be deleted")
    public void verifyAuthorRecordCanBeDeleted() {
        new AuthorsClient()
                .deleteAuthor(1)
                .verifyStatusCode(HttpStatus.SC_OK);
    }

    @Test(description = "Verify author record can be updated")
    public void verifyAuthorRecordCanBeUpdated() {
        Author author = Author.builder().build();

        new AuthorsClient()
                .updateAuthor(0, author)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(AUTHOR_JSON_SCHEMA)
                .verifyJsonBody(author);
    }

    //negative test cases
    @Test(description = "Verify author record can be updated")
    public void verifyAuthorRecordUpdateForInvalidId() {
        Author author = Author.builder().build();
        new AuthorsClient()
                .updateAuthor(2147483647, author) //max int value
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(AUTHOR_JSON_SCHEMA)
                .verifyJsonBody(author);
    }

    @Test(description = "Verify authors books record can be gotten")
    public void verifyAuthorsBooksRecordCanBeGotten() {
        int bookId = 1;

        List<Author> authors = new AuthorsClient()
                .getAuthorsBooks(bookId)
                .verifyStatusCode(200)
                .verifyBodyMatchesJsonSchema(AUTHORS_JSON_SCHEMA)
                .getAuthorsList();

        Assert.assertFalse(authors.isEmpty(), "Authors list is empty");

        //extract method for common cases in future
        boolean allAuthorsHaveCorrectBookId = authors.stream()
                .allMatch(author -> author.getIdBook().equals(bookId));

        // Assert that all authors have the correct bookId
        Assert.assertTrue(allAuthorsHaveCorrectBookId, "Not all authors have the correct bookId");

    }
}
