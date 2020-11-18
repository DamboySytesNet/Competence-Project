package generation;

import dbconnector.JavaDatabaseConnector;
import model.*;
import org.junit.Assert;
import org.junit.Test;
import repository.POIRepository;
import repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

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

    @Test
    public void checkUserCrud() throws SQLException {
        //given:
        String query = "SELECT * FROM `competence-schema`.`persons`";

        UserRepository userRepository = new UserRepository();

        //when:
        boolean st_added = userRepository.save(new User(UUID.fromString("e734b220-dffb-4e86-9ed3-e384afef233f"), "111222333",
                21, UserType.student, UserGender.female));

        User st_user = userRepository.getById(UUID.fromString("e734b220-dffb-4e86-9ed3-e384afef233f"));

        boolean nd_added = userRepository.save(new User(UUID.fromString("e734b220-dffb-4e86-9ed3-e384afef233a"), "111222456",
                48, UserType.teacher, UserGender.helikopter_szturmowy));

        boolean updated = userRepository.updateById(new User(UUID.fromString("e734b220-dffb-4e86-9ed3-e384afef233a"), "222222333",
                18, UserType.teacher, UserGender.helikopter_szturmowy));

        List<User> users = userRepository.getAll();

        boolean deleted = userRepository.delete(UUID.fromString("e734b220-dffb-4e86-9ed3-e384afef233f"));
        boolean deleted2 = userRepository.delete(UUID.fromString("e734b220-dffb-4e86-9ed3-e384afef233a"));

        //then:
        Assert.assertTrue(st_added);
        Assert.assertTrue(nd_added);
        Assert.assertTrue(updated);
        Assert.assertTrue(deleted);
        Assert.assertTrue(deleted2);
        Assert.assertEquals(2, users.size());
        Assert.assertEquals(st_user, new User(UUID.fromString("e734b220-dffb-4e86-9ed3-e384afef233f"), "111222333",
                21, UserType.student, UserGender.female));
    }

    /**
     * Przed wykonaniem testow w bazie danych muszą byc odpowiednie krotki w tabelach powiazanych z POIType i Experiment
     **/
    @Test
    public void checkPOICrud() throws SQLException {
        //given:
        POIRepository poiRepository = new POIRepository();

        //when:
        boolean st_added = poiRepository.save(new POI("testName", "testDescription",
                new Geolocalization(22.11, 33.43), POIType.outdoor, "1"));

        POI st_poi = poiRepository.getByName("testName");

        boolean nd_added = poiRepository.save(new POI("testName2", "testDescriptionABC",
                new Geolocalization(12.12, 66.13), POIType.outdoor, "1"));

        boolean updated = poiRepository.updateByName(new POI("testName2", "testDescriptionABC",
                new Geolocalization(22.6969, 33.43), POIType.other, "1"));

        List<POI> pois = poiRepository.getAll();

        boolean deleted = poiRepository.delete("testName");
        boolean deleted2 = poiRepository.delete("testName2");

        //then:
        Assert.assertTrue(st_added);
        Assert.assertTrue(nd_added);
        Assert.assertTrue(updated);
        Assert.assertTrue(deleted);
        Assert.assertTrue(deleted2);
        Assert.assertEquals(2, pois.size());
        Assert.assertEquals(st_poi, new POI("testName", "testDescription",
                new Geolocalization(22.11, 33.43), POIType.outdoor, "1"));
    }
}
