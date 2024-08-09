package manager;

import taskTypes.Task;

import java.util.LinkedList;
import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();

    void remove(int id);
}
