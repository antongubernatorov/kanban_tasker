package manager;

import manager.TaskManager;

public class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
}
