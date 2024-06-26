package test;

import basis.*;
import managers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    //исходя из ТЗ и обязательных пунктов вроде как все здесь:))
    public static InMemoryTaskManager manager;
    private int id;

    @BeforeAll
    public static void setup() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void nonMatchingTasksId() { // проверка равенства тасков по айди
        Task task1 = new Task("Task", "Description");
        task1.setId(1);
        Task task2 = new Task("Task", "Description");
        task2.setId(1);

        Assertions.assertEquals(task1, task2, "ошибка");
    }

    @Test
    public void nonMatchingSubtasksId() { // + проверка на равенство сабтасков
        Epic epic = new Epic(1, "Epic", "Description");
        Subtask subtask1 = new Subtask(2, "Subtask", "Description", epic);
        subtask1.setId(2); // Задаем одинаковые id для подзадач
        Subtask subtask2 = new Subtask(2, "Subtask", "Description", epic);
        subtask2.setId(2);

        Assertions.assertEquals(subtask1, subtask2, "ошибка");
    }

    @Test
    public void nonMatchingEpicId() { // + проверка на равенство эпиков
        Epic epic = new Epic(1, "Epic1", "Hello Hello");
        epic.setId(1);

        Epic epic1 = new Epic(1, "Epic1", "Hello Hello");
        epic1.setId(1);

        assertEquals(epic, epic1, "Ошибка");
    }

    @Test
    public void shouldNotAllowSubtaskToBeItsOwnEpic() { // проверка, что объект Subtask нельзя сделать своим же эпиком
        // Создаем новую подзадачу с null эпиком
        Subtask subtask = new Subtask(1, "Subtask", "Description", null);

        // Пытаемся установить эпиком саму подзадачу
        subtask.setEpic(subtask);

        // Проверяем, что подзадача не равна своему эпику
        assertNotSame(subtask, subtask.getEpic());
    }

    /*насчет условия: "объект Epic нельзя добавить в самого себя в виде подзадачи;" ранее
     получали такой ответ: если сигнатура не позволят, то этот тест не обязателен. при попытке выполения теста
     компилятор сразу выдает ошибку. */

    @Test
    public void shouldAddDifferentTypesOfTasksToManager() {
        TaskManager manager = new InMemoryTaskManager();
        Task task = new Task("Task", "Description");
        Epic epic = new Epic(2, "Epic", "Description");
        Subtask subtask = new Subtask(1, "Subtask", "Description", epic);

        manager.createNewTask(task);
        manager.createNewTask(epic);
        manager.createNewSubtask(subtask);


        List<Task> tasks = manager.getAllTypesTasks();

        Assertions.assertTrue(tasks.contains(task));
        Assertions.assertTrue(tasks.contains(epic));
        Assertions.assertTrue(tasks.contains(subtask));
    }

    @Test
    public void shouldNotModifyTaskWhenAddingToManager() { // тест на создание неизменности задачи
        Task task = new Task("Task", "Description");
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createNewTask(task);
        Task retrievedTask = manager.getTaskById(task.getId()); // Получаем задачу по id, сразу после добавления

        // Проверяем, что полученная задача и добавленная задача идентичны
        Assertions.assertEquals(task, retrievedTask);
    }


    @Test
    public void shouldNotConflictGeneratedAndSpecifiedTaskIds() { //проверка на отсутствие конфликта между айди
        // Arrange
        Task task1 = new Task("Task 1 ", "Description");
        task1.setId(1);
        InMemoryTaskManager manager = new InMemoryTaskManager();

        manager.createNewTask(task1);

        Task retrievedTask = manager.getTaskById(task1.getId()); // Получаем задачу по заданному id
        // Проверяем, что полученная задача и добавленная задача идентичны
        Assertions.assertEquals(task1, retrievedTask);
    }

   @Test
    public void shouldSavePreviousVersionOfTaskInHistoryManager() { // сохранение предыдущих версий задачи

        Task task = new Task("Task", "Description");
        InMemoryTaskManager manager = new InMemoryTaskManager();

        manager.createNewTask(task);
        manager.getHistory().get(0).getId();

        assertEquals(Status.NEW, manager.getHistory().get(0).getStatus()); // Проверяем первую версию
        // Обновляем задачу
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);

        List<Task> history = manager.getHistory();
        assertEquals(2, history.size()); // Проверяем, что в истории две версии задачи
        assertEquals(Status.IN_PROGRESS, history.get(1).getStatus()); // Проверяем вторую версию
    }

}

