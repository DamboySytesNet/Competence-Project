package tui;

import connectors.CassandraConnector;
import generation.Experiment;
import generation.ExperimentSaver;
import mappers.TraceDataMapper;
import model.POI;
import model.Trace;
import ranking.POIDynamicRanking;
import ranking.POIRanking;
import repository.KeyspaceRepository;
import repository.POIRepository;
import repository.TraceDataRepository;
import repository.UserRepository;

import java.sql.SQLException;
import java.util.*;

public class RankingMenu implements Menu{
    private final Scanner scanner;
    private final Menu mainMenu;
    private final TraceDataRepository traceRepository;

    private long numberofTraces;
    private List<Trace> traces;

    public RankingMenu(MainMenu mainMenu, Scanner scanner) {
        this.scanner = scanner;
        this.mainMenu = mainMenu;


        CassandraConnector connector = new CassandraConnector();
        connector.connect();


        this.traceRepository = new TraceDataRepository(connector.getSession());

        traceRepository.createTable();
    }

    public void createRanking(String title, HashMap<POI, Integer> rank){
        System.out.println("Done");
        System.out.println(title);
        System.out.println("----------------------------------------------------");

        for (POI poi : rank.keySet()) {
            String namespace= new String(new char[35 - poi.getName().length()]).replace('\0', ' ');
            String countspace= new String(new char[10 - rank.get(poi).toString().length()]).replace('\0', ' ');
            System.out.println("| " + poi.getName() + namespace + " | " + rank.get(poi) + countspace + " |");
        }
    }
    @Override
    public void execute() {
        System.out.println("[1] - Ranking based on number of visits in POI (Ascending)");
        System.out.println("[2] - Ranking based on number of visits in POI (Descending)");
        System.out.println("[3] - Ranking based on time spent in POI (Ascending)");
        System.out.println("[4] - Ranking based on time spent in POI (Descending)");
        System.out.println("[5] - Back to main menu");



        String input = scanner.nextLine();

        switch (RankingMenu.Choose.getChoose(input)) {
            case usersVisitsAscending:
                System.out.println("Please wait");
                numberofTraces = traceRepository.getTotalNumberOfTraces();
                traces = TraceDataMapper.mapTraceDataToTraces(traceRepository.getTraces(0, numberofTraces));
                createRanking("Ranking based on number of visits in POI (Ascending)",POIRanking.generateVisitorsCountRanking(traces, false));
                break;
            case usersVisitsDescending:
                System.out.println("Please wait");
                numberofTraces = traceRepository.getTotalNumberOfTraces();
                traces = TraceDataMapper.mapTraceDataToTraces(traceRepository.getTraces(0, numberofTraces));
                createRanking("Ranking based on number of visits in POI (Descending)",POIRanking.generateVisitorsCountRanking(traces, true));
                break;
            case timeSpentAscending:
                System.out.println("Please wait");
                numberofTraces = traceRepository.getTotalNumberOfTraces();
                traces = TraceDataMapper.mapTraceDataToTraces(traceRepository.getTraces(0, numberofTraces));
                createRanking("Ranking based on time spent in POI (Ascending)",POIRanking.generateTimeSpentRanking(traces, false));
                break;
            case timeSpentDescending:
                System.out.println("Please wait");
                numberofTraces = traceRepository.getTotalNumberOfTraces();
                traces = TraceDataMapper.mapTraceDataToTraces(traceRepository.getTraces(0, numberofTraces));
                createRanking("Ranking based on time spent in POI (Descending)",POIRanking.generateTimeSpentRanking(traces, true));
                break;
            case none:
                break;
        }
    }

    enum Choose {
        usersVisitsAscending("1"),
        usersVisitsDescending("2"),
        timeSpentAscending("3"),
        timeSpentDescending("4"),
        none;

        Choose(String... names) {
            this.names = names;
        }
        private final String[] names;
        private String[] getNames() {
            return names;
        }

        private static final Map<String, RankingMenu.Choose> mapping = new HashMap<>();
        static {
            Arrays.stream(RankingMenu.Choose.values())
                    .forEach(choose -> Arrays.stream(choose.getNames())
                            .forEach(name -> mapping.put(name, choose)));
        }

        static RankingMenu.Choose getChoose(String input) {
            return mapping.getOrDefault(input, none);
        }

    }
}
