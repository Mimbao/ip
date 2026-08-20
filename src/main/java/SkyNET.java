import java.util.Scanner;

public class SkyNET {
    public static void main(String[] args) {
        String banner = " ____  _          _   _      _\n"
                + "/ ___|| | ___   _| \\ | | ___| |_\n"
                + "\\___ \\| |/ / | | |  \\| |/ _ \\ __|\n"
                + " ___) |   <| |_| | |\\  |  __/ |_\n"
                + "|____/|_|\\_\\\\__, |_| \\_|\\___|\\__|\n"
                + "             |___/\n";

        Task[] tasks = new Task[100];
        int taskCount = 0;
        System.out.println(banner);
        System.out.println("Welcome to SkyNET.");
        System.out.println("How may we assist you today?");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println("We are the Future. Chat Terminated.");
                break;

            } else if (command.equals("list")) {
                System.out.println("[Target List Display]");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }

            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsDone();

                System.out.println("The Target has been Neutralized:");
                System.out.println("  " + tasks[taskIndex]);

            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsNotDone();

                System.out.println("Failed to eliminate Target:");
                System.out.println("  " + tasks[taskIndex]);

            } else if (command.startsWith("todo ")) {
                String description = command.substring(5);
                tasks[taskCount] = new Todo(description);

                System.out.println("Target in time has been located:");
                System.out.println("  " + tasks[taskCount]);

                taskCount++;
            } else if (command.startsWith("deadline ")) {
                int byIndex = command.indexOf(" /by ");

                String description = command.substring(9, byIndex);
                String by = command.substring(byIndex + 5);

                tasks[taskCount] = new Deadline(description, by);

                System.out.println("Incursion Risk, Eliminate Target:");
                System.out.println("  " + tasks[taskCount]);

                taskCount++;
            } else if (command.startsWith("event ")) {
                int fromIndex = command.indexOf(" /from ");
                int toIndex = command.indexOf(" /to ");

                String description = command.substring(6, fromIndex);
                String from = command.substring(fromIndex + 7, toIndex);
                String to = command.substring(toIndex + 5);

                tasks[taskCount] = new Event(description, from, to);

                System.out.println("Temporal target located:");
                System.out.println("  " + tasks[taskCount]);

                taskCount++;
            } else {
                System.out.println("Unknown command. Try: todo, deadline, event, list, mark, unmark, or bye.");
            }
        }
    }
}
