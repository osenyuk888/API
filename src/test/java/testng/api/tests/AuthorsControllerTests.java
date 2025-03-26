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

    //positive tests
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
        AuthorsClient authorsClient = new AuthorsClient();
        List<Author> allAuthors = authorsClient.getAuthors().getAuthorsList();
        int authorId = allAuthors.get(allAuthors.size() - 1).getId();

        authorsClient.getAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(AUTHOR_JSON_SCHEMA);
    }

    @Test(description = "Verify author record can be deleted")
    public void verifyAuthorRecordCanBeDeleted() {
        AuthorsClient authorsClient = new AuthorsClient();
        List<Author> allAuthors = authorsClient.getAuthors().getAuthorsList();
        int authorId = allAuthors.get(allAuthors.size() - 1).getId();

        authorsClient.deleteAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_OK);

        authorsClient.getAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify that an author record cannot be deleted if already deleted")
    public void verifyAuthorRecordCannotBeDeletedIfAlreadyDeleted() {
        AuthorsClient authorsClient = new AuthorsClient();
        List<Author> allAuthors = authorsClient.getAuthors().getAuthorsList();
        int authorId = allAuthors.get(allAuthors.size() - 1).getId();

        authorsClient.deleteAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_OK);

        authorsClient.deleteAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify author record can be updated")
    public void verifyAuthorRecordCanBeUpdated() {
        AuthorsClient authorsClient = new AuthorsClient();
        List<Author> allAuthors = authorsClient.getAuthors().getAuthorsList();
        int authorId = allAuthors.get(allAuthors.size() - 1).getId();
        Author author = Author.builder().build();

        new AuthorsClient()
                .updateAuthor(authorId, author)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(AUTHOR_JSON_SCHEMA)
                .verifyJsonBody(author);
    }

    @Test(description = "Verify authors books record can be gotten")
    public void verifyAuthorsBooksRecordCanBeGotten() {
        int bookId = 1;

        List<Author> authors = new AuthorsClient()
                .getAuthorsBooks(bookId)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(AUTHORS_JSON_SCHEMA)
                .getAuthorsList();

        //extract method for common cases in future
        boolean allAuthorsHaveCorrectBookId = authors.stream()
                .allMatch(author -> author.getIdBook().equals(bookId));

        // Assert that all authors have the correct bookId
        Assert.assertTrue(allAuthorsHaveCorrectBookId, "Not all authors have the correct bookId");

    }

    //negative test cases
    @Test(description = "Verify that an author record cannot be retrieved using ID 0")
    public void verifyAuthorRecordCannotBeGottenForIdZero() {
        int authorId = 0;
        new AuthorsClient()
                .getAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify that an author record cannot be retrieved using a negative ID (-1)")
    public void verifyAuthorRecordCannotBeGottenForNegativeId() {
        int authorId = -1;
        new AuthorsClient()
                .getAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify that an author record cannot be retrieved using the maximum integer value as an ID")
    public void verifyAuthorRecordCannotBeGottenForMaxIntId() {
        new AuthorsClient()
                .getAuthor(Integer.MAX_VALUE)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify that an author record cannot be created with all null fields")
    public void verifyAuthorRecordCannotBeCreatedWithAllNullFields() {
        Author author = Author.builder().id(null)
                .idBook(null)
                .firstName(null)
                .lastName(null)
                .build();

        new AuthorsClient()
                .createAuthor(author)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify that an author record cannot be created with a negative ID")
    public void verifyAuthorRecordCannotBeCreatedWithNegativeId() {
        Author author = Author.builder().id(-1).build(); //other fields are correct

        new AuthorsClient()
                .createAuthor(author)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify that an author record cannot be created with created with zero ID")
    public void verifyAuthorRecordCannotBeCreatedWithZeroId() {
        Author author = Author.builder().id(0).build(); //other fields are correct

        new AuthorsClient()
                .createAuthor(author)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify that an author record cannot be created with negative book ID")
    public void verifyAuthorRecordCannotBeCreatedWithNegativeBookId() {
        Author author = Author.builder().idBook(-1).build(); //other fields are correct

        new AuthorsClient()
                .createAuthor(author)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify that an author record cannot be created with zero book ID")
    public void verifyAuthorRecordCannotBeCreatedWithZeroBookId() {
        Author author = Author.builder().idBook(0).build(); //other fields are correct

        new AuthorsClient()
                .createAuthor(author)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify that an author record cannot be updated with a negative ID")
    public void verifyAuthorRecordCannotBeUpdatedWithNegativeId() {
        AuthorsClient authorsClient = new AuthorsClient();
        Author updatedAuthor = Author.builder().id(-1).firstName("Negative ID").build();

        //test
        authorsClient.updateAuthor(-1, updatedAuthor)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify that an author record cannot be updated with a zero ID")
    public void verifyAuthorRecordCannotBeUpdatedWithZeroId() {
        AuthorsClient authorsClient = new AuthorsClient();
        Author updatedAuthor = Author.builder().id(0).firstName("Zero ID").build();

        //test
        authorsClient.updateAuthor(0, updatedAuthor)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify that an author record cannot be updated with a negative book ID")
    public void verifyAuthorRecordCannotBeUpdatedWithNegativeBookId() {
        AuthorsClient authorsClient = new AuthorsClient();
        List<Author> allAuthors = authorsClient.getAuthors().getAuthorsList();
        int authorId = allAuthors.get(allAuthors.size() - 1).getId();
        Author updatedAuthor = Author.builder().idBook(-100).firstName("Negative book ID").build();

        //test
        authorsClient.updateAuthor(authorId, updatedAuthor)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify that an author record cannot be updated with a zero book ID")
    public void verifyAuthorRecordCannotBeUpdatedWithZeroBookId() {
        AuthorsClient authorsClient = new AuthorsClient();
        List<Author> allAuthors = authorsClient.getAuthors().getAuthorsList();
        int authorId = allAuthors.get(allAuthors.size() - 1).getId();
        Author updatedAuthor = Author.builder().idBook(0).firstName("Zero book ID").build();

        //test
        authorsClient.updateAuthor(authorId, updatedAuthor)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify that an author record cannot be deleted with non-existing ID")
    public void verifyAuthorRecordCannotBeDeletedWithNonExistingId() {
        //prepare
        AuthorsClient authorsClient = new AuthorsClient();
        int authorId = Integer.MAX_VALUE; // ID that does not exist take it using API or DB

        //test
        authorsClient.deleteAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify that an author record cannot be deleted with negative ID")
    public void verifyAuthorRecordCannotBeDeletedWithNegativeId() {
        //prepare
        AuthorsClient authorsClient = new AuthorsClient();
        int authorId = -1;

        //test
        authorsClient.deleteAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify that an author record cannot be deleted with zero ID")
    public void verifyAuthorRecordCannotBeDeletedWithZeroId() {
        //prepare
        AuthorsClient authorsClient = new AuthorsClient();
        int authorId = 0;

        //test
        authorsClient.deleteAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify authors books record cannot be gotten for negative book ID")
    public void verifyAuthorsBooksRecordCanNotBeGottenWithNegativeBookId() {
        int bookId = -1;

        new AuthorsClient()
                .getAuthorsBooks(bookId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify authors books record cannot be gotten for zero book ID")
    public void verifyAuthorsBooksRecordCanNotBeGottenWithZeroBookId() {
        int bookId = 0;

        new AuthorsClient()
                .getAuthorsBooks(bookId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify authors books record cannot be gotten for not existing book ID")
    public void verifyAuthorsBooksRecordCanNotBeGottenWithNotExistingBookId() {
        int bookId = Integer.MAX_VALUE;

        new AuthorsClient()
                .getAuthorsBooks(bookId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }
}
