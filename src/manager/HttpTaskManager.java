package manager;

import com.google.gson.Gson;
import exceptions.ManagerSaveException;
import taskTypes.Epic;
import taskTypes.Subtask;
import taskTypes.Task;
import taskTypes.TaskKey;

import java.io.IOException;
import java.net.URL;

public class HttpTaskManager extends FileBackedTasksManager implements TaskManager {
    private final URL url;
    private final KVServerClient kvServerClient;
    Gson gson = new Gson();

    public HttpTaskManager(URL url){
        this.url = url;
        kvServerClient = new KVServerClient(this.url);
    }

    public static HttpTaskManager loadFromServer(URL url){
        HttpTaskManager httpTaskManager = new HttpTaskManager(url);
        httpTaskManager.readData();
        return httpTaskManager;
    }

    @Override
    public void save() throws ManagerSaveException {
        try {
            kvServerClient.put((String.valueOf(TaskKey.TASK_KEY)), gson.toJson(tasks.values()));
            kvServerClient.put((String.valueOf(TaskKey.SUBTASK_KEY)), gson.toJson(subtasks.values()));
            kvServerClient.put((String.valueOf(TaskKey.EPIC_KEY)), gson.toJson(epics.values()));
            kvServerClient.put((String.valueOf(TaskKey.HISTORY_KEY)), gson.toJson(getHistory()));
        } catch (IOException | InterruptedException | ManagerSaveException e) {
            System.out.println("Ошибка записи");
        }
    }

    private void readData() {
        String loadedTasks = kvServerClient.load(String.valueOf(TaskKey.TASK_KEY));
        if (loadedTasks.isEmpty()){
            return;
        }
        String[] separatedTasks = loadedTasks.split("//");
        for (String t : separatedTasks) {
            Task task = fromString(t);
            createNewTask(task);
        }
        String loadedEpics = kvServerClient.load(String.valueOf(TaskKey.EPIC_KEY));
        if (loadedEpics.isEmpty()){
            return;
        }
        String[] separatedEpics = loadedEpics.split("//");
        for (String e : separatedEpics){
            Epic epic = (Epic) fromString(e);
            createNewEpic(epic);
        }
        String loadedSubtasks = kvServerClient.load(String.valueOf(TaskKey.SUBTASK_KEY));
        if (loadedSubtasks.isEmpty()) {
            return;
        }
        String[] separatedSubtasks = loadedSubtasks.split("//");
        for (String s : separatedSubtasks) {
            Subtask subtask = (Subtask) fromString(s);
            createNewSubtask(subtask);
        }
        String loadedHistory = kvServerClient.load(String.valueOf(TaskKey.HISTORY_KEY));
        for (Integer id : historyFromString(loadedHistory)) {
            if (tasks.containsKey(id)) {
                Task task = tasks.get(id);
                historyManager.add(task);
            } if (epics.containsKey(id)) {
                Epic epic = epics.get(id);
                historyManager.add(epic);
            } if(subtasks.containsKey(id)) {
                Subtask subtask = subtasks.get(id);
                historyManager.add(subtask);
            }
        }
    }
}
