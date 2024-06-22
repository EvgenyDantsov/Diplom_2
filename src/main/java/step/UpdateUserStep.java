package step;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import user.User;

import static io.restassured.RestAssured.given;

public class UpdateUserStep {
    private static final String REGISTER_USER = "/api/auth/register";
    public static final String DELETED_USER = "/api/auth/user";
    public static final String UPDATE_USER = "/api/auth/user";

    @Step("Create user")
    public ValidatableResponse createUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(REGISTER_USER)
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

    @Step("Update user data with authorization")
    public ValidatableResponse updateUserAuthorized(String accessToken, String fieldName, String newValue) {
        return given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body("{\"" + fieldName + "\":\"" + newValue + "\"}")
                .when()
                .patch(UPDATE_USER)
                .then();
    }

    @Step("Attempt to update user data without authorization")
    public ValidatableResponse updateUserUnauthorized(String fieldName, String newValue) {
        return given()
                .contentType(ContentType.JSON)
                .body("{\"" + fieldName + "\":\"" + newValue + "\"}")
                .when()
                .patch(UPDATE_USER)
                .then();
    }
}