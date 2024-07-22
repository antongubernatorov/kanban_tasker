package taskTypes;

import taskTypes.Subtask;
import taskTypes.Task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    List<Subtask> subtasks;

    public Epic(int id, String name, String description) {
        super(id, name, description);
        subtasks = new ArrayList<>();
    }

    public void showSubtasks(){
        System.out.println(this.subtasks);
    }

    public void addSubtask(String name, String description){
        Subtask subtask = new Subtask(id ,name, description);
        subtasks.add(subtask);
    }

    public boolean checkSubtasks(){
        boolean allIsDone = true;
        for(Subtask el: subtasks){
            if (el.status.equals("NEW") || el.status.equals("IN_PROGRESS")){
                allIsDone = false;
            }
        }
        return allIsDone;
    }

    @Override
    public String toString() {
        return "[" + "Название='" + name + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус='" + status + '\'' +
                ", Подзадачи=" + subtasks +
                ']';
    }
}
