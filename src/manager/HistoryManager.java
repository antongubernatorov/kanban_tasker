package manager;

import taskTypes.Task;

import java.util.LinkedList;

public interface HistoryManager {
    void add(Task task);
    LinkedList<Task> getHistory();

    void remove(int id);
}
