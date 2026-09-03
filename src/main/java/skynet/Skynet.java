package skynet;

import java.io.IOException;
import java.util.List;

/**
 * Main class for the Skynet application.
 */
public class Skynet {
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage();

        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            ui.showError("Unable to load saved tasks.");
            tasks = new TaskList();
        }

        ui.showWelcome();

        while (true) {
            String command = ui.readCommand();
            try {
                if (command.equals("bye")) {
                    ui.showGoodbye();
                    break;

                } else if (command.equals("list")) {
                    ui.showTaskList(tasks.getTasks());

                } else if (command.equals("find") || command.startsWith("find ")) {
                    String keyword = Parser.parseFind(command);
                    List<Task> matches = tasks.find(keyword);
                    ui.showMatchingTasks(matches);

                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = Parser.getTaskIndex(command, 4, tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    storage.save(tasks.getTasks());
                    ui.showTask(
                            "The Target has been Neutralized:",
                            tasks.get(taskIndex));

                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = Parser.getTaskIndex(command, 6, tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    storage.save(tasks.getTasks());
                    ui.showTask(
                            "Failed to Complete:",
                            tasks.get(taskIndex));

                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    Task task = Parser.parseTodo(command);
                    tasks.add(task);
                    storage.save(tasks.getTasks());
                    ui.showTask(
                            "Target in time has been located:",
                            task);

                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    Task task = Parser.parseDeadline(command);
                    tasks.add(task);
                    storage.save(tasks.getTasks());
                    ui.showTask(
                            "Incursion Risk, Finish Deadline:",
                            task);

                } else if (command.equals("event") || command.startsWith("event ")) {
                    Task task = Parser.parseEvent(command);
                    tasks.add(task);
                    storage.save(tasks.getTasks());
                    ui.showTask(
                            "Temporal target located:",
                            task);

                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    int taskIndex = Parser.getTaskIndex(command, 6, tasks.size());
                    Task deletedTask = tasks.delete(taskIndex);
                    storage.save(tasks.getTasks());
                    ui.showDeletedTask(deletedTask, tasks.size());

                } else {
                    throw new SkynetException("Unrecognised Command. "
                            + "Use todo/deadline/event/list/mark/unmark/delete/find/bye.");

                }

            } catch (SkynetException | IOException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}
