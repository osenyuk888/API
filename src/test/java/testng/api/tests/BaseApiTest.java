package testng.api.tests;

import api.listeners.TestListener;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners(TestListener.class)
public class BaseApiTest {

    @BeforeSuite(description = "Set Base URI")
    public void setBaseURI() {
        RestAssured.baseURI = "https://fakerestapi.azurewebsites.net";
    }
}
