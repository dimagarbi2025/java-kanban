package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void testTaskEqualityById() {
        Task task1 = new Task("Task 1", "Описание 1", 1);
        Task task2 = new Task("Task 2", "Описание 2", 1);

        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
        assertEquals(task1.hashCode(), task2.hashCode(), "HashCode должен быть одинаковым для одинаковых id");
    }

    @Test
    void testTaskInequalityDifferentId() {
        Task task1 = new Task("Task 1", "Описание 1", 1);
        Task task2 = new Task("Task 1", "Описание 1", 2);

        assertNotEquals(task1, task2, "Задачи с разным id не должны быть равны");
    }

    @Test
    void testEpicEqualityById() {
        Epic epic1 = new Epic("Epic 1", "Описание 1", 1);
        Epic epic2 = new Epic("Epic 2", "Описание 2", 1);

        assertEquals(epic1, epic2, "Эпики с одинаковым id должны быть равны");
        assertEquals(epic1.hashCode(), epic2.hashCode(), "HashCode должен быть одинаковым для одинаковых id");
    }

    @Test
    void testSubtaskEqualityById() {
        Subtask subtask1 = new Subtask("Subtask 1", "Описание 1", 1, 10);
        Subtask subtask2 = new Subtask("Subtask 2", "Описание 2", 1, 20);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым id должны быть равны");
        assertEquals(subtask1.hashCode(), subtask2.hashCode(), "HashCode должен быть одинаковым для одинаковых id");
    }

    @Test
    void testTaskImmutabilityWhenAdded() {
        Task originalTask = new Task("Переезд", "Переехать из Питера в Москву", 0);
        originalTask.setStatus(TaskStatus.IN_PROGRESS);

        manager.Managers.getDefault().createTask(originalTask);

        originalTask.setName("Упаковать вещи");
        originalTask.setDescription("Упаковать вещи");
        originalTask.setStatus(TaskStatus.DONE);

        Task fromManager = manager.Managers.getDefault().getTaskById(originalTask.getId());

        assertNotEquals(originalTask.getName(), fromManager.getName(), "Имя задачи не должно измениться в менеджере");
        assertNotEquals(originalTask.getDescription(), fromManager.getDescription(), "Описание не должно измениться в менеджере");
        assertNotEquals(originalTask.getStatus(), fromManager.getStatus(), "Статус не должен измениться в менеджере");
    }

}