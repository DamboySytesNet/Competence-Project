package tui.menus;

import connectors.CassandraConnector;
import mappers.TraceDataMapper;
import model.POI;
import model.Trace;
import model.TraceData;
import model.User;
import org.apache.commons.lang3.tuple.Pair;
import repository.ExperimentRepository;
import repository.POIRepository;
import repository.TraceDataRepository;
import repository.UserRepository;
import statistics.Statistics;
import tui.Menu;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

public class StatisticsMenu implements Menu {
    private final Menu parent;
    private final Scanner scanner;
    private final Statistics statistics;

    public StatisticsMenu(Menu parent, Scanner scanner) {
        this.scanner = scanner;
        this.parent = parent;

        statistics = new Statistics();
    }

    @Override
    public void execute() {
        // Get experiments
        List<Pair<UUID, Long>> experiments;
        try {
            experiments = ExperimentRepository.getAllIds();
        } catch (SQLException sqlException) {
            parent.execute();
            return;
        }

        System.out.println("Choose experiment: ");

        int i = 0;
        for (Pair<UUID, Long> experiment : experiments) {
            StringBuilder sb = new StringBuilder("[");
            sb.append(i);
            sb.append("] Experiment with ");
            sb.append(experiment.getValue());
            sb.append(" traces");

            i++;
        }

        int whichExperiment;
        do {
            whichExperiment = scanner.nextInt();
        } while (whichExperiment < 0 || whichExperiment >= i);

        UUID experimentUUID = experiments.get(whichExperiment).getKey();

        // Get all traces
        CassandraConnector connector = new CassandraConnector();
        connector.connect();
        TraceDataRepository traceDataRepository = new TraceDataRepository(connector.getSession());

        List<TraceData> traces = traceDataRepository.getTracesByExperimentId(experimentUUID);

        // Group traces by user
        Map<UUID, List<Trace>> usersTraces = traces.stream()
            .map(TraceDataMapper::mapTraceDataToTrace)
            .collect(Collectors.groupingBy(t -> t.getUser().getUserID()));

        // List users
        List<User> users = new LinkedList<>();
        try {
            users = UserRepository.getAll();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        // List POIs
        List<POI> pois;
        try {
            pois = POIRepository.getAll();
        } catch (SQLException sqlException) {
            parent.execute();
            return;
        }

        System.out.println("[1] - Get length of stay");
        System.out.println("[2] - Get longest route");
        System.out.println("[3] - Get most popular POI");
        System.out.println("[0] - Back");

        UUID userId;
        UUID poiId;
        List<Trace> userTraces;
        Trace oldTrace = null;

        String input = scanner.nextLine();
        switch (Choice.getChoice(input)) {
            case lengthOfStay:
                userId = chooseUser(users);
                poiId = choosePOI(pois);

                // Limit and sort traces to users
                userTraces = usersTraces.get(userId).stream()
                    .sorted(Comparator.comparing(Trace::getEntryTime))
                    .collect(Collectors.toList());

                // Add traces to statistics
                oldTrace = null;
                for (int j = 0; j < userTraces.size(); j++) {
                    statistics.addNewTrace(userTraces.get(j), oldTrace);
                    oldTrace = userTraces.get(j);
                }

                System.out.println(statistics.getLengthOfStayText(userId.toString(), poiId.toString()));
                break;
            case longestRoute:
                userId = chooseUser(users);

                // Limit traces to users
                userTraces = usersTraces.get(userId).stream()
                    .sorted(Comparator.comparing(Trace::getEntryTime))
                    .collect(Collectors.toList());
                ;


                // Add traces to statistics
                oldTrace = null;
                for (int j = 0; j < userTraces.size(); j++) {
                    statistics.addNewTrace(userTraces.get(j), oldTrace);
                    oldTrace = userTraces.get(j);
                }

                System.out.println(statistics.getLongestRouteText(userId.toString()));
                break;
            case mostPopularPOI:
                poiId = choosePOI(pois);

                // Add traces to statistics
                usersTraces.forEach((k, traceList) -> {
                    Trace previousTrace = null;

                    traceList.stream()
                        .sorted(Comparator.comparing(Trace::getEntryTime))
                        .collect(Collectors.toList());

                    for (Trace t : traceList) {
                        statistics.addNewTrace(t, previousTrace);
                        previousTrace = t;
                    }
                });

                System.out.println(statistics.getMostPopularPOIText(poiId.toString()));
                break;
            case exit:
                parent.execute();
                return;
        }

        parent.execute();
    }

    private UUID chooseUser(List<User> users) {
        System.out.println("Select user");
        int i = 0;
        for (User user : users) {
            System.out.println("[" + i + "] " + user.getUserID());
            i++;
        }

        int whichUser;
        do {
            whichUser = scanner.nextInt();
        } while (whichUser < 0 || whichUser >= i);

        return users.get(i).getUserID();
    }

    private UUID choosePOI(List<POI> POIs) {
        System.out.println("Select POI");
        int i = 0;
        for (POI poi : POIs) {
            System.out.println("[" + i + "] " + poi.getId());
            i++;
        }

        int whichPOI;
        do {
            whichPOI = scanner.nextInt();
        } while (whichPOI < 0 || whichPOI >= i);

        return POIs.get(i).getId();
    }

    enum Choice {
        lengthOfStay("1"),
        longestRoute("2"),
        mostPopularPOI("3"),
        exit; // default

        Choice(String... names) {
            this.names = names;
        }

        private final String[] names;

        private String[] getNames() {
            return names;
        }

        private static final Map<String, Choice> mapping = new HashMap<>();

        static {
            Arrays.stream(Choice.values())
                .forEach(choose -> Arrays.stream(choose.getNames())
                    .forEach(name -> mapping.put(name, choose)));
        }

        static Choice getChoice(String input) {
            return mapping.getOrDefault(input, exit);
        }
    }
}
