package basis;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(int id, String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Subtask epic) {
        this.epic = epic.getEpic();
    }
}