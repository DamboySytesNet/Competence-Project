package generation;

import model.Geolocalization;
import model.POI;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

public class POIFactory {
    private ExponentialDistribution exponentialDistribution;
    private UniformRealDistribution uniformRealDistribution;

    private static POIFactory instance = new POIFactory();

    private POIFactory() {
        exponentialDistribution = new ExponentialDistribution(0.5);
        uniformRealDistribution = new UniformRealDistribution();
    }

    public static POIFactory getInstance() {
        return instance;
    }

    public POI generate() {

        Geolocalization geolocalization;
        do {
            double angle = uniformRealDistribution.sample() * 2 * Math.PI;
            double distance = exponentialDistribution.sample();

            geolocalization = new Geolocalization(
                    GeneratorConsts.LATITUDE_C + distance * Math.cos(angle),
                    GeneratorConsts.LONGITUDE_C + distance * Math.sin(angle)
            );
        } while (!isGeolocalizationInBoundaries(geolocalization));

        return POI.builder()
                .geolocalization(geolocalization)
                .build();
    }

    private boolean isGeolocalizationInBoundaries(Geolocalization geolocalization) {
        if(GeneratorConsts.LATITUDE_C - GeneratorConsts.LATITUDE_B > geolocalization.getLatitude()
                || geolocalization.getLatitude() > GeneratorConsts.LATITUDE_C + GeneratorConsts.LATITUDE_B)
            return false;
        else if(GeneratorConsts.LONGITUDE_C - GeneratorConsts.LONGITUDE_B > geolocalization.getLongitude()
                || geolocalization.getLongitude() > GeneratorConsts.LONGITUDE_C + GeneratorConsts.LONGITUDE_B)
            return false;

        return true;
    }
}
