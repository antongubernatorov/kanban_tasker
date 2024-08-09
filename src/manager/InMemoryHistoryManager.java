package manager;

import classes.CustomLinkedList;
import taskTypes.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    CustomLinkedList tasksList = new CustomLinkedList();
    @Override
    public void add(Task task) {
        tasksList.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return tasksList.getWatchedTasks();
    }

    @Override
    public void remove(int id) {
        CustomLinkedList.Node node = tasksList.getNode(id);
        tasksList.removeNode(node);
    }
}
