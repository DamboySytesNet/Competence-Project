import tui.MainMenu;
import tui.Menu;
import tui.MenuAdapter;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu menu = new MainMenu(null);
        String input;
        do {
            input = null;

            menu.atStart();
            if (menu instanceof MenuAdapter) {
                input = ((MenuAdapter) menu).getInput();
            }

            if (input == null) {
                input = scanner.nextLine();
            }
            menu = menu.chooseNext(input.toLowerCase());
            System.out.println("");
        } while (menu != null);
    }
}
