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
public class TraceData {

    private final UUID id;
    private final UUID userId;
    private final UUID pointOfInterestId;
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;

}
