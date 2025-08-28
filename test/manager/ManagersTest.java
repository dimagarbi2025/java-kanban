package manager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test
    void testGetDefaultReturnsInitializedTaskManager() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager, "Менеджер не должен быть null");
        assertNotNull(manager.getAllTasks(), "Список задач должен быть инициализирован");
        assertNotNull(manager.getAllEpics(), "Список эпиков должен быть инициализирован");
        assertNotNull(manager.getAllSubtasks(), "Список подзадач должен быть инициализирован");
    }

    @Test
    void testGetDefaultHistoryReturnsInitializedHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Менеджер истории не должен быть null");
        assertNotNull(historyManager.getHistory(), "История должна быть инициализирована");
    }

    @Test
    void testManagersReturnDifferentInstances() {
        TaskManager manager1 = Managers.getDefault();
        TaskManager manager2 = Managers.getDefault();
        HistoryManager history1 = Managers.getDefaultHistory();
        HistoryManager history2 = Managers.getDefaultHistory();

        assertNotSame(manager1, manager2, "Должны возвращаться разные экземпляры TaskManager");
        assertNotSame(history1, history2, "Должны возвращаться разные экземпляры HistoryManager");
    }

}