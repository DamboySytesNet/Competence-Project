package cluster.clusterable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.POI;
import org.apache.commons.math3.ml.clustering.Clusterable;

@AllArgsConstructor
@Getter
public class POIWrapper implements Clusterable {

    private final POI poi;

    @Override
    public double[] getPoint() {
        return new double [] { poi.getGeolocalization().getLatitude(),
                poi.getGeolocalization().getLongitude() };
    }
}
