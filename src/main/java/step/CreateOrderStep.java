package step;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class CreateOrderStep {
    private static final String INGREDIENTS = "/api/ingredients";
    private static final String REGISTER_USER = "/api/auth/register";
    public static final String DELETED_USER = "/api/auth/user";
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
                .delete(DELETED_USER)
                .then();
    }

    @Step("Get all ingredient")
    public List<String> getRandomIngredients() {
        ValidatableResponse response = given()
                .contentType(ContentType.JSON)
                .when()
                .get(INGREDIENTS)
                .then();
        List<String> ingredientHash = response.extract().path("data._id");
        return selectedOfRandomIngredients(ingredientHash);
    }

    private List<String> selectedOfRandomIngredients(List<String> ingredientHash) {
        List<String> selectedIngredientsHash = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < 2; i++) {
            int randomIndex = rand.nextInt(ingredientHash.size());
            selectedIngredientsHash.add(ingredientHash.get(randomIndex));
        }
        return selectedIngredientsHash;
    }

    @Step("Create order with authorization")
    public ValidatableResponse createOrderAuthorized(String accessToken, List<String> ingredients) {
        // Формируем JSON-объект с ключом "ingredients"
        JsonObject requestJson = new JsonObject();
        requestJson.add("ingredients", (new Gson().toJsonTree(ingredients)));
        return given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body(requestJson.toString())
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Create order without authorization")
    public ValidatableResponse createOrderUnauthorized(List<String> ingredients) {
        // Формируем JSON-объект с ключом "ingredients"
        JsonObject requestJson = new JsonObject();
        requestJson.add("ingredients", (new Gson().toJsonTree(ingredients)));
        return given()
                .contentType(ContentType.JSON)
                .body(requestJson.toString())
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Create order without ingredients")
    public ValidatableResponse createOrderNoIngredients(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body("")
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Create order with invalid ingredient ID")
    public ValidatableResponse createOrderInvalidIngredient(String accessToken) {
        // Формируем JSON-объект с ключом "ingredients"
        JsonObject requestJson = new JsonObject();
        requestJson.add("ingredients", new Gson().toJsonTree(List.of("60", "6096")));
        return given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body(requestJson.toString())
                .when()
                .post(ORDERS)
                .then();
    }
}