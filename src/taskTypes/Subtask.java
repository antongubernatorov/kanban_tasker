package taskTypes;

import taskTypes.Task;

public class Subtask extends Task {

    String epicName;

    public Subtask(int id, String name, String description) {
        super(id, name, description);
    }


    @Override
    public String toString() {
        return "Subtask{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", epicName='" + epicName + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
