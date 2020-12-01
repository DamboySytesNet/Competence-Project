package repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import model.TraceData;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TraceRepository {

    private static final String TABLE_NAME = "traces";

    private final Session session;

    public TraceRepository(Session session) {
        this.session = session;
    }

    public void createTable() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(TABLE_NAME)
                .append("(")
                .append("id uuid PRIMARY KEY, ")
                .append("user_id uuid,")
                .append("point_of_interest_id uuid,")
                .append("entry_time timestamp,")
                .append("exit_time timestamp);");
        session.execute(sb.toString());
    }

    public void insertTrace(TraceData traceData) {
        StringBuilder sb = new StringBuilder("INSERT INTO ")
                .append(TABLE_NAME)
                .append("(id, user_id, point_of_interest_id, entry_time, exit_time) ")
                .append("VALUES (")
                .append(traceData.getId())
                .append(", '")
                .append(traceData.getUserId())
                .append("', '")
                .append(traceData.getPointOfInterestId())
                .append("', '")
                .append(traceData.getEntryTime())
                .append("', '")
                .append(traceData.getExitTime())
                .append("');");
        session.execute(sb.toString());
    }

    public void deleteTraces(List<UUID> ids) {
        StringBuilder tempIds = new StringBuilder("");

        for (int i = 0; i < ids.size() - 1; i++) {
            tempIds.append("'");
            tempIds.append(ids.get(i).toString());
            tempIds.append("'");
            tempIds.append(',');
        }
        tempIds.append("'");
        tempIds.append(ids.get(ids.size() - 1));
        tempIds.append("'");

        StringBuilder sb = new StringBuilder("DELETE FROM ")
                .append(TABLE_NAME)
                .append(" WHERE id IN (")
                .append(tempIds)
                .append(")';");

        final String query = sb.toString();
        session.execute(query);
    }

    public List<TraceData> getTraces(long offset, long limit) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ")
                .append(TABLE_NAME);

        final String query = sb.toString();
        ResultSet rs = session.execute(query);

        List<Row> rows = Stream.iterate(rs.one(), Objects::nonNull, row -> rs.one())
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());

        List<TraceData> traceDataList = new ArrayList<>();

        for (Row r : rows) {
            TraceData traceData = TraceData.builder()
                    .id(r.getUUID("id"))
                    .userId(r.getUUID("user_id"))
                    .pointOfInterestId(r.getUUID("point_of_interest_id"))
                    .entryTime(r.getTimestamp("entry_time").toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime())
                    .exitTime(r.getTimestamp("exit_time").toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime())
                    .build();
            traceDataList.add(traceData);
        }
        return traceDataList;
    }
}
