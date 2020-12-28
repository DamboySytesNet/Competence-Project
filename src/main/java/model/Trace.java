package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@ToString
@Getter
@Builder
public class Trace {
    private final UUID id;
    private final User user;
    private final POI pointOfInterest;
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;

}
