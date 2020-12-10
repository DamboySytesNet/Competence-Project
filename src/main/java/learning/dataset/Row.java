package learning.dataset;

import lombok.AllArgsConstructor;
import model.Geolocalization;
import model.Trace;
import model.User;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
public class Row {
    private User user;
    private List<Trace> traces;
    private int label;

    public float[] getData() {
        float userType = user.getUserType().ordinal();
        float userGender = user.getUserGender().ordinal();
        float userAge = user.getUserAge();
        List<Float> xy = new LinkedList<>();

        Geolocalization geolocalization;
        for (int i=0; i<traces.size()*2; i=i+2) {
            geolocalization = traces.get(i).getPointOfInterest().getGeolocalization();
            xy.add((float)geolocalization.getLatitude());
            xy.add((float)geolocalization.getLongitude());
        }

        List<Float> values = new LinkedList<>();
        values.add(userAge);
        values.add(userType);
        values.add(userGender);
        values.addAll(xy);

        float[] row = new float[values.size()];
        for (int i=0; i<values.size(); ++i) {
            row[i] = values.get(i);
        }
        return row;
    }

    public int getLabel() {
        return label;
    }
}
