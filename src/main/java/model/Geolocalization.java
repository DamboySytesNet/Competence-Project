package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Geolocalization {
    private static final double EARTH_RADIUS = 6371000.0; // m

    // x coordinate
    private double latitude;
    // y coordinate
    private double longitude;

    public double getDistance(Geolocalization other) {
        return this.haversineDistance(other);
    }

    private double haversineDistance(Geolocalization other) {
        // https://www.movable-type.co.uk/scripts/latlong.html
        double phi1 = Math.toRadians(this.latitude);
        double phi2 = Math.toRadians(other.getLatitude());
        double deltaPhi = Math.abs(phi1 - phi2);
        double deltaLambda = Math.toRadians(Math.abs(this.longitude - other.getLongitude()));

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
                + Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS * c;

        return distance;
    }

}
