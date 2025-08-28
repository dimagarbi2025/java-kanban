package manager;

import manager.HistoryManager;
import manager.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;


public class HistoryManagerTest {

        private HistoryManager historyManager;

        @BeforeEach
        void setUp() {
            historyManager = Managers.getDefaultHistory();
        }

        @Test
        void testAddTaskToHistory() {
            Task task = new Task("Task", "Description", 1);
            task.setStatus(TaskStatus.IN_PROGRESS);

            historyManager.addToHistory(task);
            List<Task> history = historyManager.getHistory();

            assertEquals(1, history.size(), "История должна содержать одну задачу");
            assertEquals(task, history.get(0), "Задача в истории должна быть равна оригинальной");
        }

        @Test
        void testHistoryPreservesTaskData() {
            Task task = new Task("Test Task", "Test Description", 123);
            task.setStatus(TaskStatus.DONE);

            historyManager.addToHistory(task);
            Task fromHistory = historyManager.getHistory().get(0);

            // Проверяем, что все данные сохранились
            assertEquals("Test Task", fromHistory.getName(), "Имя должно сохраниться");
            assertEquals("Test Description", fromHistory.getDescription(), "Описание должно сохраниться");
            assertEquals(123, fromHistory.getId(), "ID должно сохраниться");
            assertEquals(TaskStatus.DONE, fromHistory.getStatus(), "Статус должен сохраниться");
        }

        @Test
        void testHistoryLimit() {
            // Добавляем больше 10 задач
            for (int i = 1; i <= 15; i++) {
                Task task = new Task("Task " + i, "Description", i);
                historyManager.addToHistory(task);
            }

            List<Task> history = historyManager.getHistory();
            assertEquals(10, history.size(), "История должна быть ограничена 10 элементами");

            // Проверяем, что остались последние 10 задач
            assertEquals(6, history.get(0).getId(), "Первый элемент должен быть с ID 6");
            assertEquals(15, history.get(9).getId(), "Последний элемент должен быть с ID 15");
        }

        @Test
        void testDuplicateTaskMovesToEnd() {
            Task task1 = new Task("Task 1", "Description", 1);
            Task task2 = new Task("Task 2", "Description", 2);
            Task task3 = new Task("Task 3", "Description", 3);

            historyManager.addToHistory(task1);
            historyManager.addToHistory(task2);
            historyManager.addToHistory(task3);
            historyManager.addToHistory(task1); // Добавляем task1 снова

            List<Task> history = historyManager.getHistory();
            assertEquals(3, history.size(), "Дубликаты не должны создавать новые записи");
            assertEquals(task2, history.get(0), "Первый элемент должен быть task2");
            assertEquals(task3, history.get(1), "Второй элемент должен быть task3");
            assertEquals(task1, history.get(2), "Последний элемент должен быть task1 (перемещен в конец)");
        }

        @Test
        void testDifferentTaskTypesInHistory() {
            Task task = new Task("Task", "Description", 1);
            Epic epic = new Epic("Epic", "Description", 2);
            Subtask subtask = new Subtask("Subtask", "Description", 3, 2);

            historyManager.addToHistory(task);
            historyManager.addToHistory(epic);
            historyManager.addToHistory(subtask);

            List<Task> history = historyManager.getHistory();
            assertEquals(3, history.size(), "История должна содержать все типы задач");
            assertTrue(history.get(0) instanceof Task, "Первый элемент должен быть Task");
            assertTrue(history.get(1) instanceof Epic, "Второй элемент должен быть Epic");
            assertTrue(history.get(2) instanceof Subtask, "Третий элемент должен быть Subtask");
        }

        @Test
        void testNullTaskNotAddedToHistory() {
            historyManager.addToHistory(null);
            assertEquals(0, historyManager.getHistory().size(), "Null не должен добавляться в историю");
        }
}
