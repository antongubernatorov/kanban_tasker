package manager;

import manager.TaskManager;

public class Managers {
    public TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
}
