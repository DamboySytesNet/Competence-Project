package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


@AllArgsConstructor
@ToString
@Getter
public class Trace {
    private final User user;
    private final POI pointOfInterest;
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;

}
