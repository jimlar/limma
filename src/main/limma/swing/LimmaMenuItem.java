package limma.swing;

import java.util.ArrayList;
import java.util.List;

public class LimmaMenuItem {
    private String title;

    public LimmaMenuItem(String title) {
        this.title = title;
    }

    public void execute() {
    }

    public String toString() {
        return title;
    }

    public String getTitle() {
        return title;
    }
}
