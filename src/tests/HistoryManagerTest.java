package tests;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import taskTypes.Task;
import taskTypes.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private static HistoryManager historyManager;
    private static InMemoryTaskManager inMemoryTaskManager;
    private Task task;
    @BeforeEach
    public void prepare(){
        historyManager = new InMemoryHistoryManager();
        inMemoryTaskManager = new InMemoryTaskManager();
        task = new Task("Task1", "Descr1", TaskStatus.NEW,
                LocalDateTime.of(2022,12,10,10,0), 30);
    }

    @Test
    @DisplayName("Add and Get test for HistoryManager")
    void addAndReturnHistoryListTest(){
        inMemoryTaskManager.deleteAllTaskTypes();
        inMemoryTaskManager.createNewTask(task);
        historyManager.add(task);
        final var history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertEquals(1, history.size(), "Неправильное количество задач в истории");
    }

    @Test
    void removeDuplicatedTasksFromHistoryListTest(){
        inMemoryTaskManager.deleteAllTaskTypes();
        inMemoryTaskManager.createNewTask(task);
        historyManager.add(task);
        historyManager.add(task);
        final var history = historyManager.getHistory();
        assertEquals(1, history.size(), "Повторная задача найдена");
    }

    @Test
    void removeTaskFromHistoryListByIdTes(){
        inMemoryTaskManager.deleteAllTaskTypes();
        inMemoryTaskManager.createNewTask(task);
        historyManager.add(task);
        historyManager.remove(task.getId());
        final var history = historyManager.getHistory();
        final var containsTask = history.contains(task);
        assertFalse(containsTask, "Задача не удалена");
    }

    @Test
    void removeTaskFromPositionFromHistoryListTest(){
        historyManager.getHistory().clear();
        Task task2 = new Task("Task1", "Descr1",TaskStatus.NEW,
                LocalDateTime.of(2022,9,6,12,15), 30);
        Task task3 = new Task("Task1", "Descr1",TaskStatus.NEW,
                LocalDateTime.of(2022,10,7,15,30), 600);
        Task task4 = new Task("Task1", "Descr1",TaskStatus.NEW,
                LocalDateTime.of(2022,11,8,22,25), 60);
        Task task5 = new Task("Task1", "Descr1",TaskStatus.NEW,
                LocalDateTime.of(2022,12,10,9,0), 15);
        inMemoryTaskManager.createNewTask(task);
        inMemoryTaskManager.createNewTask(task2);
        inMemoryTaskManager.createNewTask(task3);
        inMemoryTaskManager.createNewTask(task4);
        inMemoryTaskManager.createNewTask(task5);
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.remove(task.getId());
        Task newFirst = historyManager.getHistory().getFirst();
        assertEquals(task2, newFirst, "Неверное смещение при удалении первой задачи.");
        historyManager.remove(task5.getId());
        int lastIndex = historyManager.getHistory().size() - 1;
        Task newLast = historyManager.getHistory().get(lastIndex);
        assertEquals(task4, newLast, "Неверно определена последняя задача.");
        int index = historyManager.getHistory().indexOf(task3);
        Task preTask = historyManager.getHistory().get(index - 1);
        Task postTask = historyManager.getHistory().get(index + 1);
        historyManager.remove(task3.getId());
        assertEquals(preTask, task2, "Предшествующая задача восстановлена неверно.");
        assertEquals(postTask, task4, "Следующая задача восстановлена неверно.");
    }

    @Test
    void returnNullWhenHistoryIsEmptyTest(){
        final var size = historyManager.getHistory().size();
        assertEquals(0, size, "История не пустая");
    }
}