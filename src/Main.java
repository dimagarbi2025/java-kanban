import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager manager = Managers.getDefault();

        Task task1 = manager.createTask(new Task("Задача 1", "Описание задачи 1", 0));
        Task task2 = manager.createTask(new Task("Задача 2", "Описание задачи 2", 0));

        System.out.println("Находим задачу: " + manager.getTaskById(task1.getId()));
        System.out.println("Находим задачу: " + manager.getTaskById(task2.getId()));


        System.out.println("История просмотров :" + manager.getHistory());
    }
}
