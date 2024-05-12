package test;

import static org.junit.jupiter.api.Assertions.*;

import basis.Task;
import managers.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import managers.Managers;

import java.util.*;

class HistoryManagerTest { //добавила тесты
    //надеюсь я верно поняла:))
    @Test
    public void addNewHistoryManager() {
        assertNotNull(Managers.getDefaultHistory());
    }

    @Test
    public void addNewTaskManager() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    public void shouldRemoveTaskFromHistory() { // удаление задачи из истории
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Task", "Description");
        historyManager.add(task);
        historyManager.remove(task.getId());
        List<Task> tasks = historyManager.getTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testRemoveNonExistingTask() { // удаление несуществующей задачи
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Task", "Descpr");
        historyManager.add(task);
        historyManager.remove(666);
        List<Task> tasks = historyManager.getTasks();
        assertEquals(1, historyManager.getTasks().size());
    }

    @Test
    public void shouldNotAddDuplicateTasksToHistory() { //наличие дубликатов в истории
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Task", "Description");
        historyManager.add(task);
        historyManager.add(task); // Попытка добавить дубликат
        List<Task> tasks = historyManager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));
    }


}