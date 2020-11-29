package generation;


import reader.CsvReader;
import reader.POIDataContainer;

import java.util.List;

public class POIRandomizer {
    private final List<POIDataContainer> poiDataContainerList;
    private final double totalWeight;

    public POIRandomizer() {
        this.poiDataContainerList = CsvReader.readPOIDataFromCSV("poi-additional-data.csv");
        this.totalWeight = poiDataContainerList.stream()
                .mapToDouble(poi -> poi.getWeight())
                .sum();
    }

    public POIDataContainer getRandomPOIAdditionalData() {
        double r = Math.random() * totalWeight;
        for (POIDataContainer poiDataContainer : poiDataContainerList) {
            r -= poiDataContainer.getWeight();
            if (r <= 0.0) {
                return poiDataContainer;
            }
        }
        return poiDataContainerList.get(0);
    }
}
