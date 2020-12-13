package generation;

import dbconnector.JavaDatabaseConnector;
import model.POI;
import model.User;
import model.UserGender;
import model.UserType;
import model.Geolocalization;
import model.POIType;
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


    /**
     * Przed wykonaniem poniższych testow w bazie danych muszą byc dodadne odpowiednie krotki -
     * znajduja sie one w V2__insert_init_values.sql
     **/

    @Test
    public void checkUserCrud() throws SQLException {
        //given:
        String query = "SELECT * FROM `competence-schema`.`persons`";
        UserRepository userRepository = new UserRepository();
        User stUser = User.builder().userAge(21).userGender(UserGender.female)
                .userID(UUID.randomUUID()).userType(UserType.student).experimentId("1")
                .phoneNumber("111222333").build();
        User ndUser = User.builder().userAge(48).userGender(UserGender.helikopter_szturmowy)
                .userID(UUID.randomUUID()).userType(UserType.teacher).experimentId("1")
                .phoneNumber("111222456").build();

        //when:
        boolean stAdded = userRepository.save(stUser);

        User getUser = userRepository.getById(stUser.getUserID());

        boolean ndAdded = userRepository.save(ndUser);
        ndUser.setUserAge(49);
        ndUser.setUserGender(UserGender.female);
        boolean updated = userRepository.updateById(ndUser);

        List<User> users = userRepository.getAll();

        boolean deleted = userRepository.delete(stUser.getUserID());
        boolean deleted2 = userRepository.delete(ndUser.getUserID());

        //then:
        Assert.assertTrue(stAdded);
        Assert.assertTrue(ndAdded);
        Assert.assertTrue(updated);
        Assert.assertTrue(deleted);
        Assert.assertTrue(deleted2);
        Assert.assertEquals(2, users.size());
        Assert.assertEquals(getUser, stUser);
    }

    @Test
    public void checkPOICrud() throws SQLException {
        //given:
        POIRepository poiRepository = new POIRepository();

        POI stPOI = POI.builder()
                .name("testName")
                .description("testDescription")
                .geolocalization(new Geolocalization(22.11, 33.43))
                .type(POIType.outdoor)
                .experimentId("1")
                .build();

        POI ndPOI = POI.builder()
                .name("testName2")
                .description("testDescriptionABC")
                .geolocalization(new Geolocalization(12.12, 66.13))
                .type(POIType.outdoor)
                .experimentId("1")
                .build();


        //when:
        boolean stAdded = poiRepository.save(stPOI);

        POI getPOI = poiRepository.getByName("testName");

        boolean ndAdded = poiRepository.save(ndPOI);
        ndPOI.getGeolocalization().setLatitude(22.69);
        ndPOI.setType(POIType.other);

        boolean updated = poiRepository.updateByName(ndPOI);

        List<POI> pois = poiRepository.getAll();

        boolean deleted = poiRepository.delete("testName");
        boolean deleted2 = poiRepository.delete("testName2");

        //then:
        Assert.assertTrue(stAdded);
        Assert.assertTrue(ndAdded);
        Assert.assertTrue(updated);
        Assert.assertTrue(deleted);
        Assert.assertTrue(deleted2);
        Assert.assertEquals(2, pois.size());
        Assert.assertEquals(getPOI, stPOI);
    }
}
