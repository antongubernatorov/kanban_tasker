package manager;

import manager.TaskManager;
import taskTypes.Epic;
import taskTypes.Subtask;
import taskTypes.Task;


import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    static int id = 0;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    //работа с тасками
    public void createNewTask(String name, String description){
        int taskId = ++id;
        Task task = new Task(taskId, name, description);
        tasks.put(taskId, task);
    }

    public void showTasks(){
        System.out.println(tasks);
    }

    public void clearTasks(){
        tasks.clear();
    }

    public void getTask(int id){
        Task task = tasks.get(id);
        System.out.println(task);
    }

    public void updateTask(int id, String name, String description){
        Task task = new Task(id, name, description);
        tasks.put(id, task);
    }
    public void updateTask(int id, int statusId){
        Task task = tasks.get(id);
        task.setStatus(statusId);
        tasks.put(id, task);
    }

    //работа с эпиками
    public void createEpic(String name, String description){
        Epic epic = new Epic(id, name, description);
        epics.put(id, epic);
    }

    public void showEpics(){
        if(epics.isEmpty()){
            System.out.println("Эпиков пока что нет");
        }
        System.out.println(epics);
    }

    public void getEpic(int id){
        Epic epic = epics.get(id);
        System.out.println(epic);
    }

    public void addSubtasks(int id, String name, String description){
        if(epics.containsKey(id)) {
            epics.get(id).addSubtask(name, description);
        } else {
            System.out.println("Такого эпика не существует");
        }
    }

    public void showSubtasks(int id){
        if(epics.containsKey(id)){
            epics.get(id).showSubtasks();
        } else {
            System.out.println("Такого эпика не существует");
        }
    }

    public void checkSubtasksStatus(int id){
        Epic epic = epics.get(id);
        boolean checkedSubtasks = epic.checkSubtasks();
        if(checkedSubtasks){
            epic.setStatus(2);
        }
    }

}
