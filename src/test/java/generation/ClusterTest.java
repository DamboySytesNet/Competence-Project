package generation;

import cluster.POICluster;
import org.junit.Test;

public class ClusterTest {

    @Test
    public void KMeanPOI(){
        //given:
        POICluster poiCluster = new POICluster(3, 10);

        //then:
        poiCluster.KMeanPOI();
    }
}
