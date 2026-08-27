import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SkyNET {
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage();

        // RELOAD TASKS (IF ANY)
        List<Task> tasks;
        try {
            tasks = storage.load();
        } catch (IOException e) {
            ui.showError("Unable to load saved tasks.");
            tasks = new ArrayList<>();
        }

        ui.showWelcome();

        while (true) {
            String command = ui.readCommand();
            try {
                if (command.equals("bye")) {
                    ui.showGoodbye();
                    break;

                } else if (command.equals("list")) {
                    ui.showTaskList(tasks);

                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = getTaskIndex(command, 4, tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    storage.save(tasks);
                    ui.showTask(
                            "The Target has been Neutralized:",
                            tasks.get(taskIndex));

                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = getTaskIndex(command, 6, tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    storage.save(tasks);
                    ui.showTask(
                            "Failed to Complete:",
                            tasks.get(taskIndex));

                } else if (command.equals("todo") || command.startsWith("todo "))  {
                    Task task = createTodo(command);
                    tasks.add(task);
                    storage.save(tasks);
                    ui.showTask(
                            "Target in time has been located:",
                            task);

                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    Task task = createDeadline(command);
                    tasks.add(task);
                    storage.save(tasks);
                    ui.showTask(
                            "Incursion Risk, Finish Deadline:",
                            task);

                } else if (command.equals("event") || command.startsWith("event ")) {
                    Task task = createEvent(command);
                    tasks.add(task);
                    storage.save(tasks);
                    ui.showTask(
                            "Temporal target located:",
                            task);


                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    int taskIndex = getTaskIndex(command, 6, tasks.size());
                    Task deletedTask = tasks.remove(taskIndex);
                    storage.save(tasks);
                    ui.showDeletedTask(deletedTask, tasks.size());

                } else {
                    throw new SkyNETException("Unrecognised Command. " +
                            "Use todo/deadline/event/list/mark/unmark/delete/bye.");
                }

            } catch (SkyNETException | IOException e) {
                ui.showError(e.getMessage());
            }
        }
    }



    private static Task createTodo(String command) throws SkyNETException {
        String description = command.substring(4).trim();

        if (description.isEmpty()) {
            throw new SkyNETException("Please input a target.");
        }

        return new Todo(description);
    }

    private static Task createDeadline(String command) throws SkyNETException {
        String details = command.substring(8).trim();
        int byIndex = details.indexOf(" /by ");

        if (byIndex == -1) {
            throw new SkyNETException(
                    "Please use the format: deadline DESCRIPTION /by yyyy-MM-dd HH:mm");
        }

        String description = details.substring(0, byIndex).trim();
        String byText = details.substring(byIndex + 5).trim();

        if (description.isEmpty() || byText.isEmpty()) {
            throw new SkyNETException(
                    "A deadline needs both a description and a deadline time.");
        }

        // once obtained by-text, convert to local date time
        try {
            LocalDateTime by = LocalDateTime.parse(
                    byText,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            return new Deadline(description, by);

        } catch (DateTimeParseException e) {
            throw new SkyNETException(
                    "Invalid date. Please use yyyy-MM-dd HH:mm.");

        }
    }

    private static Task createEvent(String command) throws SkyNETException {
        int fromIndex = command.indexOf(" /from ");
        int toIndex = command.indexOf(" /to ");

        if (fromIndex == -1 || toIndex == -1 || fromIndex > toIndex) {
            throw new SkyNETException(
                    "Please use the format: event DESCRIPTION /from yyyy-MM-dd HH:mm /to yyyy-MM-dd HH:mm");
        }

        String description = command.substring(6, fromIndex).trim();
        String fromText = command.substring(fromIndex + 7, toIndex).trim();
        String toText = command.substring(toIndex + 5).trim();

        if (description.isEmpty() || fromText.isEmpty() || toText.isEmpty()) {
            throw new SkyNETException(
                    "An event needs a description, start time, and end time.");
        }

        DateTimeFormatter inputFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try {
            LocalDateTime from = LocalDateTime.parse(fromText, inputFormatter);
            LocalDateTime to = LocalDateTime.parse(toText, inputFormatter);
            return new Event(description, from, to);

        } catch (DateTimeParseException e) {
            throw new SkyNETException(
                    "Invalid date. Please use yyyy-MM-dd HH:mm.");

        }
    }


    // Method to error handle invalid mark and unmark inputs/only accept numbers in range
    private static int getTaskIndex(String command, int commandLength, int taskCount)
            throws SkyNETException {
        String numberText = command.substring(commandLength).trim();

        try {
            int taskNumber = Integer.parseInt(numberText);

            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new SkyNETException("Target Number does not exist.");
            }

            return taskNumber - 1;
        } catch (NumberFormatException e) {
            throw new SkyNETException("Please input a Target Number.");
        }
    }
}

// All Errors Handled:
// 1. Using string for mark/unmark
// 2. Using invalid number for mark/unmark
// 3. Empty to do format
// 4. Wrong event format
// 5. Empty event format
// 6. Wrong deadline format
// 7. Empty deadline format
// 8. Prevent exceeding 100 targets >>> Removed in favor of scalable ArrayList
// 9. Incorrect date format