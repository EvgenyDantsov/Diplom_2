import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import step.UserStep;
import user.User;
import util.BaseTest;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class CreateUserParameterizedTest extends BaseTest {
    private UserStep userStep;
    private final User user;

    public CreateUserParameterizedTest(User user) {
        this.user = user;
    }

    @Parameterized.Parameters(name = "{index}: User = {0}")
    public static Object[][] getColorData() {
        return new Object[][]{
                {new User(null, "1234", "Evgeny")},
                {new User("diplom2@mail.ru", null, "Evgeny")},
                {new User("diplom2@mail.ru", "1234", null)}
        };
    }

    @Before
    public void setUp() {
        userStep = new UserStep();
    }

    @Test
    @DisplayName("Create user without one required field")
    public void createUserWithoutRequiredFieldTest() {
        userStep.createUser(user)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}