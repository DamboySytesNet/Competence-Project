package tui.example;

import tui.MainMenu;
import tui.Menu;

public class FooMenu implements Menu {
    private static int stylePoints;

    @Override
    public void atStart() {
        System.out.println("Hello in my Foo world");
        System.out.println("[stream] - Use stream and be extra rewarded");
        System.out.println("\nWrite anything else to continue...");
    }

    @Override
    public Menu chooseNext(String input) {
        // some processing
        process();

        switch (input) {
            case "stream": {
                usedStream();
                return this;
            }
            case "exit": return null;
            default: return new MainMenu(null);
        }
    }

    public void usedStream() {
        System.out.println("Thank you for using stream!");
        System.out.println("Here is your style point");
        System.out.println("Current number of style points: " + ++stylePoints);
    }

    public void process() {
        System.out.println("Started processing");
        System.out.println("Processing.");
        System.out.println("Processing..");
        System.out.println("Processing...");
        System.out.println("Success!");
    }
}
