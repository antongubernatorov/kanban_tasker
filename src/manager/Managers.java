package manager;

import manager.TaskManager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static TaskManager getNewDefault(File file){
        return new FileBackendTasksManager(file);
    }

    public static TaskManager getDefault(File file){
        return (TaskManager) FileBackendTasksManager.loadFromFile(file);
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
