package skynet;

import java.io.IOException;
import java.util.List;

/**
 * Main class for the Skynet application.
 */
public class Skynet {
    private final Storage storage;
    private TaskList tasks;
    private String commandType;

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
        try {
            if (command.equals("bye")) {
                commandType = "OtherCommand";
                return "The Future, now. Chat Terminated.";

            } else if (command.equals("list")) {
                commandType = "OtherCommand";
                return getTaskListResponse();

            } else if (command.equals("find") || command.startsWith("find ")) {
                commandType = "OtherCommand";

                String keyword = Parser.parseFind(command);
                List<Task> matches = tasks.find(keyword);

                if (matches.isEmpty()) {
                    return "No matching tasks found.";
                }

                StringBuilder response = new StringBuilder(
                        "Here are the matching tasks in your list:\n");

                for (int i = 0; i < matches.size(); i++) {
                    response.append(i + 1)
                            .append(". ")
                            .append(matches.get(i))
                            .append("\n");
                }

                return response.toString().trim();

            } else if (command.equals("mark") || command.startsWith("mark ")) {
                commandType = "MarkCommand";

                int taskIndex = Parser.getTaskIndex(command, 4, tasks.size());
                tasks.get(taskIndex).markAsDone();
                storage.save(tasks.getTasks());

                return "The Target has been Neutralized:\n  "
                        + tasks.get(taskIndex);

            } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                commandType = "OtherCommand";

                int taskIndex = Parser.getTaskIndex(command, 6, tasks.size());
                tasks.get(taskIndex).markAsNotDone();
                storage.save(tasks.getTasks());

                return "Failed to Complete:\n  "
                        + tasks.get(taskIndex);

            } else if (command.equals("todo") || command.startsWith("todo ")) {
                commandType = "AddCommand";

                Task task = Parser.parseTodo(command);
                tasks.add(task);
                storage.save(tasks.getTasks());

                return "Target in time has been located:\n  " + task;

            } else if (command.equals("deadline")
                    || command.startsWith("deadline ")) {
                commandType = "AddCommand";

                Task task = Parser.parseDeadline(command);
                tasks.add(task);
                storage.save(tasks.getTasks());

                return "Incursion Risk, Finish Deadline:\n  " + task;

            } else if (command.equals("event")
                    || command.startsWith("event ")) {
                commandType = "AddCommand";

                Task task = Parser.parseEvent(command);
                tasks.add(task);
                storage.save(tasks.getTasks());

                return "Temporal target located:\n  " + task;

            } else if (command.equals("delete")
                    || command.startsWith("delete ")) {
                commandType = "DeleteCommand";

                int taskIndex = Parser.getTaskIndex(command, 6, tasks.size());
                Task deletedTask = tasks.delete(taskIndex);
                storage.save(tasks.getTasks());

                return "Target Erased:\n  " + deletedTask
                        + "\nRemaining targets: " + tasks.size();

            } else {
                commandType = "OtherCommand";
                throw new SkynetException("Hello, your command is unrecognized. "
                        + "Use todo/deadline/event/list/mark/unmark/delete/find/bye.");
            }

        } catch (SkynetException | IOException e) {
            return e.getMessage();
        }
    }

    /**
     * Returns the type of the most recently processed command.
     *
     * @return the command type
     */
    public String getCommandType() {
        return commandType;
    }


    /**
     * Creates the response for the list command.
     *
     * @return the formatted task list
     */
    private String getTaskListResponse() {
        StringBuilder response = new StringBuilder("[Target List Display]\n");

        for (int i = 0; i < tasks.size(); i++) {
            response.append(i + 1)
                    .append(". ")
                    .append(tasks.get(i))
                    .append("\n");
        }

        return response.toString().trim();
    }

    /**
     * Runs the command-line version of Skynet.
     *
     * @param args command-line arguments
     */
    static void main(String[] args) {
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
