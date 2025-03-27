package testng.api.tests;

import api.clients.AuthorsClient;
import api.support.AuthorMatcher;
import api.pojo.Author;
import api.support.AuthorUtils;
import org.apache.http.HttpStatus;
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
        AuthorsClient authorsClient = new AuthorsClient();
        Author author = Author.builder().build();

        authorsClient.createAuthor(author)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(AUTHOR_JSON_SCHEMA)
                .verifyJsonBody(author);
    }

    @Test(description = "Verify authors record list can be gotten")
    public void verifyAuthorsRecordListCanBeGotten() {
        AuthorsClient authorsClient = new AuthorsClient();
        authorsClient.getAuthors()
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(AUTHORS_JSON_SCHEMA);
    }

    @Test(description = "Verify author record can be gotten")
    public void verifyAuthorRecordCanBeGotten() {
        AuthorsClient authorsClient = new AuthorsClient();
        authorsClient.getAuthors();
        int authorId = AuthorUtils.getLastAuthorIdFromList(AuthorUtils.
                getAuthorsList(authorsClient.getResponse()));

        authorsClient.getAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(AUTHOR_JSON_SCHEMA);
    }

    @Test(description = "Verify author record can be deleted")
    public void verifyAuthorRecordCanBeDeleted() {
        AuthorsClient authorsClient = new AuthorsClient();
        authorsClient.getAuthors();
        int authorId = AuthorUtils.getLastAuthorIdFromList(AuthorUtils.
                getAuthorsList(authorsClient.getResponse()));

        authorsClient.deleteAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_OK);

        authorsClient.getAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify that an author record cannot be deleted if already deleted")
    public void verifyAuthorRecordCannotBeDeletedIfAlreadyDeleted() {
        AuthorsClient authorsClient = new AuthorsClient();
        authorsClient.getAuthors();
        int authorId = AuthorUtils.getLastAuthorIdFromList(AuthorUtils.
                getAuthorsList(authorsClient.getResponse()));

        authorsClient.deleteAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_OK);

        authorsClient.deleteAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify author record can be updated")
    public void verifyAuthorRecordCanBeUpdated() {
        AuthorsClient authorsClient = new AuthorsClient();
        authorsClient.getAuthors();
        int authorId = AuthorUtils.getLastAuthorIdFromList(AuthorUtils.
                getAuthorsList(authorsClient.getResponse()));
        Author author = Author.builder().build();

        authorsClient.updateAuthor(authorId, author)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(AUTHOR_JSON_SCHEMA)
                .verifyJsonBody(author);
    }

    @Test(description = "Verify authors books record can be gotten")
    public void verifyAuthorsBooksRecordCanBeGotten() {
        AuthorsClient authorsClient = new AuthorsClient();
        int bookId = 1;

         authorsClient.getAuthorsBooks(bookId)
                .verifyStatusCode(HttpStatus.SC_OK)
                .verifyBodyMatchesJsonSchema(AUTHORS_JSON_SCHEMA);

        List<Author> authors = AuthorUtils.getAuthorsList(authorsClient.getResponse());

        AuthorMatcher.hasBookId(authors, bookId);
    }

    //negative test cases
    @Test(description = "Verify that an author record cannot be retrieved using ID 0")
    public void verifyAuthorRecordCannotBeGottenForIdZero() {
        AuthorsClient authorsClient = new AuthorsClient();
        int authorId = 0;

        authorsClient.getAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify that an author record cannot be retrieved using a negative ID (-1)")
    public void verifyAuthorRecordCannotBeGottenForNegativeId() {
        AuthorsClient authorsClient = new AuthorsClient();
        int authorId = -1;

        authorsClient.getAuthor(authorId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify that an author record cannot be retrieved using the maximum integer value as an ID")
    public void verifyAuthorRecordCannotBeGottenForMaxIntId() {
        AuthorsClient authorsClient = new AuthorsClient();
        authorsClient.getAuthor(Integer.MAX_VALUE)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify that an author record cannot be created with all null fields")
    public void verifyAuthorRecordCannotBeCreatedWithAllNullFields() {
        AuthorsClient authorsClient = new AuthorsClient();
        Author author = Author.builder().id(null)
                .idBook(null)
                .firstName(null)
                .lastName(null)
                .build();

        authorsClient.createAuthor(author)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify that an author record cannot be created with a negative ID")
    public void verifyAuthorRecordCannotBeCreatedWithNegativeId() {
        AuthorsClient authorsClient = new AuthorsClient();
        Author author = Author.builder().id(-1).build(); //other fields are correct

        authorsClient.createAuthor(author)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify that an author record cannot be created with created with zero ID")
    public void verifyAuthorRecordCannotBeCreatedWithZeroId() {
        AuthorsClient authorsClient = new AuthorsClient();
        Author author = Author.builder().id(0).build(); //other fields are correct

        authorsClient.createAuthor(author)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify that an author record cannot be created with negative book ID")
    public void verifyAuthorRecordCannotBeCreatedWithNegativeBookId() {
        AuthorsClient authorsClient = new AuthorsClient();
        Author author = Author.builder().idBook(-1).build(); //other fields are correct

        authorsClient.createAuthor(author)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify that an author record cannot be created with zero book ID")
    public void verifyAuthorRecordCannotBeCreatedWithZeroBookId() {
        AuthorsClient authorsClient = new AuthorsClient();
        Author author = Author.builder().idBook(0).build(); //other fields are correct

        authorsClient.createAuthor(author)
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
        authorsClient.getAuthors();
        int authorId = AuthorUtils.getLastAuthorIdFromList(AuthorUtils.
                getAuthorsList(authorsClient.getResponse()));
        Author updatedAuthor = Author.builder().idBook(-100).firstName("Negative book ID").build();

        //test
        authorsClient.updateAuthor(authorId, updatedAuthor)
                .verifyStatusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Verify that an author record cannot be updated with a zero book ID")
    public void verifyAuthorRecordCannotBeUpdatedWithZeroBookId() {
        AuthorsClient authorsClient = new AuthorsClient();
        authorsClient.getAuthors();
        int authorId = AuthorUtils.getLastAuthorIdFromList(AuthorUtils.
                getAuthorsList(authorsClient.getResponse()));
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
        AuthorsClient authorsClient = new AuthorsClient();
        int bookId = -1;

        authorsClient.getAuthorsBooks(bookId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify authors books record cannot be gotten for zero book ID")
    public void verifyAuthorsBooksRecordCanNotBeGottenWithZeroBookId() {
        AuthorsClient authorsClient = new AuthorsClient();
        int bookId = 0;

        authorsClient.getAuthorsBooks(bookId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Verify authors books record cannot be gotten for not existing book ID")
    public void verifyAuthorsBooksRecordCanNotBeGottenWithNotExistingBookId() {
        AuthorsClient authorsClient = new AuthorsClient();
        int bookId = Integer.MAX_VALUE;

        authorsClient.getAuthorsBooks(bookId)
                .verifyStatusCode(HttpStatus.SC_NOT_FOUND);
    }
}
