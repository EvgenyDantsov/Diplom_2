import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import step.UserStep;
import user.User;
import util.BaseTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateUserTest extends BaseTest {
    private User user;
    private UserStep userStep;
    // Флаг для отслеживания создания пользователя
    private boolean userCreated;
    private String accessToken;

    @Before
    public void setUp() {
        userStep = new UserStep();
        userCreated = false;
        user = new User("diplom2@mail.ru", "1234", "Evgeny");
    }

    @After
    public void tearDown() {
        if (userCreated) {
            userStep.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Create new unique user")
    public void createUniqueUserTest() {
        ValidatableResponse response = userStep.createUser(user)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
        userCreated = true;
        accessToken = response.extract().path("accessToken").toString();
    }

    @Test
    @DisplayName("Create user that is already registered")
    public void createAlreadyRegisteredUserTest() {
        ValidatableResponse response = userStep.createUser(user);
        userCreated = true;
        accessToken = response.extract().path("accessToken").toString();
        userStep.createUser(user)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }
}