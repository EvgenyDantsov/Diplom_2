import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import step.GetOrderStep;
import user.User;
import util.BaseTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderUserTest extends BaseTest {
    private GetOrderStep getOrderStep;
    private String accessToken;
    private User user;

    @Before
    public void setUp() {
        getOrderStep = new GetOrderStep();
        user = new User("diplom2@mail.ru", "1234", "Evgeny");
        ValidatableResponse response = getOrderStep.createUser(user);
        accessToken = response.extract().path("accessToken").toString();
    }

    @After
    public void tearDown() {
        getOrderStep.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Get order for authorized user")
    public void getOrdersAuthorizedUserTest() {
        getOrderStep.getOrdersAuthorizedUser(accessToken)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue())
                .body("total", notNullValue())
                .body("totalToday", notNullValue());
    }

    @Test
    @DisplayName("Get order for unauthorized user")
    public void getOrdersUnauthorizedUserTest() {
        getOrderStep.getOrdersUnauthorizedUser()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}