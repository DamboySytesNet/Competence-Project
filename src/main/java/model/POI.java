package model;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class POI {

    private String name;
    private String description;
    private Geolocalization geolocalization;
    private POIType type;
}
