import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import step.LoginUserStep;
import user.User;
import util.BaseTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginUserTest extends BaseTest {
    private LoginUserStep loginUserStep;
    private String accessToken;
    private String refreshToken;
    private User user;

    @Before
    public void setUp() {
        loginUserStep = new LoginUserStep();
        user = new User("diplom2@mail.ru", "1234", "Evgeny");
        ValidatableResponse response = loginUserStep.createUser(user);
        accessToken = response.extract().path("accessToken").toString();
        refreshToken = response.extract().path("refreshToken").toString();
        loginUserStep.logoutUser(refreshToken);
    }

    @After
    public void tearDown() {
        loginUserStep.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Login with existing user")
    public void loginWithExistingUserTest() {
        loginUserStep.loginUser(user.getEmail(), user.getPassword())
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Login with invalid email")
    public void loginWithInvalidEmailTest() {
        loginUserStep.loginUser("diplom2", user.getPassword())
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Login with invalid password")
    public void loginWithInvalidPasswordTest() {
        loginUserStep.loginUser(user.getEmail(), "1111")
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}