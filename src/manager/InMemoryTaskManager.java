package manager;

import exceptions.IntersectionException;
import taskTypes.Epic;
import taskTypes.Subtask;
import taskTypes.Task;
import taskTypes.TaskStatus;


import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    public static int id = -1;
    public static final HashMap<Integer,Task> tasks = new HashMap<>();
    public static final HashMap<Integer,Epic> epics = new HashMap<>();
    public static final HashMap<Integer,Subtask> subtasks = new HashMap<>();
    public static int idGenerator = -1;
    public static Set<Task> prioritizedTask = new TreeSet<>();

    //INITIALIZE
    public static final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
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

    public List<Task> getPrioritizedTask() {
        return new ArrayList<>(prioritizedTask);
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void createNewTask(Task task){
        int id = generateNewId();
        task.setId(id);
        task.setStatus(TaskStatus.NEW);
        addNewPrioritizedTask(task);
        tasks.put(id, task);
    }

    @Override
    public void createNewEpic(Epic epic) {
        int id = generateNewId();
        epic.setId(id);
        epic.setStatus(TaskStatus.NEW);
        epics.put(id,epic);
        calcEpicStartAndFinish(epic);
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        Epic epic = getEpicById(subtask.getEpicId());
        int id = generateNewId();
        subtask.setId(id);
        subtask.setStatus(TaskStatus.NEW);
        epic.addSubtaskId(id);
        subtasks.put(subtask.getId(), subtask);
        updateEpic(epic);
        calcEpicStartAndFinish(epic);
    }

    @Override
    public void updateTask(Task task) {
        addNewPrioritizedTask(task);
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
        addNewPrioritizedTask(subtask);
        subtasks.put(subtask.getId(), subtask);
        int EpicId = subtask.getEpicId();
        Epic epic = epics.get(EpicId);
        updateEpic(epic);
        calcEpicStartAndFinish(epic);
    }

    private void calcEpicStart(Epic epic) {
        List<Subtask> epicSubtasks = getSubtaskByEpic(epic);
        if (!epicSubtasks.isEmpty()) {
            LocalDateTime startTime = null;
            for (Subtask subtask : epicSubtasks) {
                if (startTime == null) {
                    startTime = subtask.getStartTime();
                }else if (subtask.getStartTime().isBefore(startTime)){
                    startTime = subtask.getStartTime();
                }
            }
            epic.setStartTime(startTime);
        }
    }

    private void calcEpicDuration(Epic epic) {
        List<Subtask> epicSubtasks = getSubtaskByEpic(epic);
        if (!epicSubtasks.isEmpty()) {
            int duration = 0;
            for (Subtask subtask : epicSubtasks) {
                duration += subtask.getDuration();
            }
            epic.setDuration(duration);
        }
    }

    private void calcEpicFinish(Epic epic) {
        List<Subtask> epicSubtasks = getSubtaskByEpic(epic);
        if (epicSubtasks.size() > 0) {
            LocalDateTime endTime = null;
            for (Subtask subtask : epicSubtasks) {
                if (endTime == null) {
                    endTime = subtask.getEndTime();
                } else if (subtask.getEndTime().isAfter(endTime)) {
                    endTime = subtask.getEndTime();
                }
            }
            epic.setEndTime(endTime);
        }
    }

    private void calcEpicStartAndFinish(Epic epic){
        calcEpicStart(epic);
        calcEpicFinish(epic);
        calcEpicDuration(epic);
    }

    private void addNewPrioritizedTask(Task task){
        if(task == null){
            return;
        }
        validateTask(task);
        prioritizedTask.add(task);
    }

    private void validateTask(Task newTask){
        for (Task task : prioritizedTask){
            if (newTask.getStartTime().isBefore(task.getStartTime())
            && newTask.getEndTime().isBefore(task.getEndTime()) || (newTask.getStartTime().isAfter(task.getStartTime()) &&
                    newTask.getEndTime().isBefore(task.getEndTime()))
                    || (newTask.getStartTime().isBefore(task.getEndTime()) &&
                    newTask.getEndTime().isAfter(task.getEndTime()))){
                throw new IntersectionException("Задачи пересекаются по времени");
            }
        }
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
            calcEpicStartAndFinish(epic);
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
        prioritizedTask.clear();
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
        for(Epic epic : epics.values()){
            if(epic == null){
                throw new IntersectionException("Список эпиков и подзадач пуст");
            }
            epic.getSubtaskIds().clear();
            calcEpicDuration(epic);
        }
    }

    @Override
    public void deleteAllTaskTypes() {
        deleteAllTasks();
        deleteAllSubtasks();
        deleteAllEpics();
        prioritizedTask.clear();
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
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtaskList.add(subtasks.get(subtaskId));
        }
        return subtaskList;
    }

    @Override
    public int getEpicEndTime(int id) {
        var epic = epics.get(id);
        var subtaskIds = epic.getSubtaskIds();
        int duration = 0;
        for (Integer i : subtaskIds){
            duration += tasks.get(i).getDuration();
        }
        return duration;
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
