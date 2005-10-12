package limma.swing;

public abstract class LimmaMenuItem {
    private String title;

    public LimmaMenuItem(String title) {
        this.title = title;
    }

    public abstract void execute();

    public String toString() {
        return title;
    }

    public String getTitle() {
        return title;
    }
}
