package model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Getter
@Setter
@Builder
public class TraceData {

    private final UUID id;
    private final UUID userId;
    private final UUID pointOfInterestId;
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;
    private UUID previousTraceId;
    private final UUID experimentId;
}
