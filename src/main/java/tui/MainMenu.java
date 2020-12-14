package tui;

import tui.example.FooMenu;

public class MainMenu extends MenuAdapter {

    public MainMenu(String input) {
        super(input);
    }

    @Override
    public void atStart() {
        System.out.println("[1] - Generate and save data");
        System.out.println("[2] - Statistics");
        System.out.println("[3] - Ranking");
        System.out.println("[4] - Clustering");
        System.out.println("[5] - Neural network");
        System.out.println("[6] - FooExample");
        System.out.println("[9] - Options");
        System.out.println("[0] - Exit");
    }

    @Override
    public Menu chooseNext(String input) {
        switch (input) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "9":
                return new FooMenu();
            case "0":
            case "exit":
                return null;
            default:
                return this;
        }
    }
}
