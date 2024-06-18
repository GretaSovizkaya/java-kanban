package test;

import basis.*;
import managers.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    public void tearDown() {
        tempFile.delete();
    }

    @Test
    public void testSaveAndLoadEmptyFile() {
        manager.save();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(loadedManager.getAllTypesTasks().isEmpty(), "Task list should be empty");
    }

    @Test
    public void testSaveMultipleTasks() {
        Task task1 = new Task("Task", "smth");
        Epic epic = new Epic("Epic", "smth");
        Subtask subtask = new Subtask("Subtask", "smth", epic);

        manager.createNewTask(task1);
        manager.createNewEpic(epic);
        manager.createNewSubtask(subtask);

        final int idTask = task1.getId();
        final int idSubtask = subtask.getId();
        final int idEpic = epic.getId();

        assertEquals(task1,manager.getTaskById(idTask));
        assertEquals(subtask,manager.getSubtaskById(idSubtask));
        assertEquals(epic,manager.getEpicById(idEpic));
    }

}