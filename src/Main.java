import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager manager = new TaskManager();

        // Создаем задачи
        Task task1 = manager.createTask(new Task("Покупки", "Купить продукты", 0));
        Task task2 = manager.createTask(new Task("Уборка", "Убраться в квартире", 0));

        // Создаем эпик с подзадачами
        Epic epic1 = manager.createEpic(new Epic("Переезд", "Организация переезда", 0));

        Subtask subtask1 = manager.createSubtask(new Subtask("Сбор коробок", "Собрать коробки для переезда", 0, epic1.getId()));
        Subtask subtask2 = manager.createSubtask(new Subtask("Упаковка вещей", "Упаковать вещи в коробки", 0, epic1.getId()));


        // Выводим все задачи
        System.out.println("Все задачи:");
        manager.getAllTasks().forEach(System.out::println);

        System.out.println("\nВсе эпики:");
        manager.getAllEpics().forEach(System.out::println);

        System.out.println("\nВсе подзадачи:");
        manager.getAllSubtasks().forEach(System.out::println);

        // Меняем статусы
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);

        System.out.println("\nЭпик после изменения статусов подзадач:");
        System.out.println(manager.getEpicById(epic1.getId()));
    }
}
