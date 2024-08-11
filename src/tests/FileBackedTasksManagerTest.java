package tests;

import manager.FileBackedTasksManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTypes.Epic;
import taskTypes.Subtask;
import taskTypes.Task;
import taskTypes.TaskStatus;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{

    private final File file = new File("src/files", "SaveTasks.csv");
    private TaskManager fileBackendTasksManager;
    private TaskManager newFileBackendTasksManager;
    private Task task;
    private Epic epic;
    @Override
    public TaskManager createTaskManager() {
        return new FileBackedTasksManager();
    }

    @BeforeEach
    public void beforeEach() {
        fileBackendTasksManager = createTaskManager();
        task = new Task("Task1", "Descr1", TaskStatus.NEW,
                LocalDateTime.of(2022,12,10,10,0), 30);
        epic = new Epic("Epic1", "Descr1", null,0);
    }

    @Test
    public void saveAndReadFromFileTest(){
        fileBackendTasksManager.deleteAllTaskTypes();
        fileBackendTasksManager.createNewTask(task);
        fileBackendTasksManager.createNewEpic(epic);
        fileBackendTasksManager.getTaskById(task.getId());
        fileBackendTasksManager.getEpicById(epic.getId());

        newFileBackendTasksManager = (TaskManager) FileBackedTasksManager.loadFromFile(file);
        List <Task> taskList = newFileBackendTasksManager.getAllTasks();
        assertEquals(1, taskList.size(), "Из файла восстановленно неверное количество задач.");
        assertEquals(task, taskList.getFirst(), "Задача из файла восстановлена неверно");

        List<Epic> epicList = newFileBackendTasksManager.getAllEpics();
        assertEquals(1, epicList.size(), "Из файла восстановленно неверное количество эпиков.");
        assertEquals(epic, epicList.getFirst(), "Эпик из файла восстановлен неверно");

        final var history = newFileBackendTasksManager.getHistory();
        assertEquals(2, history.size(), "Неверное количество задач в истории.");
        assertEquals(task, history.getFirst(), "Задача в истории не совпадает");
    }

    @Test
    public void saveAndReadFromFileWithEmptyHistoryTest() {
        fileBackendTasksManager.deleteAllTaskTypes();
        fileBackendTasksManager.createNewTask(task);
        fileBackendTasksManager.getTaskById(task.getId());
        newFileBackendTasksManager = (TaskManager) FileBackedTasksManager.loadFromFile(file);
        List<Task> historyList = newFileBackendTasksManager.getHistory();
        assertEquals(1, historyList.size(), "Неверное количество задач в истории.");
    }

    @Test
    public void saveAndReadFromFileWithEpicNoSubtasksTest() {
        fileBackendTasksManager.deleteAllTaskTypes();
        fileBackendTasksManager.createNewEpic(epic);
        fileBackendTasksManager.getEpicById(epic.getId());
        newFileBackendTasksManager = (TaskManager) FileBackedTasksManager.loadFromFile(file);
        List<Epic> epicList = newFileBackendTasksManager.getAllEpics();
        assertEquals(1, epicList.size(), "Из файла восстановлено неверное количество эпиков'.");
        assertEquals(epic, epicList.getFirst(), "Эпик из файла восстановлен неверно.");
    }
}