package skynet;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Main class for the Skynet application.
 */
public class Skynet {
    private final Storage storage;
    private TaskList tasks;
    private String commandType;
    private final Deque<List<Task>> stateHistory = new ArrayDeque<>();

    /**
     * Creates a Skynet instance and loads saved tasks.
     */
    public Skynet() {
        storage = new Storage();

        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            tasks = new TaskList();
        }
    }

    /**
     * Processes a user command and returns the appropriate response.
     *
     * @param command the command entered by the user
     * @return the response to display
     */
    public String getResponse(String command) {
        assert command != null : "Command passed to getResponse must not be null";
        try {
            // Extract the first word to determine the command type
            String commandWord = command.split(" ", 2)[0];

            return switch (commandWord) {
                case "bye" -> handleBye();
                case "list" -> handleList();
                case "find" -> handleFind(command);
                case "mark" -> handleMark(command);
                case "unmark" -> handleUnmark(command);
                case "todo" -> handleTodo(command);
                case "deadline" -> handleDeadline(command);
                case "event" -> handleEvent(command);
                case "delete" -> handleDelete(command);
                case "undo" -> handleUndo();
                default -> {
                    commandType = "OtherCommand";
                    throw new SkynetException("Hello, your command is unrecognized. "
                            + "Use todo/deadline/event/list/mark/unmark/delete/find/undo/bye.");
                }
            };
        } catch (SkynetException | IOException e) {
            return e.getMessage();
        }
    }

    private void saveStateSnapshot() {
        // Create a copy of the task list before modifying it
        // to track state and support undo by retrieving last state
        // use copy method instead of clone since protected
        List<Task> snapshot = tasks.getTasks().stream()
                .map(Task::copy) //
                .collect(Collectors.toList());
        stateHistory.push(snapshot);
    }

    /**
     * Returns the type of the most recently processed command.
     *
     * @return the command type
     */
    public String getCommandType() {
        return commandType;
    }

    private String handleBye() {
        commandType = "OtherCommand";
        return "The Future, now. Chat Terminated.";
    }

    private String handleList() {
        commandType = "OtherCommand";
        assert tasks != null : "TaskList cannot be null when building list response";

        StringBuilder response = new StringBuilder("[Target List Display]\n");
        for (int i = 0; i < tasks.size(); i++) {
            response.append(i + 1)
                    .append(". ")
                    .append(tasks.get(i))
                    .append("\n");
        }
        return response.toString().trim();
    }

    private String handleFind(String command) throws SkynetException {
        commandType = "OtherCommand";
        String keyword = Parser.parseFind(command);
        List<Task> matches = tasks.find(keyword);

        assert matches != null : "TaskList.find() should return a non-null list";

        if (matches.isEmpty()) {
            return "No matching tasks found.";
        }

        StringBuilder response = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matches.size(); i++) {
            response.append(i + 1)
                    .append(". ")
                    .append(matches.get(i))
                    .append("\n");
        }
        return response.toString().trim();
    }

    private String handleMark(String command) throws SkynetException, IOException {
        commandType = "MarkCommand";
        saveStateSnapshot();
        int taskIndex = Parser.getTaskIndex(command, "mark", tasks.size());
        assert taskIndex >= 0 && taskIndex < tasks.size() : "Task index must be in valid range";

        tasks.get(taskIndex).markAsDone();
        storage.save(tasks.getTasks());

        return "The Target has been Neutralized:\n  " + tasks.get(taskIndex);
    }

    private String handleUnmark(String command) throws SkynetException, IOException {
        commandType = "OtherCommand";
        saveStateSnapshot();
        int taskIndex = Parser.getTaskIndex(command, "unmark", tasks.size());
        assert taskIndex >= 0 && taskIndex < tasks.size() : "Task index must be in valid range";

        tasks.get(taskIndex).markAsNotDone();
        storage.save(tasks.getTasks());

        return "Failed to Complete:\n  " + tasks.get(taskIndex);
    }

    private String handleTodo(String command) throws SkynetException, IOException {
        commandType = "AddCommand";
        saveStateSnapshot();
        Task task = Parser.parseTodo(command);

        tasks.add(task);
        storage.save(tasks.getTasks());

        return "Target in time has been located:\n  " + task;
    }

    private String handleDeadline(String command) throws SkynetException, IOException {
        commandType = "AddCommand";
        saveStateSnapshot();
        Task task = Parser.parseDeadline(command);

        tasks.add(task);
        storage.save(tasks.getTasks());

        return "Incursion Risk, Finish Deadline:\n  " + task;
    }

    private String handleEvent(String command) throws SkynetException, IOException {
        commandType = "AddCommand";
        saveStateSnapshot();
        Task task = Parser.parseEvent(command);

        tasks.add(task);
        storage.save(tasks.getTasks());

        return "Temporal target located:\n  " + task;
    }

    private String handleDelete(String command) throws SkynetException, IOException {
        commandType = "DeleteCommand";
        saveStateSnapshot();
        int taskIndex = Parser.getTaskIndex(command, "delete", tasks.size());
        assert taskIndex >= 0 && taskIndex < tasks.size() : "Task index must be in valid range";

        Task deletedTask = tasks.delete(taskIndex);
        assert deletedTask != null : "Deleted task cannot be null";

        storage.save(tasks.getTasks());

        return "Target Erased:\n  " + deletedTask
                + "\nRemaining targets: " + tasks.size();
    }

    private String handleUndo() throws SkynetException, IOException {
        commandType = "OtherCommand";

        if (stateHistory.isEmpty()) {
            throw new SkynetException("No previous operations to undo.");
        }

        this.tasks = new TaskList(stateHistory.pop());
        storage.save(tasks.getTasks());

        return "Previous command has been undone successfully.";
    }

    /**
     * Runs the command-line version of Skynet.
     *
     * @param args command-line arguments
     */
    static void main(String... args) {
        Skynet skynet = new Skynet();
        Ui ui = new Ui();

        ui.showWelcome();

        while (true) {
            String command = ui.readCommand();
            String response = skynet.getResponse(command);
            ui.showError(response);

            if (command.equals("bye")) {
                break;
            }
        }
    }
}
