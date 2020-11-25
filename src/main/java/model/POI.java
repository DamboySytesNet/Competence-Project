package model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class POI {

    private String name;
    private String description;
    private Geolocalization geolocalization;
    private POIType type;
    private String experimentId;

}
