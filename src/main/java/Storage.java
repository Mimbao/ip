import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

/**
 * Saves and loads the chatbot's tasks from a file on disk.
 */

public class Storage {
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
}
