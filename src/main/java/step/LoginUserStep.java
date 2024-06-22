package step;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import user.User;

import static io.restassured.RestAssured.given;

public class LoginUserStep {
    private static final String REGISTER_USER = "/api/auth/register";
    public static final String DELETED_USER = "/api/auth/user";
    public static final String LOGIN_USER = "/api/auth/login";
    public static final String LOGOUT_USER = "/api/auth/logout";

    @Step("Create user")
    public ValidatableResponse createUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(REGISTER_USER)
                .then();
    }

    @Step("Login user")
    public ValidatableResponse loginUser(String email, String password) {
        return given()
                .contentType(ContentType.JSON)
                .body(new User(email, password))
                .when()
                .post(LOGIN_USER)
                .then();
    }

    @Step("Delete the user")
    public void deleteUser(String accessToken) {
        given()
                .header("Authorization", accessToken)
                .when()
                .delete(DELETED_USER)
                .then();
    }

    @Step("Logout user")
    public void logoutUser(String refreshToken) {
        given()
                .contentType(ContentType.JSON)
                .body("{\"token\":\"" + refreshToken + "\"}")
                .when()
                .post(LOGOUT_USER)
                .then();
    }
}