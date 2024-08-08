package tests;

import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTypes.Epic;
import taskTypes.Subtask;
import taskTypes.Task;
import taskTypes.TaskStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        final Task savedTask = taskManager.getAllTasks().get(0);
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");
    }
}
