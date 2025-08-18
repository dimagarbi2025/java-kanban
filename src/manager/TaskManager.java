package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap();
    HashMap<Integer, Epic> epics = new HashMap();
    HashMap<Integer, Subtask> subtasks = new HashMap();
    private int nextId = 1;

    // Методы model.Task
    public List<Task> getAllTasks() {
        return new ArrayList(tasks.values());
    }
    public void deleteAllTasks(){
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Task createTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task;
    }

    public void updateTask(Task task){
        if(tasks.containsKey(task.getId())){
            tasks.put(task.getId(), task);
        }
    }

    public void deleteTaskById(int id){
        tasks.remove(id);
    }

    // Методы для model.Epic

    public List<Epic> getAllEpics(){
        return new ArrayList(epics.values());
    }

    public void deleteAllEpics(){
        epics.clear();
        subtasks.clear();
    }

    public Epic getEpicById(int id){
        return epics.get(id);
    }

    public Epic createEpic(Epic epic){
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public void updateEpic(Epic epic){
        if(epics.containsKey(epic.getId())){
            Epic savedEpic = epics.get(epic.getId());
            savedEpic.setName(epic.getName());
            savedEpic.setDescription(epic.getDescription());
            updateEpicStatus(savedEpic.getId());
        }
    }

    public void deleteEpicById(int id){
        Epic epic = epics.remove(id);
        if(epic != null){
            for(int subtaskId : epic.getSubtaskIds()){
                subtasks.remove(subtaskId);
            }
        }
    }

    public List<Subtask> getSubtasksByEpicId(int epicId){
        List<Subtask> result = new ArrayList();
        Epic epic = epics.get(epicId);
        if(epic != null){
            for(int subtaskId : epic.getSubtaskIds()){
                result.add(subtasks.get(subtaskId));
            }
        }
        return result;
    }

    // Методы для model.Subtask

    public List<Subtask> getAllSubtasks(){
        return new ArrayList(subtasks.values());
    }

    public void deleteAllSubtasks(){
        subtasks.clear();
        for(Epic epic : epics.values()){
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic.getId());
        }
    }

    public Subtask getSubtaskById(int id){
        return subtasks.get(id);
    }

    public Subtask createSubtask(Subtask subtask){
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if(epic != null){
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic.getId());
        }
        return subtask;
    }

    public void updateSubtask(Subtask subtask){
        if(subtasks.containsKey(subtask.getId())){
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    public void deleteSubtaskById(int id){
        Subtask subtask = subtasks.remove(id);
        if(subtask != null){
            Epic epic = epics.get(subtask.getEpicId());
            if(epic != null){
                epic.removeSubtaskId(subtask.getId());
                updateEpicStatus(epic.getId());
            }
        }
    }

    private void updateEpicStatus(int epicId){
        Epic epic = epics.get(epicId);
        if(epic == null || epic.getSubtaskIds().isEmpty()){
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean allDone = true;
        boolean allNew = true;

        for(int subtaskId : epic.getSubtaskIds()){

            Subtask subtask = subtasks.get(subtaskId);
            if(subtask == null){
                continue;
            }

            if(subtask.getStatus() != TaskStatus.DONE){
                allDone = false;
            }

            if(subtask.getStatus() != TaskStatus.NEW){
                allNew = false;
            }
        }

        if(allDone){
            epic.setStatus(TaskStatus.DONE);
        } else if(allNew){
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

}
