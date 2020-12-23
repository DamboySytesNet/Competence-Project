package model;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class POI {

    private UUID id;
    private String name;
    private String description;
    private Geolocalization geolocalization;
    private POIType type;
    private String experimentId;

}
