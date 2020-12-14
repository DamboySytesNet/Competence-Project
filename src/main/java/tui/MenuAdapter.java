package tui;

/**
 * In comparison to menu can skip passing input thanks to predefined input
 */
public abstract class MenuAdapter implements Menu {
    private String input;

    /**
     * @param input predefined input, if null acts like menu
     */
    public MenuAdapter(String input) {
        this.input = input;
    }

    public String getInput() {return input;}
}
