package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


@AllArgsConstructor
@ToString
@Getter
public class Trace {
    private User user;
    private POI poinOfInterest;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

//    public String toString() {
//        return user.getUserID() + " " + poinOfInterest.getGeolocalization(); //  + " " + entryTime + " " + exitTime;
//    }
}
