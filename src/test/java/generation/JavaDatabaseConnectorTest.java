package generation;

import dbconnector.JavaDatabaseConnector;
import model.Geolocalization;
import model.POI;
import model.POIType;
import org.junit.Assert;
import org.junit.Test;
import repository.POIRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
