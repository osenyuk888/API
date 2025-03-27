package api.listeners;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.mapper.ObjectMapperType;
import lombok.extern.slf4j.Slf4j;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class TestListener implements IInvokedMethodListener, ITestListener {

    @Override
    public void onStart(ITestContext context) {
        //config for serializasion deserialization
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.GSON));
        //add filter to automatically listen tests and add details request response
        RestAssured.filters(new AllureRestAssured());
        //RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter()); //enable this line for debug
        //add possibility to print logs for debug
        if (log.isDebugEnabled()) {
            RestAssured.filters(new AllureRestAssured());
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info(result.getMethod().getMethodName() + " started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info(result.getMethod().getMethodName() + " passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.info(result.getMethod().getMethodName() + " failed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.info(result.getMethod().getMethodName() + " skipped");
    }
}