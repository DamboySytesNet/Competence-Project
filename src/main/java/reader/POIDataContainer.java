package reader;

import lombok.Getter;
import lombok.ToString;
import model.POIType;

@ToString
@Getter
public class POIDataContainer {
    private final double weight;
    private final POIType type;
    private final String name;
    private final String description;

    public POIDataContainer(String[] attributes) {
        this.weight = Double.parseDouble(attributes[0]);
        this.type = POIType.getPOIType(Integer.parseInt(attributes[1]));
        this.name = attributes[2];
        this.description = attributes[3];
    }
}
