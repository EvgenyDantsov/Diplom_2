package util;

import io.restassured.RestAssured;
import org.junit.Before;

public class BaseTest {
    @Before
    public void setupRestAssured(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
}