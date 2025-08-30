package manager;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new ArrayList<>();
    private static final int MAX_HISTORY_SIZE = 10;

    @Override
    public void addToHistory(Task task) {
        if (task == null) {
            return;
        }

        if (history.size() == MAX_HISTORY_SIZE) {
            history.removeFirst();
        }

        history.addLast(task);
    }

    @Override
    public List<Task> getHistory(){
        return new ArrayList<>(history);

    }
}
