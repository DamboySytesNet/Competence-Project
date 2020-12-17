import tui.MainMenu;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        MainMenu menu = new MainMenu(new Scanner(System.in));
        menu.execute();
    }
}
