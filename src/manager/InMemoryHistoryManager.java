package manager;

import taskTypes.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> taskList = new LinkedList<>();
    @Override
    public void add(Task task) {
        taskList.add(task);
    }

    @Override
    public LinkedList<Task> getHistory() {
        return taskList;
    }

    @Override
    public void remove(int id) {
        taskList.remove(id);
    }
}
