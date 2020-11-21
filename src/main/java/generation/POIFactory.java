package generation;

import model.Geolocalization;
import model.POI;
import model.POIType;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import java.util.UUID;

public class POIFactory {
    private final ExponentialDistribution distanceExponentialDistribution;
    private final UniformRealDistribution angleUniformRealDistribution;
    private final UniformIntegerDistribution typeUniformDistribution;

    private static final POIFactory instance = new POIFactory();

    private POIFactory() {
        distanceExponentialDistribution = new ExponentialDistribution(0.5);
        angleUniformRealDistribution = new UniformRealDistribution();
        typeUniformDistribution = new UniformIntegerDistribution(0,2);
    }

    public static POIFactory getInstance() {
        return instance;
    }

    public POI generate() {

        Geolocalization geolocalization;
        do {
            double angle = angleUniformRealDistribution.sample() * 2 * Math.PI;
            double distance = distanceExponentialDistribution.sample();

            geolocalization = new Geolocalization(
                    GeneratorConsts.LATITUDE_CENTER + distance * Math.cos(angle),
                    GeneratorConsts.LONGITUDE_CENTER + distance * Math.sin(angle)
            );
        } while (!isGeolocalizationInBoundaries(geolocalization));

        return POI.builder()
                .id(UUID.randomUUID())
                .name("")
                .description("")
                .geolocalization(geolocalization)
                .type(POIType.getPOIType(typeUniformDistribution.sample()))
                .build();
    }

    private boolean isGeolocalizationInBoundaries(Geolocalization geolocalization) {
        if(GeneratorConsts.LATITUDE_CENTER - GeneratorConsts.LATITUDE_BOUNDARY
                > geolocalization.getLatitude() || geolocalization.getLatitude()
                > GeneratorConsts.LATITUDE_CENTER + GeneratorConsts.LATITUDE_BOUNDARY)
            return false;
        else if(GeneratorConsts.LONGITUDE_CENTER - GeneratorConsts.LONGITUDE_BOUNDARY
                > geolocalization.getLongitude() || geolocalization.getLongitude()
                > GeneratorConsts.LONGITUDE_CENTER + GeneratorConsts.LONGITUDE_BOUNDARY)
            return false;

        return true;
    }
}
