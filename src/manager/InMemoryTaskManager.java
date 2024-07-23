package manager;

import manager.TaskManager;
import taskTypes.Epic;
import taskTypes.Subtask;
import taskTypes.Task;
import taskTypes.TaskStatus;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    public static int id = -1;
    public static final HashMap<Integer,Task> tasks = new HashMap<>();
    public static final HashMap<Integer,Epic> epics = new HashMap<>();
    public static final HashMap<Integer,Subtask> subtasks = new HashMap<>();
    public static int idGenerator = -1;

    //INITIALIZE
    public static final HistoryManager historyManager = null;
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int id) {
        ArrayList<Subtask> subTasks = new ArrayList<>();
        Epic epic = epics.get(id);
        for(int epicId : epic.getSubtaskIds()){
            subTasks.add(subtasks.get(epicId));
        }
        return subTasks;
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(tasks.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void createNewTask(Task task) {
        int id = generateNewId();
        task.setId(id);
        task.setStatus(TaskStatus.NEW);
        tasks.put(id, task);
    }

    @Override
    public void createNewEpic(Epic epic) {
        int id = generateNewId();
        epic.setId(id);
        epic.setStatus(TaskStatus.NEW);
        epics.put(id,epic);
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        Epic epic = getEpicById(subtask.getId());
        int id = generateNewId();
        subtask.setId(id);
        subtask.setStatus(TaskStatus.NEW);
        epic.addSubtaskId(id);
        subtasks.put(subtask.getId(), subtask);
        updateEpic(epic);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        ArrayList<String> statusOfSubtasks;
        statusOfSubtasks = getEpicSubtasksStatuses(epic.getId());
        if (statusOfSubtasks.contains("NEW")
                && !statusOfSubtasks.contains("IN_PROGRESS")
                && !statusOfSubtasks.contains("DONE")) {
            epic.setStatus(TaskStatus.NEW);
        } else if (subtasks.isEmpty()){
            epic.setStatus(TaskStatus.NEW);
        } else if (!statusOfSubtasks.contains("NEW")
                && !statusOfSubtasks.contains("IN_PROGRESS")
                && statusOfSubtasks.contains("DONE")){
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        int EpicId = subtask.getEpicId();
        Epic epic = epics.get(EpicId);
        updateEpic(epic);
    }

    @Override
    public void deleteTaskById(int id) {
        Task task = tasks.get(id);
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        for(int epicId : epic.getSubtaskIds()){
            subtasks.remove(epicId);
            historyManager.remove(epicId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        int epicId = subtask.getEpicId();
        if(subtask != null){
            Epic epic = epics.get(epicId);
            epic.getSubtaskIds().remove((Integer) id);
            subtasks.remove(id);
            historyManager.remove(id);
            updateEpic(epic);
        } else {
            System.out.println("Subtask не найден");
        }
    }

    @Override
    public void deleteAllTasks() {
        for(Integer id : tasks.keySet()){
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();
        for(Integer epicId : epics.keySet()){
            historyManager.remove(epicId);
        }
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for(Integer id : subtasks.keySet()){
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllTaskTypes() {
        deleteAllTasks();
        deleteAllSubtasks();
        deleteAllEpics();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void remove(int id) {
        historyManager.remove(id);
    }

    @Override
    public List<Subtask> getSubtaskByEpic(Epic epic) {
        return null;
    }

    public int getId(){
        int res = id;
        return ++res;
    }

    private int generateNewId(){
        return ++idGenerator;
    }

    private ArrayList<String> getEpicSubtasksStatuses(int epicId){
        ArrayList<String> statuses = new ArrayList<>();
        Epic epic = epics.get(epicId);
        for(int id : epic.getSubtaskIds()){
            statuses.add(String.valueOf(subtasks.get(id).getId()));
        }
        return statuses;
    }
}
