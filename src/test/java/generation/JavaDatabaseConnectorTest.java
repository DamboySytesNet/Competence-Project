package generation;

import dbconnector.JavaDatabaseConnector;
import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;

public class JavaDatabaseConnectorTest {

    @Test
    public void checkConnection() {
        //given:
        String query = "SELECT * FROM `competence-schema`.`poi_types`";

        //when:
        ResultSet resultSet = JavaDatabaseConnector.doOperation(query);

        //then:
        Assert.assertNotEquals(null, resultSet);
    }
}
