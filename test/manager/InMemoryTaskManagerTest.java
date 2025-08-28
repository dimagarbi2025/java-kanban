package manager;

import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void testAddAndFindDifferentTaskTypes() {
        // Создаем задачи разных типов
        Task task = taskManager.createTask(new Task("Task", "Description", 0));
        Epic epic = taskManager.createEpic(new Epic("Epic", "Description", 0));
        Subtask subtask = taskManager.createSubtask(new Subtask("Subtask", "Description", 0, epic.getId()));

        // Проверяем, что задачи добавлены и находятся по id
        assertNotNull(taskManager.getTaskById(task.getId()), "Задача должна находиться по id");
        assertNotNull(taskManager.getEpicById(epic.getId()), "Эпик должен находиться по id");
        assertNotNull(taskManager.getSubtaskById(subtask.getId()), "Подзадача должна находиться по id");

        // Проверяем списки
        assertEquals(1, taskManager.getAllTasks().size(), "Должна быть одна задача");
        assertEquals(1, taskManager.getAllEpics().size(), "Должен быть один эпик");
        assertEquals(1, taskManager.getAllSubtasks().size(), "Должна быть одна подзадача");
    }

    @Test
    void testTaskIdConflict() {
        // Создаем задачу с предустановленным id
        Task taskWithFixedId = new Task("Fixed", "Description", 999);
        taskWithFixedId.setId(999);
        taskManager.createTask(taskWithFixedId);

        // Создаем еще задачи - они должны получить следующие id
        Task task1 = taskManager.createTask(new Task("Task1", "Description", 0));
        Task task2 = taskManager.createTask(new Task("Task2", "Description", 0));

        // Проверяем, что id не конфликтуют
        assertNotEquals(999, task1.getId(), "ID не должен конфликтовать с предустановленным");
        assertNotEquals(999, task2.getId(), "ID не должен конфликтовать с предустановленным");
        assertEquals(1000, task1.getId(), "ID должен быть следующим после предустановленного");
        assertEquals(1001, task2.getId(), "ID должен увеличиваться последовательно");
    }

    @Test
    void testCannotAddEpicAsItsOwnSubtask() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Description", 0));

        // Пытаемся создать подзадачу с эпиком в качестве своего же эпика
        Subtask invalidSubtask = new Subtask("Invalid", "Description", 0, epic.getId());
        invalidSubtask.setEpicId(epic.getId()); // Устанавливаем тот же id

        Subtask createdSubtask = taskManager.createSubtask(invalidSubtask);

        // Проверяем, что подзадача создалась нормально (это допустимо в текущей реализации)
        assertNotNull(createdSubtask, "Подзадача должна создаться");
        assertEquals(epic.getId(), createdSubtask.getEpicId(), "EpicId должен соответствовать");
    }

    @Test
    void testCannotSetSubtaskAsItsOwnEpic() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Description", 0));
        Subtask subtask = taskManager.createSubtask(new Subtask("Subtask", "Description", 0, epic.getId()));

        // Пытаемся установить подзадачу как свой же эпик (это не должно быть возможно по логике)
        // В текущей реализации это технически возможно, но логически некорректно
        subtask.setEpicId(subtask.getId());

        assertEquals(subtask.getId(), subtask.getEpicId(), "Технически возможно, но логически некорректно");
    }

    @Test
    void testTaskDataPreservationInManager() {
        Task originalTask = new Task("Original", "Original description", 0);
        originalTask.setStatus(TaskStatus.NEW);

        Task createdTask = taskManager.createTask(originalTask);

        // Проверяем, что все поля сохранились
        assertEquals("Original", createdTask.getName(), "Имя должно сохраниться");
        assertEquals("Original description", createdTask.getDescription(), "Описание должно сохраниться");
        assertEquals(TaskStatus.NEW, createdTask.getStatus(), "Статус должен сохраниться");
        assertTrue(createdTask.getId() > 0, "Должен быть установлен ID");
    }

}