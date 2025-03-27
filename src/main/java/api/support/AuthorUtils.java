package api.support;

import api.pojo.Author;
import io.qameta.allure.Step;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;

import java.util.List;

public class AuthorUtils {

    @Step("Getting authors list")
    public static List<Author> getAuthorsList(Response response) {
        return response.getBody().as(new TypeRef<>() {
        });
    }

    @Step("Getting last author ID from list")
    public static int getLastAuthorIdFromList(List<Author> authorList) {
        return authorList.get(authorList.size() - 1).getId();
    }
}
