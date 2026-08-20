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
                System.out.println("[Task List Display]");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". [" + tasks[i].getStatusIcon() + "] "
                            + tasks[i].getDescription());
                }

            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsDone();

                System.out.println("The Task has been Completed:");
                System.out.println("  [X] " + tasks[taskIndex].getDescription());

            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsNotDone();

                System.out.println("Task Remains Unfulfilled:");
                System.out.println("  [ ] " + tasks[taskIndex].getDescription());

            } else {
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println("_________________________");
                System.out.println("Added: " + command);
                System.out.println("_________________________");
            }
        }
    }
}
