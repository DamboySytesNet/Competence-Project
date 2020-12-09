package generation;

import org.junit.Assert;
import org.junit.Test;
import reader.POIDataContainer;

public class POIRandomizerTest {
    @Test
    public void weightTest() {
        POIRandomizer poiRandomizer = new POIRandomizer();
        POIDataContainer poiDataContainer;
        double weight;

        for (int i=0; i<200; ++i) {
            poiDataContainer = poiRandomizer.getRandomPOIAdditionalData();
            weight = poiDataContainer.getWeight();

            Assert.assertTrue(weight > 0);
        }
    }
}
