package model;

import lombok.*;

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
