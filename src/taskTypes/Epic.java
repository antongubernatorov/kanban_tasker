package taskTypes;

import taskTypes.Subtask;
import taskTypes.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private final List<Integer> subtaskIds;

    public Epic(int id, String name, String description, LocalDateTime startTime, int duration) {
        super(id, name, description, startTime, duration);
        subtaskIds = new ArrayList<>();
    }

    public Epic(String name, String description, LocalDateTime startTime, int duration) {
        super(name, description, startTime, duration);
        subtaskIds = new ArrayList<>();
    }

    public List<Integer> getSubtaskIds(){
        return subtaskIds;
    }

    public void addSubtaskId(int id){
        subtaskIds.add(id);
    }

    public int getNumberOfSubtasks(){
        return subtaskIds.size();
    }

    @Override
    public String toString() {
        return super.getId() + "," +
                "Epic" + "," +
                super.getName() + "," +
                super.getStatus() + "," +
                super.getDescription() + "," +
                super.getStartTime() + "," +
                super.getDuration();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }
}
