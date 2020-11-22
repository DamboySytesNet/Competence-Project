package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Geolocalization {

    // x coordinate
    private double latitude;
    // y coordinate
    private double longitude;

}
