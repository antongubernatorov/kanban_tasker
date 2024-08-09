package tests;

import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import taskTypes.Epic;
import taskTypes.Subtask;
import taskTypes.Task;
import taskTypes.TaskStatus;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    public T taskManager;
    private Task task;
    private Epic epic;
    private Epic epic2;
    private Epic epic3;
    private Subtask subtask2;
    private Subtask subtask3;
    private Subtask subtask4;

    public abstract TaskManager createTaskManager();

    @BeforeEach
    public void updateTaskManager(){
        taskManager = (T) createTaskManager();
        task = new Task("TASK 1", "DESCR1", TaskStatus.NEW, LocalDateTime.of(2022, 7, 10, 10, 0), 30);
        epic = new Epic("EPIC1", "DESCR1", null, 0);
        epic2 = new Epic("EPIC2", "DESCR2", null, 0);
        subtask2 = new Subtask("SUBTASK2", "DESCR2", LocalDateTime.of(2022, 8, 20, 15,30), 120, epic.getId());
        subtask2 = new Subtask("SUBTASK3", "DESCR3", LocalDateTime.of(2022, 8, 20, 15,30), 15, epic.getId());
        epic3 = new Epic("EPIC3", "DESCR3", null, 0);
        subtask4= new Subtask("SUBTASK4", "DESCR4", LocalDateTime.of(2022, 8, 20, 15,30), 360, epic.getId());
    }

    @Test
    void createNewTaskTest(){
        taskManager.deleteAllTaskTypes();
        taskManager.createNewTask(task);
        final Task savedTask = taskManager.getAllTasks().getFirst();
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");
    }

    @Test
    void printTaskListTest(){
        taskManager.deleteAllTaskTypes();
        taskManager.createNewTask(task);
        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи не возращаются");
        assertEquals(1, tasks.size(), "Неправильный размер списка");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают");
    }

    @Test
    void returnNoTasksIfListIsEmptyTest(){
        taskManager.deleteAllTaskTypes();
        final List<Task> tasks = taskManager.getAllTasks();
        assertTrue(tasks.isEmpty(), "Список не пустой");
    }

    @Test
    void throwNullPointerExceptionWithEmptyListTest(){
        var nullPointerException = assertThrows(NullPointerException.class, () ->
                taskManager.createNewTask(null));
        assertNull(nullPointerException.getMessage());
    }

    @Test
    void createNewEpicTest(){
        taskManager.deleteAllTaskTypes();
        taskManager.createNewEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epic.getId());
        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Эпики не совпадают");
    }

    @Test
    void printEpicListTest(){
        taskManager.deleteAllTaskTypes();
        taskManager.createNewEpic(epic);
        final List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Список эпиков не возращается");
        assertEquals(1, epics.size(), "Неправильный размер списка");
        assertEquals(epic, epics.getFirst(), "Элементы не равны");
    }

    @Test
    void throwNullPointerExceptionIfEpicListIsEmptyTest(){
        var nullPointerException = assertThrows(NullPointerException.class, () -> taskManager.createNewEpic(null));
        assertNull(nullPointerException.getMessage());
    }

    @Test
    void returnEmptyListIfNoEpicsTest(){
        taskManager.deleteAllTaskTypes();
        final List<Epic> epics = taskManager.getAllEpics();
        assertTrue(epics.isEmpty(), "Неправильный размер списка");
    }
}
