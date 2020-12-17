package tui.example;

import tui.Menu;

import java.util.Scanner;

public class FooMenu implements Menu {
    private Menu parent;
    private static int stylePoints;

    public FooMenu(Menu parent) {
        this.parent = parent;
    }

    @Override
    public void execute() {
        System.out.println("Hello in my Foo world");
        System.out.println("[stream] - Use stream and be extra rewarded");
        System.out.println("\nWrite anything else to continue...");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        switch (input) {
            case "stream": {
                usedStream();
                execute();
            }
            case "exit":
                System.exit(0);
            default:
                parent.execute();
        }
    }

    public void usedStream() {
        System.out.println("Thank you for using stream!");
        System.out.println("Here is your style point");
        System.out.println("Current number of style points: " + ++stylePoints);
    }
}
