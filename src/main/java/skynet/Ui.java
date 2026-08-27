package skynet;

import java.util.List;
import java.util.Scanner;

public class Ui {
    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

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
        System.out.println("Welcome to skynet.SkyNET.");
        System.out.println("How may we assist you today?");
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showGoodbye() {
        System.out.println("The Future, now. Chat Terminated.");
    }

    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }

    public void showTaskList(List<Task> tasks) {
        System.out.println("[Target List Display]");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    public void showTask(String message, Task task) {
        System.out.println(message);
        System.out.println("  " + task);
    }

    public void showDeletedTask(Task task, int remainingTasks) {
        System.out.println("Target Erased:");
        System.out.println("  " + task);
        System.out.println("Remaining targets: " + remainingTasks);
    }
}
