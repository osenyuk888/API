package api.support;

import api.pojo.Book;
import io.qameta.allure.Step;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;

import java.util.List;

public class BookUtils {

    @Step("Getting books list")
    public static List<Book> getBooksList(Response response) {
        return response.getBody().as(new TypeRef<>() {
        });
    }

    @Step("Getting last book ID from list")
    public static int getLastBookIdFromList(List<Book> booksList) {
        return booksList.get(booksList.size() - 1).getId();
    }
}
