package skynet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

// write add_task_increasesize by hand to get used to writing tests
// follow structure of @Tests -> Arrange -> Act -> Assert

public class TaskListTest {

    @Test
    void add_task_increasesSize() {
        // Arrange
        TaskList tasks = new TaskList();
        Task task = new Todo("test task");
        // Act
        tasks.add(task);
        // Assert
        assertEquals(1, tasks.size());
    }

    @Test
    void delete_existingTask_returnsDeletedTask() {
        // Arrange
        TaskList tasks = new TaskList();
        Task task = new Todo("test task");
        tasks.add(task);
        // Act
        Task deleted = tasks.delete(0);
        // Assert
        assertEquals(task, deleted);
        assertEquals(0, tasks.size());
    }

    @Test
    void get_existingIndex_returnsCorrectTask() {
        // Arrange
        TaskList tasks = new TaskList();
        Task task = new Todo("test task");
        tasks.add(task);
        // Act
        Task result = tasks.get(0);
        // Assert
        assertEquals(task, result);
    }

    @Test
    void find_matchingKeyword_returnsMatchingTask() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("go shopping"));

        List<Task> matches = tasks.find("book");

        assertEquals(1, matches.size());
        assertEquals("read book", matches.get(0).getDescription());
    }
}
