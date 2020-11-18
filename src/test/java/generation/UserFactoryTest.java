package generation;

import model.Geolocalization;
import model.User;
import org.junit.Assert;
import org.junit.Test;

public class UserFactoryTest {

    @Test
    public void generateTest() {
        User user;


        for (int i = 0; i < 100; i++) {
            user = UserFactory.getInstance().generate();
            Assert.assertTrue(!user.getPhoneNumber().startsWith("0"));
        }
    }
}
