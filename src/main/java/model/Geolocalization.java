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

    // x coordinate
    private double latitude;
    // y coordinate
    private double longitude;

    public double getDistance(Geolocalization other) {
        return Math.sqrt(this.latitude * other.getLatitude() + this.longitude * other.getLongitude());
    }

}
