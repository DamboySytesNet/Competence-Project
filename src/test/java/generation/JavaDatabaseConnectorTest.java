package generation;

import connectors.JavaDatabaseConnector;
import model.Geolocalization;
import model.POI;
import model.POIType;
import model.User;
import model.UserGender;
import model.UserType;
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
        User stUser = new User(UUID.randomUUID(), "111222333",
                21, UserType.student, UserGender.female, "1");
        User ndUser = new User(UUID.fromString("e734b220-dffb-4e86-9ed3-e384afef233a"), "111222456",
                48, UserType.teacher, UserGender.helikopter_szturmowy, "1");

        //when:
        long numberOfUsersBefore = UserRepository.getTotalNumberOfUsers();
        boolean stAdded = UserRepository.save(stUser);

        User getUser = UserRepository.getById(stUser.getUserID());

        boolean ndAdded = UserRepository.save(ndUser);
        ndUser.setUserAge(49);
        ndUser.setUserGender(UserGender.female);
        boolean updated = UserRepository.updateById(ndUser);

        List<User> users = UserRepository.getAll();

        boolean deleted = UserRepository.delete(stUser.getUserID());
        boolean deleted2 = UserRepository.delete(UUID.fromString("e734b220-dffb-4e86-9ed3-e384afef233a"));

        //then:
        Assert.assertTrue(stAdded);
        Assert.assertTrue(ndAdded);
        Assert.assertTrue(updated);
        Assert.assertTrue(deleted);
        Assert.assertTrue(deleted2);
        Assert.assertEquals(numberOfUsersBefore + 2, users.size());
        Assert.assertEquals(getUser, stUser);
    }

    @Test
    public void checkPOICrud() throws SQLException {
        //given:
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
        boolean stAdded = POIRepository.save(stPOI);

        POI getPOI = POIRepository.getByName("testName");

        boolean ndAdded = POIRepository.save(ndPOI);
        ndPOI.getGeolocalization().setLatitude(22.69);
        ndPOI.setType(POIType.other);

        boolean updated = POIRepository.updateByName(ndPOI);

        List<POI> pois = POIRepository.getAll();

        boolean deleted = POIRepository.delete("testName");
        boolean deleted2 = POIRepository.delete("testName2");

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
