package step;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import user.User;

import static io.restassured.RestAssured.given;

public class GetOrderStep {
    private static final String REGISTER_USER = "/api/auth/register";
    public static final String DELETED_OR_UPDATE_USER = "/api/auth/user";
    private static final String ORDERS = "/api/orders";

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
                .contentType(ContentType.JSON)
                .when()
                .delete(DELETED_OR_UPDATE_USER)
                .then();
    }

    @Step("Get orders for an authorized user")
    public ValidatableResponse getOrdersAuthorizedUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get(ORDERS)
                .then();
    }

    @Step("Get orders for an unauthorized user")
    public ValidatableResponse getOrdersUnauthorizedUser() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(ORDERS)
                .then();
    }
}