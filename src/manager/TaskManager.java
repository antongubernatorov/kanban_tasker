package manager;

import taskTypes.Epic;
import taskTypes.Task;

public interface TaskManager {
    public void createNewTask(String name, String description);

    public void showTasks();

    public void clearTasks();

    public void getTask(int id);

    public void updateTask(int id, String name, String description);
    public void updateTask(int id, int statusId);

    //работа с эпиками
    public void createEpic(String name, String description);

    public void showEpics();

    public void getEpic(int id);

    public void addSubtasks(int id, String name, String description);

    public void showSubtasks(int id);

    public void checkSubtasksStatus(int id);

}
