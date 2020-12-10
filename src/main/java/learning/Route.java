package learning;


import lombok.Getter;
import model.Trace;
import model.User;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class Route {
    private final User user;
    private final List<Trace> traces;

    public Route(User user, List<Trace> traces) {
        this.user = user;
        this.traces = sortTraces(traces);
    }

    private List<Trace> sortTraces(List<Trace> traces) {
        return traces.stream()
                .sorted(Comparator.comparing(Trace::getEntryTime))
                .collect(Collectors.toUnmodifiableList());
    }

    public List<List<Trace>> getGroupedTraces(int limit) {
        return traces.stream()
                .map(t -> {
                    int startIndex = traces.indexOf(t);
                    if (startIndex + limit <= traces.size())
                        return traces.subList(startIndex, startIndex + limit);
                    else
                        return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
