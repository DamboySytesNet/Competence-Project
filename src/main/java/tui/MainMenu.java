package tui;

import tui.example.FooMenu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



public class MainMenu implements Menu {
    private final FooMenu exampleSubmenu;

    public MainMenu() {
        this.exampleSubmenu = new FooMenu(this);
    }

    @Override
    public void execute() {
        System.out.println("[1] - Generate and save data");
        System.out.println("[2] - Statistics");
        System.out.println("[3] - Ranking");
        System.out.println("[4] - Clustering");
        System.out.println("[5] - Neural network");
        System.out.println("[6] - FooExample");
        System.out.println("[9] - Options");
        System.out.println("[0] - Exit");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();


        switch (Choose.getChoose(input)) {
            case generateAndSave:
            case statistics:
            case ranking:
            case clustering:
            case neuralNetwork:
            case options:
                exampleSubmenu.execute();
            case exit:
                System.exit(0);
            case mainMenu:
                execute();
        }
    }

    enum Choose {
        generateAndSave("1"),
        statistics("2"),
        ranking("3"),
        clustering("4"),
        neuralNetwork("5"),
        options("9"),
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
