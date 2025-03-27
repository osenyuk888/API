package api.clients;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import net.javacrumbs.jsonunit.JsonAssert;
import net.javacrumbs.jsonunit.core.Option;

import java.io.File;

//parameterized to extend other clients
@Getter
@Setter
public abstract class Client<T> {

    protected abstract T getType();

    private Response response;

    @Step("Verifying status code")
    public T verifyStatusCode(int expectedStatusCode) {
        response.then().assertThat().statusCode(expectedStatusCode);
        return getType();
    }

    @Step("Verifying body matches json schema")
    public T verifyBodyMatchesJsonSchema(File expectedJsonSchema) {
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(expectedJsonSchema));
        return getType();
    }

    @Step("Verifying json body")
    public void verifyJsonBody(Object expectedBody) {
        String actualJsonBody = response.getBody().asString();
        String expectedJsonBody = new Gson().toJson(expectedBody);
        //assert of two json objects
        JsonAssert.assertJsonEquals(expectedJsonBody, actualJsonBody,
                JsonAssert.when(Option.IGNORING_EXTRA_FIELDS, Option.IGNORING_ARRAY_ORDER));
    }
}
