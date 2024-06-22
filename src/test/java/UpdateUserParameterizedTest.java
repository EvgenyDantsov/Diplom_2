import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import step.UpdateUserStep;
import user.User;
import util.BaseTest;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class UpdateUserParameterizedTest extends BaseTest {
    private UpdateUserStep updateUserStep;
    private String accessToken;
    private User user;
    private final String fieldName;
    private final String newValue;

    public UpdateUserParameterizedTest(String fieldName, String newValue) {
        this.fieldName = fieldName;
        this.newValue = newValue;
    }

    @Parameterized.Parameters(name = "{index}: Update field: {0} = {1}")
    public static Object[][] getColorData() {
        return new Object[][]{
                {"name", "EvgenySmith"},
                {"email", "diplom0@mail.ru"}
        };
    }

    @Before
    public void setUp() {
        updateUserStep = new UpdateUserStep();
        user = new User("diplom2@mail.ru", "1234", "Evgeny");
        ValidatableResponse response = updateUserStep.createUser(user);
        accessToken = response.extract().path("accessToken").toString();

    }

    @After
    public void tearDown() {
        updateUserStep.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Update user field with authorization")
    public void updateUserFieldAuthorizedTest() {
        updateUserStep.updateUserAuthorized(accessToken, fieldName, newValue)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user." + fieldName, equalTo(newValue));
    }

    @Test
    @DisplayName("Update user data without authorization")
    public void updateUserUnauthorizedTest() {
        updateUserStep.updateUserUnauthorized(fieldName, newValue)
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}