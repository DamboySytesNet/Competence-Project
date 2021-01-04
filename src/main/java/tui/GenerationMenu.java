package tui;

import connectors.CassandraConnector;
import generation.Experiment;
import model.POI;
import model.Trace;
import model.TraceData;
import model.User;
import repository.KeyspaceRepository;
import repository.POIRepository;
import repository.TraceDataRepository;
import repository.UserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class GenerationMenu implements Menu{
    private final Scanner scanner;
    private final Menu mainMenu;
    private final UserRepository userRepository;
    private final POIRepository poiRepository;
    private final TraceDataRepository traceRepository;

    public GenerationMenu(MainMenu mainMenu, Scanner scanner) {
        this.scanner = scanner;
        this.mainMenu = mainMenu;
        this.userRepository = new UserRepository();
        this.poiRepository = new POIRepository();

        CassandraConnector connector = new CassandraConnector();
        connector.connect();

        String keyspaceName = "competence_project";

        KeyspaceRepository keyspaceRepository = new KeyspaceRepository(connector.getSession());
        keyspaceRepository.createKeyspace(
                keyspaceName,
                "SimpleStrategy",
                1);

        keyspaceRepository.useKeyspace(keyspaceName);
        this.traceRepository = new TraceDataRepository(connector.getSession());

        traceRepository.createTable();
    }


    private int getNumberInput(String prompt) {
        System.out.println(prompt);
        String input = this.scanner.nextLine();

        int number;
        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Given input is not a number. Try again.");
            number = getNumberInput(prompt);
        }
        return number;
    }

    @Override
    public void execute() {
        int numberOfUsers = this.getNumberInput("Type number of users to generate: ");
        int numberOfpois = this.getNumberInput("Type number of points of interest to generate: ");
        int numberOfTraces = this.getNumberInput("Type number of traces to generate: ");
        int timeStep = 5;

        System.out.println("Generating...");
        Experiment experiment = new Experiment("1", numberOfUsers, numberOfpois, numberOfTraces, timeStep);
        System.out.println("Done");
        List<User> users = experiment.getUsers();
        List<POI> pois = experiment.getPois();
        List<Trace> traces = experiment.getTraces();

        System.out.println("Saving users...");
        for (User user: users) {
            try {
                this.userRepository.save(user);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        System.out.println("Done");

        System.out.println("Saving points of interest...");
        for (POI poi: pois) {
            try {
                this.poiRepository.save(poi);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        System.out.println("Done");

        System.out.println("Saving traces...");
        for (Trace trace: traces) {
            TraceData traceData = TraceData.builder()
                    .id(UUID.randomUUID())
                    .userId(trace.getUser().getUserID())
                    .pointOfInterestId(trace.getPointOfInterest().getId())
                    .entryTime(trace.getEntryTime())
                    .exitTime(trace.getExitTime())
                    .build();
            this.traceRepository.insertTrace(traceData);
        }
        System.out.println("Done");

        this.mainMenu.execute();
    }
}
