package tui.menus;

import model.Trace;
import statistics.Statistics;
import tui.MainMenu;
import tui.Menu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StatisticsMenu implements Menu {
    private final Menu parent;
    private final Scanner scanner;
    private final Statistics statistics;

    public StatisticsMenu(Menu parent, Scanner scanner) {
        this.scanner = scanner;
        this.parent = parent;

        statistics = new Statistics();
    }

    public void addTrace(Trace t1, Trace t2) {
        statistics.addNewTrace(t1, t2);
    }

    @Override
    public void execute() {



        System.out.println("[1] - Get length of stay");
        System.out.println("[2] - Get longest route");
        System.out.println("[3] - Get most popular POI");
        System.out.println("[0] - Back");
        // Choose experiment

        // Get all traces
        // Sort traces
        // Add traces to statistics

        // List users
        // List POIs

        System.out.println("[1] - Get length of stay");
        System.out.println("[2] - Get longest route");
        System.out.println("[3] - Get most popular POI");
        System.out.println("[0] - Back");

        String userId;
        String poiId;

        String input = scanner.nextLine();
        switch (Choice.getChoice(input)) {
            case lengthOfStay:
                System.out.println("Give me user ID");
                userId = scanner.nextLine();
                System.out.println("Give me POI ID");
                poiId = scanner.nextLine();
                System.out.println(statistics.getLengthOfStayText(userId, poiId));
                 break;
            case longestRoute:
                System.out.println("Give me user ID");
                userId = scanner.nextLine();
                System.out.println(statistics.getLongestRouteText(userId));
                break;
            case mostPopularPOI:
                System.out.println("Give me POI ID");
                poiId = scanner.nextLine();
                System.out.println(statistics.getMostPopularPOIText(poiId));
                break;
            case exit:
                parent.execute();
        }

        parent.execute();
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
