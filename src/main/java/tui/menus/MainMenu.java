package tui.menus;

import tui.Menu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class MainMenu implements Menu {
    private final Scanner scanner;
    private final StatisticsMenu statisticsMenu;
    private final GenerationMenu generationMenu;
    private final ClusteringMenu clusteringMenu;
    private final RankingMenu rankingMenu;

    public MainMenu(Scanner scanner) {
        this.scanner = scanner;
        this.statisticsMenu = new StatisticsMenu(this, scanner);
        this.generationMenu = new GenerationMenu(this, scanner);
        this.clusteringMenu = new ClusteringMenu(this, scanner, this.generationMenu.getTraceRepository());
        this.rankingMenu = new RankingMenu(this, scanner);
    }

    @Override
    public void execute() {
        System.out.println("[1] - Generate and save data");
        System.out.println("[2] - Statistics");
        System.out.println("[3] - Ranking");
        System.out.println("[4] - Clustering");
        System.out.println("[0] - Exit");

        String input = scanner.nextLine();
        switch (Choose.getChoose(input)) {
            case generateAndSave:
                generationMenu.execute();
                break;
            case statistics:
                statisticsMenu.execute();
                break;
            case ranking:
                rankingMenu.execute();
                break;
            case clustering:
                clusteringMenu.execute();
                break;
            case exit:
                System.exit(0);
            case mainMenu:
                break;
        }

        execute();
    }

    enum Choose {
        generateAndSave("1"),
        statistics("2"),
        ranking("3"),
        clustering("4"),
        exit("0", "exit"),
        mainMenu; // default

        Choose(String... names) {
            this.names = names;
        }
        private final String[] names;
        private String[] getNames() {
            return names;
        }

        private static final Map<String, Choose> mapping = new HashMap<>();
        static {
            Arrays.stream(Choose.values())
                    .forEach(choose -> Arrays.stream(choose.getNames())
                            .forEach(name -> mapping.put(name, choose)));
        }

        static Choose getChoose(String input) {
            return mapping.getOrDefault(input, mainMenu);
        }
    }
}
