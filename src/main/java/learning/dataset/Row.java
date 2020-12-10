package learning.dataset;

import lombok.AllArgsConstructor;
import model.Geolocalization;
import model.POI;
import model.Trace;
import model.User;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class Row {
    private User user;
    private List<Trace> traces;
    //private int label;

    public Float[] getData() {
        float userType = user.getUserType().ordinal();
        float userGender = user.getUserGender().ordinal();
        float userAge = user.getUserAge();
        List<Float> x = traces.stream()
                .map(Trace::getPointOfInterest)
                .map(POI::getGeolocalization)
                .mapToDouble(Geolocalization::getLatitude)
                .boxed()
                .map(Float::new)
                .collect(Collectors.toList());
        List<Float> y = traces.stream()
                .map(Trace::getPointOfInterest)
                .map(POI::getGeolocalization)
                .mapToDouble(Geolocalization::getLongitude)
                .boxed()
                .map(Float::new)
                .collect(Collectors.toList());

        List<Float> values = new LinkedList<>();
        values.add(userAge);
        values.add(userType);
        values.add(userGender);
        values.addAll(x);
        values.addAll(y);

        //float[] xx = (float)x;
        //do dokonczenia

        Float[] table = values.toArray(Float[]::new);
        return table;
    }

    public int getLabel() {
        return (int)Math.round(Math.random() * 2);
        //return label;
    }
}
