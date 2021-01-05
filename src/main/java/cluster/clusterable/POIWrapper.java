package cluster.clusterable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import model.POI;
import org.apache.commons.math3.ml.clustering.Clusterable;

@Getter
@EqualsAndHashCode
public class POIWrapper implements Clusterable {

    private final POI poi;
    private double freqUser = -1;
    private double lengthOfStay = -1;

    public POIWrapper(POI poi, double freqUser, double lengthOfStay) {
        this.poi = poi;
        this.freqUser = freqUser;
        this.lengthOfStay = lengthOfStay;
    }

    public POIWrapper(POI poi, double freqUser) {
        this.poi = poi;
        this.freqUser = freqUser;
    }

    public POIWrapper(POI poi) {
        this.poi = poi;
    }

    @Override
    public double[] getPoint() {

        if (freqUser >= 0.0)
            return new double [] { freqUser };
        else if (lengthOfStay >= 0.0)
            return new double [] { lengthOfStay };
        else
            return new double [] { poi.getGeolocalization().getLatitude(),
                    poi.getGeolocalization().getLongitude() };
    }
}
