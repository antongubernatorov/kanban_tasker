package taskTypes;

import taskTypes.Subtask;
import taskTypes.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private final List<Integer> subtaskIds;

    public Epic(int id, String name, String description) {
        super(id, name, description);
        subtaskIds = new ArrayList<>();
    }

    public Epic(String name, String description) {
        super(name, description);
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
        return "Epic{" +
                "subtaskIds=" + subtaskIds +
                '}';
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
