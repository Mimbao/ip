package skynet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles saving and loading of a TaskList using Java Streams.
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

        List<String> lines = tasks.stream()
                .map(this::convertTaskToLine)
                .collect(Collectors.toList());

        if (SAVE_FILE_PATH.getParent() != null) {
            Files.createDirectories(SAVE_FILE_PATH.getParent());
        }
        Files.write(SAVE_FILE_PATH, lines);
    }

    /**
     * Loads the previously saved target list from disk.
     *
     * @return a list of saved tasks, or an empty list when no save file exists
     * @throws IOException if the save file cannot be read
     */
    List<Task> load() throws IOException {
        if (Files.notExists(SAVE_FILE_PATH)) {
            return List.of();
        }

        try (var linesStream = Files.lines(SAVE_FILE_PATH)) {
            return linesStream
                    .map(this::convertLineToTask)
                    .collect(Collectors.toList());
        }
    }

    private String convertTaskToLine(Task task) {
        assert task != null : "Task should not be null during save";
        String status = task.isDone() ? "1" : "0";

        return switch (task) {
            case Todo todo -> "T" + DELIMITER + status + DELIMITER + todo.getDescription();
            case Deadline deadline -> "D" + DELIMITER + status + DELIMITER
                    + deadline.getDescription() + DELIMITER + deadline.getBy();
            case Event event -> "E" + DELIMITER + status + DELIMITER
                    + event.getDescription() + DELIMITER + event.getFrom()
                    + DELIMITER + event.getTo();
            default -> throw new IllegalArgumentException("Unknown target type");
        };
    }

    private Task convertLineToTask(String line) {
        assert line != null : "Line read from disk should not be null";

        String[] parts = line.split(DELIMITER_REGEX);
        assert parts.length >= 3 : "Formatted save line must have at least 3 fields";

        Task task = switch (parts[0]) {
            case "T" -> new Todo(parts[2]);
            case "D" -> new Deadline(parts[2], LocalDateTime.parse(parts[3]));
            case "E" -> new Event(parts[2], LocalDateTime.parse(parts[3]), LocalDateTime.parse(parts[4]));
            default -> throw new IllegalArgumentException("Unknown task type");
        };

        if (parts[1].equals("1")) {
            task.markAsDone();
        }

        return task;
    }
}
