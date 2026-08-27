package skynet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * Handles saving and loading of a TaskList.
 */
public class Storage {

    /**
     * Saves and loads the chatbot's tasks from a file on disk.
     *
     * @param tasks the list of tasks
     */
    void save(List<Task> tasks) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            String status = task.isDone() ? "1" : "0";

            switch (task) {
                case Todo todo -> lines.add("T | " + status + " | "
                        + todo.getDescription());
                case Deadline deadline -> lines.add("D | " + status + " | "
                        + deadline.getDescription() + " | " + deadline.getBy());
                case Event event -> lines.add("E | " + status + " | "
                        + event.getDescription() + " | " + event.getFrom()
                        + " | " + event.getTo());
                default -> throw new IllegalArgumentException("Unknown target type");
            }

        }
        Path saveFile = Path.of("data", "saveFile.txt");
        Files.createDirectories(saveFile.getParent());
        Files.write(saveFile, lines);
    }

    /**
     * Loads the previously saved target list from disk, does correct translation.
     *
     * @return a list of saved tasks, or an empty list when no save file exists
     * @throws IOException if the save file cannot be read
     */
    List<Task> load() throws IOException {
        Path saveFile = Path.of("data", "saveFile.txt");
        List<Task> tasks = new ArrayList<>();

        if (Files.notExists(saveFile)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(saveFile);
        for (String line : lines) {
            String[] parts = line.split(" \\| ");

            Task task = switch (parts[0]) {
                case "T" -> new Todo(parts[2]);
                case "D" -> new Deadline(
                                    parts[2],
                                    LocalDateTime.parse(parts[3]));
                case "E" -> new Event(
                                    parts[2],
                                    LocalDateTime.parse(parts[3]),
                                    LocalDateTime.parse(parts[4]));
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
