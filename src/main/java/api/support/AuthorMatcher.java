package api.support;

import api.pojo.Author;
import org.testng.Assert;

import java.util.List;
import java.util.function.Predicate;

public class AuthorMatcher {

    /**
     * Checks if all authors in the list have the specified bookId.
     * This approach can also be applied to books.
     * Consider creating a generic validation class for all controllers.
     *
     * @param authors List of authors.
     * @param bookId  Expected book ID.
     */
    public static void hasBookId(List<Author> authors, int bookId) {
        List<Author> invalidAuthors = authors.stream()
                .filter(author -> !bookIdEquals(author, bookId))
                .toList();

        Assert.assertTrue(invalidAuthors.isEmpty(),
                "Some authors do not have the expected bookId: " + bookId + "\nInvalid authors: " + invalidAuthors);
    }

    /**
     * Generic method to validate authors list against a custom predicate.
     *
     * @param authors   List of authors.
     * @param condition Predicate condition for validation.
     * @param message   Custom failure message.
     */
    public static void validateAuthors(List<Author> authors, Predicate<Author> condition, String message) {
        boolean allMatch = authors.stream().allMatch(condition);
        Assert.assertTrue(allMatch, message);
    }

    private static boolean bookIdEquals(Author author, int expectedBookId) {
        return author.getIdBook() != null && author.getIdBook().equals(expectedBookId);
    }
}