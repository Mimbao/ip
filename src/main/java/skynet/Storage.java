package skynet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading of a TaskList.
 */
public class Storage {
    private static final Path SAVE_FILE_PATH = Path.of("data", "saveFile.txt");
    private static final String DELIMITER = " | ";
    private static final String DELIMITER_REGEX = " \\| ";

    /**
     * Saves the chatbot's tasks to a file on disk.
     *
     * @param tasks the list of tasks
     * @throws IOException if the save file cannot be written
     */
    void save(List<Task> tasks) throws IOException {
        assert tasks != null : "TaskList passed to save() must not be null";

        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            assert task != null : "Task list should not contain null entries";
            String status = task.isDone() ? "1" : "0";

            switch (task) {
                case Todo todo -> lines.add("T" + DELIMITER + status + DELIMITER
                        + todo.getDescription());
                case Deadline deadline -> lines.add("D" + DELIMITER + status + DELIMITER
                        + deadline.getDescription() + DELIMITER + deadline.getBy());
                case Event event -> lines.add("E" + DELIMITER + status + DELIMITER
                        + event.getDescription() + DELIMITER + event.getFrom()
                        + DELIMITER + event.getTo());
                default -> throw new IllegalArgumentException("Unknown target type");
            }
        }

        if (SAVE_FILE_PATH.getParent() != null) {
            Files.createDirectories(SAVE_FILE_PATH.getParent());
        }
        Files.write(SAVE_FILE_PATH, lines);
    }

    /**
     * Loads the previously saved target list from disk, does correct translation.
     *
     * @return a list of saved tasks, or an empty list when no save file exists
     * @throws IOException if the save file cannot be read
     */
    List<Task> load() throws IOException {
        List<Task> tasks = new ArrayList<>();

        if (Files.notExists(SAVE_FILE_PATH)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(SAVE_FILE_PATH);
        for (String line : lines) {
            assert line != null : "Save file line read should not be null";

            String[] parts = line.split(DELIMITER_REGEX);
            assert parts.length >= 3 : "Formatted save line must have at least 3 pipe-separated fields";

            Task task = switch (parts[0]) {
                case "T" -> new Todo(parts[2]);
                case "D" -> {
                    assert parts.length == 4 : "Deadline entry must have 4 fields";
                    yield new Deadline(
                            parts[2],
                            LocalDateTime.parse(parts[3]));
                }
                case "E" -> {
                    assert parts.length == 5 : "Event entry must have 5 fields";
                    yield new Event(
                            parts[2],
                            LocalDateTime.parse(parts[3]),
                            LocalDateTime.parse(parts[4]));
                }
                default -> throw new IllegalArgumentException("Unknown task type");
            };

            if (parts[1].equals("1")) {
                task.markAsDone();
            }

            tasks.add(task);
        }
        return tasks;
    }
}
