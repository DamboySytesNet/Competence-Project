package tui.menus;

import connectors.CassandraConnector;
import generation.Experiment;
import generation.ExperimentSaver;
import lombok.Getter;
import repository.KeyspaceRepository;
import repository.POIRepository;
import repository.TraceDataRepository;
import repository.UserRepository;
import tui.Menu;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class GenerationMenu implements Menu {
    private final Scanner scanner;
    private final Menu mainMenu;
    private final UserRepository userRepository;
    private final POIRepository poiRepository;
    @Getter
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
        int numberOfPOI = this.getNumberInput("Type number of points of interest to generate: ");
        int numberOfTraces = this.getNumberInput("Type number of traces to generate: ");
        int timeStep = 5;

        System.out.println("Generating...");
        Experiment experiment = new Experiment(
                UUID.randomUUID(), numberOfUsers, numberOfPOI, numberOfTraces, timeStep);
        System.out.println("Done");
        System.out.println("Saving results...");
        try {
            ExperimentSaver.saveExperimentResults(experiment);
            System.out.println("Done");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
