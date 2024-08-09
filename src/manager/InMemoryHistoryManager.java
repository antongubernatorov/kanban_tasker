package manager;

import classes.CustomLinkedList;
import taskTypes.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList tasksList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        tasksList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        tasksList.removeNode(tasksList.getNode(id));
    }

    @Override
    public List<Task> getHistory() {
        return tasksList.getWatchedTasks();
    }
}