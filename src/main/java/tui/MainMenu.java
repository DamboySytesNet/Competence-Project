package tui;

import tui.example.FooMenu;

import java.util.Scanner;

public class MainMenu implements Menu {
    private FooMenu menu6;

    public MainMenu() {
        this.menu6 = new FooMenu(this);
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
        switch (input) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "9":
                menu6.execute();
            case "0":
            case "exit":
                System.exit(0);
            default:
                execute();
        }
    }
}
