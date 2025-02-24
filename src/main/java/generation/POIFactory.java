package generation;

import model.Geolocalization;
import model.POI;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import reader.POIDataContainer;

import java.util.UUID;

public class POIFactory {
    private final ExponentialDistribution distanceExponentialDistribution;
    private final UniformRealDistribution angleUniformRealDistribution;
    private final POIRandomizer poiRandomizer;

    private static final POIFactory instance = new POIFactory();

    private POIFactory() {
        distanceExponentialDistribution = new ExponentialDistribution(0.5);
        angleUniformRealDistribution = new UniformRealDistribution();
        poiRandomizer = new POIRandomizer();
    }

    public static POIFactory getInstance() {
        return instance;
    }

    public POI generate() {
        return generate("");
    }

    public POI generate(String experimentId) {

        Geolocalization geolocalization;
        do {
            double angle = angleUniformRealDistribution.sample() * 2 * Math.PI;
            double distance = distanceExponentialDistribution.sample();

            geolocalization = new Geolocalization(
                    GeneratorConsts.LATITUDE_CENTER + distance * Math.cos(angle),
                    GeneratorConsts.LONGITUDE_CENTER + distance * Math.sin(angle)
            );
        } while (!isGeolocalizationInBoundaries(geolocalization));

        POIDataContainer additionalData = poiRandomizer.getRandomPOIAdditionalData();

        return POI.builder()
                .id(UUID.randomUUID())
                .name(additionalData.getName())
                .description(additionalData.getDescription())
                .geolocalization(geolocalization)
                .type(additionalData.getType())
                .experimentId(experimentId)
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
