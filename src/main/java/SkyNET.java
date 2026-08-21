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
            try {
                if (command.equals("bye")) {
                    System.out.println("The Future, now. Chat Terminated.");
                    break;

                } else if (command.equals("list")) {
                    printTaskList(tasks, taskCount);

                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = getTaskIndex(command, 4, taskCount);
                    tasks[taskIndex].markAsDone();

                    System.out.println("The Target has been Neutralized:");
                    System.out.println("  " + tasks[taskIndex]);

                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = getTaskIndex(command, 6, taskCount);
                    tasks[taskIndex].markAsNotDone();

                    System.out.println("Failed to eliminate Target:");
                    System.out.println("  " + tasks[taskIndex]);

                } else if (command.equals("todo") || command.startsWith("todo "))  {
                    String description = command.substring(4).trim();

                    if (description.isEmpty()) {
                        throw new SkyNETException("Please input a target.");
                    }

                    ensureTaskListHasSpace(tasks, taskCount);
                    tasks[taskCount] = new Todo(description);
                    System.out.println("Target in time has been located:");
                    System.out.println("  " + tasks[taskCount]);
                    taskCount++;

                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    String details = command.substring(8).trim();
                    int byIndex = details.indexOf(" /by ");

                    if (byIndex == -1) {
                        throw new SkyNETException(
                                "Please use the format: deadline DESCRIPTION /by date");
                    }

                    String description = details.substring(0, byIndex).trim();
                    String by = details.substring(byIndex + 5).trim();

                    if (description.isEmpty() || by.isEmpty()) {
                        throw new SkyNETException(
                                "A deadline needs both a description and a deadline time.");
                    }

                    ensureTaskListHasSpace(tasks, taskCount);
                    tasks[taskCount] = new Deadline(description, by);
                    System.out.println("Incursion Risk, Eliminate Target:");
                    System.out.println("  " + tasks[taskCount]);
                    taskCount++;

                } else if (command.equals("event") || command.startsWith("event ")) {
                    int fromIndex = command.indexOf(" /from ");
                    int toIndex = command.indexOf(" /to ");

                    if (fromIndex == -1 || toIndex == -1 || fromIndex > toIndex) {
                        throw new SkyNETException(
                                "Please use the format: event DESCRIPTION /from date /to date");
                    }

                    String description = command.substring(6, fromIndex).trim();
                    String from = command.substring(fromIndex + 7, toIndex).trim();
                    String to = command.substring(toIndex + 5).trim();

                    if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                        throw new SkyNETException(
                                "An event needs a description, start time, and end time.");
                    }

                    ensureTaskListHasSpace(tasks, taskCount);
                    tasks[taskCount] = new Event(description, from, to);
                    System.out.println("Temporal target located:");
                    System.out.println("  " + tasks[taskCount]);

                    taskCount++;

                } else {
                    throw new SkyNETException("Unrecognised Command. Use todo/deadline/event/list/mark/unmark/bye.");
                }

            } catch (SkyNETException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
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

    // Method to error handle >100 tasks
    private static void ensureTaskListHasSpace(Task[] tasks, int taskCount)
            throws SkyNETException {
        if (taskCount >= tasks.length) {
            throw new SkyNETException("Maximum Capacity Reached.");
        }
    }

    // Abstracted print list logic
    private static void printTaskList(Task[] tasks, int taskCount) {
        System.out.println("[Target List Display]");

        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
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
// 8. Prevent exceeding 100 targets