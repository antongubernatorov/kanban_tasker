package taskTypes;

import taskTypes.Task;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int id, String name, String description, TaskStatus status, LocalDateTime startTime, int duration, int epicId) {
        super(id, name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, LocalDateTime startTime, int duration, TaskStatus status, int epicId) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, LocalDateTime startTime, int duration, int epicId) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return super.getId() + "," +
                "Subtask" + "," +
                super.getName() + "," +
                super.getStatus() + "," +
                super.getDescription() + "," +
                super.getStartTime() + "," +
                super.getDuration() + "," +
                getEpicId();
    }
}
