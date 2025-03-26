package api.dataProviders;

import org.testng.annotations.DataProvider;

public class TestDataProvider {

    //DataProvider to supply invalid publish dates for testing. Can be used for same data format everywhere.
    @DataProvider(name = "invalidPublishDates")
    public Object[][] provideInvalidPublishDates() {
        return new Object[][]{
                //just random text
                {"Demonstration"},

                //non-existent date (February has only 28/29 days)
                {"2023-02-30T12:00:00"},

                //13th month does not exist
                {"2023-13-01T12:00:00"},

                //missing time (T12:00:00 is missing)
                {"2023-12-01"},

                //incorrect order (time is before date)
                {"12:00:00T2023-12-01"},

                //(empty string)
                {""},

                //(only spaces)
                {" "},

                //"/" instead of "-"
                {"2023/12/01T12:00:00"}
        };
    }
}
