package generation;

import model.Geolocalization;
import model.POI;
import org.junit.Assert;
import org.junit.Test;

public class POIFactoryTest {

    @Test
    public void generateTest() {
        POI poi;
        Geolocalization geolocalization;

        for (int i = 0; i < 20; i++) {
            poi = POIFactory.getInstance().generate();
            geolocalization = poi.getGeolocalization();
            Assert.assertTrue(geolocalization.getLatitude()
                    > GeneratorConsts.LATITUDE_CENTER - GeneratorConsts.LATITUDE_BOUNDARY
            );
            Assert.assertTrue(geolocalization.getLatitude()
                    < GeneratorConsts.LATITUDE_CENTER + GeneratorConsts.LATITUDE_BOUNDARY
            );
            Assert.assertTrue(geolocalization.getLongitude()
                    > GeneratorConsts.LONGITUDE_CENTER - GeneratorConsts.LONGITUDE_BOUNDARY
            );
            Assert.assertTrue(geolocalization.getLongitude()
                    < GeneratorConsts.LONGITUDE_CENTER + GeneratorConsts.LONGITUDE_BOUNDARY
            );
        }
    }
}
