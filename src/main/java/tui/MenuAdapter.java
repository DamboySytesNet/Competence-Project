package tui;

/**
 * In comparison to menu can have predefined input
 */
public abstract class MenuAdapter implements Menu {
    private String input;

    public MenuAdapter(String input) {
        this.input = input;
    }

    public String getInput() {return input;}
}
