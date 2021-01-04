package generation;

import model.User;
import org.junit.Assert;
import org.junit.Test;

public class UserFactoryTest {

    @Test
    public void generateTest() {
        User user;


        for (int i = 0; i < 100; i++) {
            user = UserFactory.getInstance().generate();
            Assert.assertFalse(user.getPhoneNumber().startsWith("0"));
        }
    }
}
