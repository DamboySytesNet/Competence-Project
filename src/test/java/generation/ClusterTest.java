package generation;

import cluster.POICluster;
import model.Geolocalization;
import model.POI;
import model.POIType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ClusterTest {

    @Test
    public void KMeanPOI() {
        //given:
        POICluster poiCluster = new POICluster(3, 10);

        List<POI> pois = new ArrayList<>();
        pois.add(new POI("A1", "desc", new Geolocalization(49.14, 15.11), POIType.outdoor, "1"));
        pois.add(new POI("A2", "desc", new Geolocalization(46.14, 18.11), POIType.outdoor, "1"));
        pois.add(new POI("B1", "desc", new Geolocalization(29.14, 15.11), POIType.outdoor, "1"));
        pois.add(new POI("C1", "desc", new Geolocalization(66.14, 65.11), POIType.outdoor, "1"));
        pois.add(new POI("C2", "desc", new Geolocalization(61.14, 75.21), POIType.outdoor, "1"));
        pois.add(new POI("B2", "desc", new Geolocalization(22.14, 19.11), POIType.outdoor, "1"));
        pois.add(new POI("B3", "desc", new Geolocalization(29.14, 9.21), POIType.outdoor, "1"));


        //then:
        poiCluster.KMeanPOI(pois);
    }
}
