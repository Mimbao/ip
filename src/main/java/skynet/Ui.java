package skynet;

import java.util.List;
import java.util.Scanner;

/**
 * Handles user interaction and displays messages to the user.
 */
public class Ui {
    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome banner and greeting message.
     */
    public void showWelcome() {
        String banner = """
                 ____  _          _   _      _
                / ___|| | ___   _| \\ | | ___| |_
                \\___ \\| |/ / | | |  \\| |/ _ \\ __|
                 ___) |   <| |_| | |\\  |  __/ |_
                |____/|_|\\_\\\\__, |_| \\_|\\___|\\__|
                             |___/
                """;

        System.out.println(banner);
        System.out.println("Welcome to SkyNET.");
        System.out.println("How may we assist you today?");
    }

    /**
     * Reads and returns the next command entered by the user.
     *
     * @return the command entered by the user
     */
    public String readCommand() {
        if (!scanner.hasNextLine()) {
            return "bye";
        }
        return scanner.nextLine();
    }

    /**
     * Displays the goodbye message.
     */
    public void showGoodbye() {
        System.out.println("The Future is now. Chat Terminated.");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null.");
        }
        System.out.println("ERROR: " + message);
    }

    /**
     * Displays all tasks in the task list.
     *
     * @param tasks the tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("Task list cannot be null.");
        }
        System.out.println("[Target List Display]");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Displays a message followed by a task.
     *
     * @param message the message to display
     * @param task the task to display
     */
    public void showTask(String message, Task task) {
        if (message == null || task == null) {
            throw new IllegalArgumentException("Message and task cannot be null.");
        }
        System.out.println(message);
        System.out.println("  " + task);
    }

    /**
     * Displays a deleted task and the number of remaining tasks.
     *
     * @param task the deleted task
     * @param remainingTaskCount the number of tasks remaining
     */
    public void showDeletedTask(Task task, int remainingTaskCount) {
        if (task == null || remainingTaskCount < 0) {
            throw new IllegalArgumentException("Deleted task and count must be valid.");
        }
        System.out.println("Target Erased:");
        System.out.println("  " + task);
        System.out.println("Remaining targets: " + remainingTaskCount);
    }

    /**
     * Displays tasks that match a search query.
     *
     * @param matches the matching tasks to display
     */
    public void showMatchingTasks(List<Task> matches) {
        if (matches.isEmpty()) {
            System.out.println("No matching tasks found.");
            return;
        }

        System.out.println("Here are the matching tasks in your list:");

        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + ". " + matches.get(i));
        }
    }
}
