package tests;

import exceptions.IntersectionException;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTypes.Epic;
import taskTypes.Subtask;
import taskTypes.Task;
import taskTypes.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    public T taskManager;
    private Task task;
    private Epic epic;
    private Epic epic2;

    public abstract TaskManager createTaskManager();

    @BeforeEach
    public void updateTaskManager(){
        taskManager = (T) createTaskManager();
        task = new Task("TASK 1", "DESCR1", TaskStatus.NEW, LocalDateTime.of(2022, 7, 10, 10, 0), 30);
        epic = new Epic("EPIC1", "DESCR1", null, 0);
        epic2 = new Epic("EPIC2", "DESCR2", null, 0);
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
        assertNotNull(nullPointerException.getMessage());
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
        assertNotNull(nullPointerException.getMessage());
    }

    @Test
    void returnEmptyListIfNoEpicsTest(){
        taskManager.deleteAllTaskTypes();
        final List<Epic> epics = taskManager.getAllEpics();
        assertTrue(epics.isEmpty(), "Неправильный размер списка");
    }

    @Test
    void createNewSubtaskTest() {
        taskManager.deleteAllTaskTypes();
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "Descr1",
                LocalDateTime.of(2022, 7, 20, 10, 20), 30, epic.getId());
        taskManager.createNewSubtask(subtask);
        final Subtask savedSubtask = taskManager.getAllSubtasks().get(0);
        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final List<Integer> subtasks = epic.getSubtaskIds();
        assertNotNull(subtasks, "Подзадачи по конкретному эпику не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач в эпике.");
        assertEquals(subtask.getId(), subtasks.get(0), "Подзадачи не совпадают.");

        Optional<Task> savedInPriorities = taskManager.getPrioritizedTasks().stream().findFirst();
        savedInPriorities.ifPresent(value -> assertEquals(subtask, value, "Задача не найдена."));
    }

    @Test
    void printSubtaskListTest(){
        taskManager.deleteAllTaskTypes();
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "Descr1",
                LocalDateTime.of(2022, 7, 20, 10, 20), 30, epic.getId());
        taskManager.createNewSubtask(subtask);
        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются");
        assertEquals(1, subtasks.size(), "Неверное количество задач");
        assertEquals(subtask, subtasks.getFirst(), "Подзадачи не совпадают");
    }
    @Test
    void returnEmptySubtaskListWithNoSubtasksTest(){
        taskManager.deleteAllTaskTypes();
        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertTrue(subtasks.isEmpty(), "Список задач пустой");
    }
    @Test
    void throwNullPointerExceptionWithEmptySubtaskTest(){
        var nullPointerException = assertThrows(NullPointerException.class, () -> taskManager.createNewSubtask(null));
        assertNotNull(nullPointerException.getMessage());
    }
    @Test
    void throwNullPointerExceptionWithNoEpicTest(){
        var nullPointerException = assertThrows(NullPointerException.class, () -> {
            Subtask subtask = new Subtask("Subtask1", "Descr1",
                    LocalDateTime.of(2022, 7, 20, 10, 20), 30, 1);
            taskManager.createNewSubtask(subtask);
        });
        assertNotNull(nullPointerException.getMessage());
    }

    @Test
    void calcEpicStatusTest(){
        taskManager.deleteAllTaskTypes();
        Epic epic5 = new Epic("Epic1", "Descr1", null, 0);
        taskManager.createNewEpic(epic5);
        TaskStatus status = epic5.getStatus(); //пустой список подзадач
        assertEquals(TaskStatus.NEW, status, "Неверный статус при создании нового эпика.");

        Subtask subtask = new Subtask("Subtask1", "Descr1",
                LocalDateTime.of(2022, 7, 20, 10, 20), 30, epic5.getId());
        taskManager.createNewSubtask(subtask);
        status = epic5.getStatus(); //все подзадачи со статусом NEW
        assertEquals(TaskStatus.NEW, status, "Неверный статус при добавлении подзадачи NEW.");

        Subtask updatedTask = new Subtask(subtask.getId(),"New name",
                "New descr", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2022, 8, 5, 15, 0),
                30, epic5.getId());
        taskManager.updateSubtask(updatedTask);
        status = epic5.getStatus(); //все подзадачи со статусом IN PROGRESS
        assertEquals(TaskStatus.IN_PROGRESS, status, "Неверный статус при добавлении подзадачи  DONE.");

        Subtask updatedTask1 = new Subtask(subtask.getId(),"New name",
                "New descr", TaskStatus.DONE,
                LocalDateTime.of(2022, 8, 5, 16, 0),
                30, epic5.getId());
        taskManager.updateSubtask(updatedTask1);
        status = epic5.getStatus(); //подзадачи со статусами DONE
        assertEquals(TaskStatus.DONE, status, "Неверный статус при добавлении подзадач NEW и DONE.");

        Subtask subtask1 = new Subtask("Subtask1", "Descr1",
                LocalDateTime.of(2022, 7, 20, 10, 20),
                30, epic5.getId());
        taskManager.createNewSubtask(subtask1);
        status = epic5.getStatus(); //все подзадачи со статусом NEW - DONE
        assertEquals(TaskStatus.IN_PROGRESS, status, "Неверный статус при добавлении подзадачи IN PROGRESS.");
    }

    @Test
    void deleteAllTasksTest(){
        taskManager.deleteAllTaskTypes();
        taskManager.createNewTask(task);
        taskManager.getTaskById(task.getId());
        taskManager.deleteAllTasks();
        var allTasks = taskManager.getAllTasks();
        assertTrue(allTasks.isEmpty(), "Список задач не пустой");
    }

    @Test
    void deleteAllEpicsTest(){
        taskManager.deleteAllTaskTypes();
        taskManager.createNewEpic(epic);
        taskManager.getEpicById(epic.getId());
        taskManager.deleteAllEpics();
        var allEpics = taskManager.getAllEpics();
        assertTrue(allEpics.isEmpty(), "Список эпиков не пустой");
    }

    @Test
    void deleteAllSubtasksTest() {
        taskManager.deleteAllTaskTypes();
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "Descr1",
                LocalDateTime.of(2022, 7, 20, 10, 20), 30, epic.getId());
        taskManager.createNewSubtask(subtask);
        taskManager.getSubtaskById(subtask.getId());
        taskManager.deleteSubtaskById(subtask.getId());

        final List<Epic> epicList = taskManager.getAllEpics();
        assertEquals(1, epicList.size(), "При удалении всех подзадач удаляются эпики.");
        final List<Subtask> subtaskList = taskManager.getAllSubtasks();
        assertTrue(subtaskList.isEmpty(), "Список подзадач не пустой.");
        final List<Integer> subtasksEpic1 = epic.getSubtaskIds();
        assertTrue(subtasksEpic1.isEmpty(), "Список подзадач у эпик1 не пустой.");
        final List<Integer> subtasksEpic2 = epic2.getSubtaskIds();
        assertTrue(subtasksEpic2.isEmpty(), "Список подзадач у эпик2 не пустой.");
    }

    @Test
    void deleteAllTaskTypesTest(){
        taskManager.deleteAllTaskTypes();
        taskManager.createNewTask(task);
        taskManager.getTaskById(task.getId());
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "Descr1",
                LocalDateTime.of(2022, 7, 20, 10, 20), 30, epic.getId());
        taskManager.createNewSubtask(subtask);
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask.getId());
        taskManager.deleteAllTaskTypes();

        final var allTasks = taskManager.getAllTasks();
        assertTrue(allTasks.isEmpty(), "Список задач не пустой");
        final var allEpics = taskManager.getAllEpics();
        assertTrue(allEpics.isEmpty(), "Список эпиков не пустой");
        final var allSubtasks = taskManager.getAllSubtasks();
        assertTrue(allSubtasks.isEmpty(),"Список подзадач не пустой");
    }

    @Test
    void getTaskByIdTest(){
        taskManager.createNewTask(task);
        final var savedTask = taskManager.getTaskById(task.getId());
        assertEquals(savedTask, task, "Вернулась неверная задача по id");
    }

    //моя
    @Test
    void throwNullPointerExceptionWithWrongTaskId(){
        final var nullPointerException = assertThrows(NullPointerException.class, () -> {
            taskManager.createNewTask(task);
            taskManager.getTaskById(2);
        });
        assertNotNull(nullPointerException.getMessage());
    }

    @Test
    void throwNullPointerExceptionWithTaskListIsEmptyTest() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    int taskIdToFind = 1;
                    taskManager.getTaskById(taskIdToFind);
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void getEpicByIdTest(){
        taskManager.createNewEpic(epic);
        final var epicById = taskManager.getEpicById(epic.getId());
        assertEquals(epicById, epic, "Вернулся неправильный эпик по id");
    }

    @Test
    void throwNullPointerExceptionWrongEpicIdTest(){
        final var nullPointerException = assertThrows(NullPointerException.class, () -> {
            taskManager.createNewEpic(epic);
            taskManager.getEpicById(2);
        });
        assertNotNull(nullPointerException.getMessage());
    }

    @Test
    void throwNullPointerExceptionWithEpicListIsEmptyTest() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    int epicIdToFind = 3;
                    taskManager.getEpicById(epicIdToFind);
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void getSubtaskByIdTest(){
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "Descr1",
                LocalDateTime.of(2022, 7, 20, 10, 20), 30, epic.getId());
        taskManager.createNewSubtask(subtask);
        final var subtaskById = taskManager.getSubtaskById(subtask.getId());
        assertEquals(subtaskById, subtask, "Вернулась неправильная подзадача по id");
    }

    @Test
    void throwNullPointerExceptionWithWrongSubtaskIdTest() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    taskManager.createNewEpic(epic);
                    Subtask subtask = new Subtask("Subtask1", "Descr1", TaskStatus.NEW,
                            LocalDateTime.of(2022, 7, 20, 10, 20), 30, 1);
                    taskManager.createNewSubtask(subtask);
                    int subtaskIdToFind = 3;
                    taskManager.getSubtaskById(subtaskIdToFind);
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void throwNullPointerExceptionWithSubtaskListIsEmptyTest() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    int subtaskIdToFind = 3;
                    taskManager.getSubtaskById(subtaskIdToFind);
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void updateTaskTest(){
        taskManager.deleteAllTaskTypes();
        taskManager.createNewTask(task);
        final var id = task.getId();
        var updTask = new Task("New name", "New descr", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2022, 10, 5, 15, 0), 35);
        updTask.setId(id);
        taskManager.updateTask(updTask);
        final var taskById = taskManager.getTaskById(updTask.getId());
        assertEquals(taskById, updTask, "Задача не обновилась");
    }

    @Test
    void throwNullPointerExceptionWithUpdatingTaskWithWrongIdTest(){
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    taskManager.deleteAllTaskTypes();
                    taskManager.createNewTask(task);
                    Task updatedTask = new Task("New name", "New descr", TaskStatus.IN_PROGRESS,
                            LocalDateTime.of(2022, 9, 5, 15, 0), 35);
                    taskManager.updateTask(updatedTask);
                    int taskToUpdateId = 5;
                    Task foundTask = taskManager.getSubtaskById(taskToUpdateId);
                    assertEquals(updatedTask, foundTask, "Вернулась неверная задача по id.");
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void updateEpicTest() {
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "Descr1",
                LocalDateTime.of(2022, 7, 20, 10, 20), 30, epic.getId());
        taskManager.createNewSubtask(subtask);
        int epicToUpdateId = epic.getId();
        Epic updatedEpic = new Epic("New name", "New descr", null, 0);
        updatedEpic.setId(epicToUpdateId);
        taskManager.updateEpic(updatedEpic);
        Epic foundEpic = taskManager.getEpicById(epicToUpdateId);

        assertEquals(updatedEpic, foundEpic, "Эпик не обновился.");
    }

    @Test
    void throwNullPointerExceptionWithUpdatingEmptyEpicListTest() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    Epic updatedEpic = new Epic("New name", "New descr", null, 0);
                    taskManager.updateEpic(updatedEpic);
                    int epicToUpdateId = 5;
                    Task foundEpic = taskManager.getSubtaskById(epicToUpdateId);
                    assertEquals(updatedEpic, foundEpic, "Вернулась неверная задача по id.");
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void throwNullPointerExceptionWithUpdatingEpicWithWrongIdTest() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    taskManager.createNewEpic(epic);
                    Epic updatedEpic = new Epic("New name", "New descr", null, 0);
                    taskManager.updateEpic(updatedEpic);
                    int epicToUpdateId = 5;
                    Task foundEpic = taskManager.getSubtaskById(epicToUpdateId);
                    assertEquals(updatedEpic, foundEpic, "Вернулась неверная задача по id.");
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void updateSubtaskTest(){
        taskManager.deleteAllTaskTypes();
        Epic epic = new Epic("Epic", "epic", null, 0);
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "Descr1",
                LocalDateTime.of(2022, 7, 20, 10, 20), 30, epic.getId());
        taskManager.createNewSubtask(subtask);
        int id = subtask.getId();
        Subtask updatedSubtask = new Subtask("New name", "New descr", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2022, 7, 15, 10, 20), 30, epic.getId());
        updatedSubtask.setId(id);
        taskManager.updateSubtask(updatedSubtask);
        final var subtaskById = taskManager.getSubtaskById(id);
        assertEquals(updatedSubtask, subtaskById, "Подзадачи не совпадают");
        TaskStatus status = epic.getStatus();
        assertEquals(TaskStatus.IN_PROGRESS, status, "Статус эпика установлен неверно при обновлении задачи");
    }

    @Test
    void calcEpicStarAndDurationTimeTest(){
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "Descr1",
                LocalDateTime.of(2022, 7, 20, 10, 20), 30, epic.getId());
        taskManager.createNewSubtask(subtask);
        Subtask subtask2 = new Subtask("Subtask1", "Descr1",
                LocalDateTime.of(2022, 7, 15, 15, 0), 150, epic.getId());
        taskManager.createNewSubtask(subtask2);

        assertEquals(epic.getStartTime(), subtask2.getStartTime(), "Дата старта эпика рассчитана неверно");
        assertEquals(epic.getDuration(), (subtask.getDuration() + subtask2.getDuration()), "Продолжительность эпика рассчитана неверно.");
    }

    @Test
    void throwNullPointerExceptionWithUpdatingEmptySubtaskListTest(){
        final var nullPointerException = assertThrows(NullPointerException.class, () -> {
            Subtask updatedSubtask = new Subtask("New name", "New descr", TaskStatus.IN_PROGRESS,
                    LocalDateTime.of(2022, 7, 20, 10, 20), 30, 1);
            updatedSubtask.setId(1);
            taskManager.updateSubtask(updatedSubtask);
        });
        assertNotNull(nullPointerException.getMessage());
    }

    @Test
    void throwNullPointerExceptionWithUpdatingSubtaskWithWrongEpicIdTest() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    taskManager.createNewEpic(epic);
                    taskManager.createNewEpic(epic2);
                    Subtask subtask = new Subtask("Subtask1", "Descr1", TaskStatus.NEW,
                            LocalDateTime.of(2022, 7, 20, 10, 20), 30, 1);
                    taskManager.createNewSubtask(subtask);
                    Subtask updatedSubtask = new Subtask("New name", "New descr", TaskStatus.IN_PROGRESS,
                            LocalDateTime.of(2022, 8, 25, 12, 30), 40, 5);
                    updatedSubtask.setId(3);
                    taskManager.updateSubtask(updatedSubtask);
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void throwNullPointerExceptionWithUpdatingSubtaskOfAnotherEpicTest() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    taskManager.createNewEpic(epic);
                    taskManager.createNewEpic(epic2);
                    Subtask subtask = new Subtask("Subtask1", "Descr1", TaskStatus.NEW,
                            LocalDateTime.of(2022, 7, 20, 10, 20), 30, 1);
                    taskManager.createNewSubtask(subtask);
                    Subtask updatedSubtask = new Subtask("New name", "New descr", TaskStatus.IN_PROGRESS,
                            LocalDateTime.of(2022, 8, 25, 12, 30), 40, 2);
                    updatedSubtask.setId(3);
                    taskManager.updateSubtask(updatedSubtask);
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void deleteTaskByIdTest(){
        taskManager.deleteAllTaskTypes();
        taskManager.createNewTask(task);
        taskManager.getTaskById(task.getId());
        taskManager.deleteTaskById(task.getId());
        List<Task> tasksList = taskManager.getAllTasks();
        assertTrue(tasksList.isEmpty(), "Задача по id не удалена");
    }

    @Test
    void throwNullPointerExceptionWithDeleteTaskInEmptyTaskListTest() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    int taskToDeleteId = 1;
                    taskManager.deleteTaskById(taskToDeleteId);
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void throwNullPointerExceptionWithDeleteTaskWithWrongIdTest() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    taskManager.createNewTask(task);
                    int taskToDeleteId = 5;
                    taskManager.deleteTaskById(taskToDeleteId);
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void deleteSubtaskByIdTest(){
        taskManager.deleteAllTaskTypes();
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("New name", "New descr",
                LocalDateTime.of(2022, 8, 25, 12, 30), 40, epic.getId());
        taskManager.createNewSubtask(subtask);
        assertEquals(TaskStatus.NEW, epic.getStatus());
        taskManager.getSubtaskById(subtask.getId());
        taskManager.deleteSubtaskById(subtask.getId());
        TaskStatus newStatus = epic.getStatus();
        List<Subtask> subtaskList = taskManager.getAllSubtasks();
        assertTrue(subtaskList.isEmpty(), "Подзадача по id не удалена.");
        assertEquals(TaskStatus.NEW, newStatus, "Неверное обновление статуса при удалении подзадачи.");
    }

    @Test
    void throwNullPointerExceptionWithDeleteSubtaskFromEmptySubtaskListTest() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    int subtaskToDeleteId = 1;
                    taskManager.deleteSubtaskById(subtaskToDeleteId);
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void throwNullPointerExceptionWithDeleteSubtaskWithWrongIdTest() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    taskManager.createNewEpic(epic);
                    Subtask subtask = new Subtask("New name", "New descr",
                            LocalDateTime.of(2022, 8, 25, 12, 30),
                            40, 1);
                    taskManager.createNewSubtask(subtask);
                    int subtaskToDeleteId = 5;
                    taskManager.deleteSubtaskById(subtaskToDeleteId);
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void deleteEpicByIdTest() {
        taskManager.deleteAllTaskTypes();
        taskManager.createNewEpic(epic);
        taskManager.getEpicById(epic.getId());
        taskManager.deleteEpicById(epic.getId());
        List<Epic> epicList = taskManager.getAllEpics();
        assertTrue(epicList.isEmpty(), "Эпик по id не удален.");
    }

    @Test
    void throwNullPointerExceptionWithDeleteEpicFromEmptyEpicListTest() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    int epicToDeleteId = 1;
                    taskManager.deleteEpicById(epicToDeleteId);
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void throwNullPointerExceptionWithDeleteEpicWithWrongIdTest() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> {
                    taskManager.createNewEpic(epic);
                    int epicToDeleteId = 5;
                    taskManager.deleteEpicById(epicToDeleteId);
                });
        assertNotNull(exception.getMessage());
    }

    @Test
    void getProritizedTasksTest(){
        taskManager.deleteAllTaskTypes();
        taskManager.createNewTask(task);
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "Descr1",
                LocalDateTime.of(2022, 7, 20, 10, 20), 30, epic.getId());
        taskManager.createNewSubtask(subtask);
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        Optional<Task> firstTask = prioritizedTasks.stream().findFirst();

        firstTask.ifPresent(value -> assertEquals(value, task, "Неверная сортировка по приоритетам"));
        assertEquals(2, prioritizedTasks.size(), "Неверное количество задач");
    }

    @Test
    void throwIntersectionExceptionCreateTasksWithIntersectionTimeTest(){
        taskManager.deleteAllTaskTypes();
        final var intersectionException = assertThrows(IntersectionException.class, () -> {
            Task task1 = new Task("Task1", "Descr1", TaskStatus.NEW,
                    LocalDateTime.of(2022, 8, 10, 10, 0), 60);
            Task task2 = new Task("Task1", "Descr1", TaskStatus.NEW,
                    LocalDateTime.of(2022, 8, 10, 10, 30), 120);
            taskManager.createNewTask(task1);
            taskManager.createNewTask(task2);
        });
        assertEquals("Задача пересекается по времени.", intersectionException.getMessage());
        List<Task> tasks = taskManager.getAllTasks();
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(prioritizedTasks.size(), tasks.size(),
                "Разное количество задач в приоритезированном списке и списке задач.");
    }

    @Test
    void throwIntersectionExceptionUpdateTasksWithIntersectionTimeTest() {
        taskManager.deleteAllTaskTypes();
        final IntersectionException exception = assertThrows(IntersectionException.class,
                () -> {
                    Task task1 = new Task("Task1", "Descr1", TaskStatus.NEW,
                            LocalDateTime.of(2022, 7, 10, 10, 0), 60);
                    Task updatedTask1 = new Task(task1.getId(),"Task1", "Descr1", TaskStatus.DONE,
                            LocalDateTime.of(2022, 7, 10, 10, 30), 120);
                    taskManager.createNewTask(task1);
                    taskManager.updateTask(updatedTask1);
                });
        assertEquals("Задача пересекается по времени.", exception.getMessage());
        List<Task> tasks = taskManager.getAllTasks();
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(prioritizedTasks.size(), tasks.size(),
                "Разное количество задач в приоритезированном списке и списке задач.");
    }

    @Test
    void throwIntersectionExceptionCreateSubtasksWithSameTimeTest() {
        taskManager.deleteAllTaskTypes();
        final IntersectionException exception = assertThrows(IntersectionException.class,
                () -> {
                    taskManager.createNewEpic(epic);
                    Subtask subtask1 = new Subtask("Task1", "Descr1",
                            LocalDateTime.of(2022, 7, 10, 10, 0),
                            60, epic.getId());
                    Subtask subtask2 = new Subtask("Task1", "Descr1",
                            LocalDateTime.of(2022, 7, 10, 9, 30),
                            60, epic.getId());
                    taskManager.createNewSubtask(subtask1);
                    taskManager.createNewSubtask(subtask2);
                });
        assertEquals("Задача пересекается по времени.", exception.getMessage());
        ArrayList<Subtask> tasks = taskManager.getAllSubtasks();
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(prioritizedTasks.size(), tasks.size(),
                "Разное количество задач в приоритезированном списке и списке задач.");
    }

    @Test
    void throwIntersectionExceptionUpdateSubtasksWithSameTimeTest() {
        taskManager.deleteAllTaskTypes();
        final IntersectionException exception = assertThrows(IntersectionException.class,
                () -> {
                    taskManager.createNewEpic(epic);
                    Subtask subtask1 = new Subtask("Task1", "Descr1",
                            LocalDateTime.of(2022, 7, 10, 10, 0),
                            60, epic.getId());
                    Subtask updatedSubtask1 = new Subtask(subtask1.getId(),"Task1",
                            "Descr1", TaskStatus.DONE,
                            LocalDateTime.of(2022, 7, 10, 9, 30),
                            60, epic.getId());
                    taskManager.createNewSubtask(subtask1);
                    taskManager.updateSubtask(updatedSubtask1);
                });
        assertEquals("Задача пересекается по времени.", exception.getMessage());
        ArrayList<Subtask> tasks = taskManager.getAllSubtasks();
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(prioritizedTasks.size(), tasks.size(),
                "Разное количество задач в приоритезированном списке и списке задач.");
    }
}
