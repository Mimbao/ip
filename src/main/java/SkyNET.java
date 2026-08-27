import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class SkyNET {
    public static void main(String[] args) {
        String banner = """
                 ____  _          _   _      _
                / ___|| | ___   _| \\ | | ___| |_
                \\___ \\| |/ / | | |  \\| |/ _ \\ __|
                 ___) |   <| |_| | |\\  |  __/ |_
                |____/|_|\\_\\\\__, |_| \\_|\\___|\\__|
                             |___/
                """;

        Storage storage = new Storage();

        // RELOAD TARGETS (IF ANY)
        List<Task> tasks;
        try {
            tasks = storage.load();
        } catch (IOException e) {
            System.out.println("ERROR: Unable to load saved tasks.");
            tasks = new ArrayList<>();
        }

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
                    printTaskList(tasks);

                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = getTaskIndex(command, 4, tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    storage.save(tasks);
                    System.out.println("The Target has been Neutralized:");
                    System.out.println("  " + tasks.get(taskIndex));

                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = getTaskIndex(command, 6, tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    storage.save(tasks);
                    System.out.println("Failed to eliminate Target:");
                    System.out.println("  " + tasks.get(taskIndex));

                } else if (command.equals("todo") || command.startsWith("todo "))  {
                    Task task = createTodo(command);
                    tasks.add(task);
                    storage.save(tasks);
                    System.out.println("Target in time has been located:");
                    System.out.println("  " + task);

                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    Task task = createDeadline(command);
                    tasks.add(task);
                    storage.save(tasks);
                    System.out.println("Incursion Risk, Eliminate Target:");
                    System.out.println("  " + task);

                } else if (command.equals("event") || command.startsWith("event ")) {
                    Task task = createEvent(command);
                    tasks.add(task);
                    storage.save(tasks);
                    System.out.println("Temporal target located:");
                    System.out.println("  " + task);


                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    int taskIndex = getTaskIndex(command, 6, tasks.size());
                    Task deletedTask = tasks.remove(taskIndex);
                    storage.save(tasks);
                    System.out.println("Target Erased:");
                    System.out.println("  " + deletedTask);
                    System.out.println("Remaining targets: " + tasks.size());

                } else {
                    throw new SkyNETException("Unrecognised Command. " +
                            "Use todo/deadline/event/list/mark/unmark/delete/bye.");
                }

            } catch (SkyNETException | IOException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }



    // Handle input management and create the Tasks
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
                    "Please use the format: deadline DESCRIPTION /by date");
        }

        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + 5).trim();

        if (description.isEmpty() || by.isEmpty()) {
            throw new SkyNETException(
                    "A deadline needs both a description and a deadline time.");
        }

        return new Deadline(description, by);
    }

    private static Task createEvent(String command) throws SkyNETException {
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

        return new Event(description, from, to);
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

    // Handle print list logic
    private static void printTaskList(List<Task> tasks) {
        System.out.println("[Target List Display]");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
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