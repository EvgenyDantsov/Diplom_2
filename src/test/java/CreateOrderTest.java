import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import step.CreateOrderStep;
import user.User;
import util.BaseTest;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;

public class CreateOrderTest extends BaseTest {
    private CreateOrderStep createOrderStep;
    private String accessToken;
    private User user;
    private List<String> ingredientHash;

    @Before
    public void setUp() {
        createOrderStep = new CreateOrderStep();
        user = new User("diplom2@mail.ru", "1234", "Evgeny");
        ValidatableResponse response = createOrderStep.createUser(user);
        accessToken = response.extract().path("accessToken").toString();
        ingredientHash = createOrderStep.getRandomIngredients();
    }

    @After
    public void tearDown() {
        createOrderStep.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Create order with authorized user")
    public void createOrderAuthorizedTest() {
        createOrderStep.createOrderAuthorized(accessToken, ingredientHash)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }

    //Баг, т.к. мы ожидаем, что будет код 401, но заказ оформляется и приходит код 200.
    //Но в тоже время на сайте мы не можем оформить заказ без авторизации.
    @Test
    @DisplayName("Create order without authorized user")
    public void createOrderUnauthorizedTest() {
        createOrderStep.createOrderUnauthorized(ingredientHash)
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Create order without ingredients")
    public void createOrderNoIngredientsTest() {
        createOrderStep.createOrderNoIngredients(accessToken)
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order with invalid ingredients hash")
    public void createOrderInvalidIngredientHashTest() {
        createOrderStep.createOrderInvalidIngredient(accessToken)
                .statusCode(500)
                .body(containsString("Internal Server Error"));
    }
}