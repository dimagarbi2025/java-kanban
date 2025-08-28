package manager;

import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {

    @Test
    void testTaskManagerAndHistoryManagerIntegration() {
        TaskManager taskManager = Managers.getDefault();

        // Создаем задачи
        Task task = taskManager.createTask(new Task("Task", "Description", 0));
        Epic epic = taskManager.createEpic(new Epic("Epic", "Description", 0));
        Subtask subtask = taskManager.createSubtask(new Subtask("Subtask", "Description", 0, epic.getId()));

        // Просматриваем задачи
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask.getId());

        // Проверяем историю
        var history = taskManager.getHistory();
        assertEquals(3, history.size(), "История должна содержать 3 просмотренные задачи");
        assertEquals(task.getId(), history.get(0).getId(), "Первая задача в истории");
        assertEquals(epic.getId(), history.get(1).getId(), "Вторая задача в истории");
        assertEquals(subtask.getId(), history.get(2).getId(), "Третья задача в истории");

        // Проверяем, что данные сохранились
        Task historyTask = history.get(0);
        assertEquals("Task", historyTask.getName(), "Имя задачи в истории");
        assertEquals("Description", historyTask.getDescription(), "Описание задачи в истории");
    }

    @Test
    void testTaskUpdateDoesNotAffectHistory() {
        TaskManager taskManager = Managers.getDefault();

        Task task = taskManager.createTask(new Task("Original", "Original desc", 0));
        taskManager.getTaskById(task.getId()); // Добавляем в историю

        // Обновляем задачу
        task.setName("Updated");
        task.setDescription("Updated desc");
        task.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task);

        // Проверяем, что история содержит оригинальную версию
        Task fromHistory = taskManager.getHistory().get(0);
        assertEquals("Original", fromHistory.getName(), "История должна содержать оригинальную версию");
        assertEquals("Original desc", fromHistory.getDescription(), "История должна содержать оригинальную версию");
        assertEquals(TaskStatus.NEW, fromHistory.getStatus(), "История должна содержать оригинальную версию");
    }
}
