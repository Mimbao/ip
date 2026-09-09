package skynet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses user commands into tasks and task indices.
 */
public class Parser {
    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Parses a to-do command and creates a Todo task.
     *
     * @param command the user command containing the to-do description
     * @return the created Todo task
     * @throws SkynetException if the command has an empty description
     */
    public static Task parseTodo(String command) throws SkynetException {
        assert command != null : "Command string must not be null";
        assert command.startsWith("todo") : "Command passed to parseTodo must start with 'todo'";
        String description = command.substring(4).trim();
        if (description.isEmpty()) {
            throw new SkynetException("Please input a target.");
        }
        return new Todo(description);
    }

    /**
     * Parses a deadline command and creates a Deadline task.
     *
     * @param command the user command containing the description and deadline
     * @return the created Deadline task
     * @throws SkynetException if the command format or deadline date is invalid
     */
    public static Task parseDeadline(String command) throws SkynetException {
        assert command != null : "Command string must not be null";
        assert command.startsWith("deadline") : "Command passed to parseDeadline must start with 'deadline'";
        String details = command.substring(8).trim();
        int byIndex = details.indexOf(" /by ");
        if (byIndex == -1) {
            throw new SkynetException(
                    "Please use the format: deadline DESCRIPTION /by yyyy-MM-dd HH:mm");
        }
        String description = details.substring(0, byIndex).trim();
        String byText = details.substring(byIndex + " /by ".length()).trim();
        if (description.isEmpty() || byText.isEmpty()) {
            throw new SkynetException(
                    "A deadline needs both a description and a deadline time.");
        }

        try {
            LocalDateTime by = LocalDateTime.parse(byText, INPUT_FORMATTER);
            return new Deadline(description, by);
        } catch (DateTimeParseException e) {
            throw new SkynetException("Invalid date. Please use yyyy-MM-dd HH:mm.");
        }
    }

    /**
     * Parses an event command and creates an Event task.
     *
     * @param command the user command containing the description, start time, and end time
     * @return the created Event task
     * @throws SkynetException if the command format or event dates are invalid
     */
    public static Task parseEvent(String command) throws SkynetException {
        assert command != null : "Command string must not be null";
        assert command.startsWith("event") : "Command passed to parseEvent must start with 'event'";
        int fromIndex = command.indexOf(" /from ");
        int toIndex = command.indexOf(" /to ");
        if (fromIndex == -1 || toIndex == -1 || fromIndex > toIndex) {
            throw new SkynetException(
                    "Please use the format: event DESCRIPTION "
                            + "/from yyyy-MM-dd HH:mm "
                            + "/to yyyy-MM-dd HH:mm");
        }

        String description = command.substring("event".length(), fromIndex).trim();
        String fromText = command.substring(fromIndex + " /from ".length(), toIndex).trim();
        String toText = command.substring(toIndex + " /to ".length()).trim();

        if (description.isEmpty() || fromText.isEmpty() || toText.isEmpty()) {
            throw new SkynetException(
                    "An event needs a description, start time, and end time.");
        }

        try {
            LocalDateTime from = LocalDateTime.parse(fromText, INPUT_FORMATTER);
            LocalDateTime to = LocalDateTime.parse(toText, INPUT_FORMATTER);
            return new Event(description, from, to);
        } catch (DateTimeParseException e) {
            throw new SkynetException("Invalid date. Please use yyyy-MM-dd HH:mm.");
        }
    }

    /**
     * Extracts the keyword if Find command was given.
     *
     * @param command the command given containing the keyword
     * @return the keyword string
     * @throws SkynetException if the keyword is empty
     */
    public static String parseFind(String command) throws SkynetException {
        assert command != null : "Command string must not be null";
        assert command.startsWith("find") : "Command passed to parseFind must start with 'find'";

        String keyword = command.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new SkynetException("Please provide a keyword to find.");
        }
        return keyword;
    }

    /**
     * Converts a task number from a user command into a zero-based task index.
     *
     * @param command the user command containing the task number
     * @param commandWord the command word string (e.g., "mark", "unmark", "delete")
     * @param taskCount the number of tasks currently in the task list
     * @return the zero-based index of the selected task
     * @throws SkynetException if the task number is invalid or outside the valid range
     */
    public static int getTaskIndex(String command, String commandWord, int taskCount)
            throws SkynetException {
        assert command != null : "Command string must not be null";
        assert commandWord != null : "commandWord string must not be null";
        assert taskCount >= 0 : "taskCount cannot be negative";
        String numberText = command.substring(commandWord.length()).trim();

        try {
            int taskNumber = Integer.parseInt(numberText);

            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new SkynetException("Target Number does not exist.");
            }

            return taskNumber - 1;

        } catch (NumberFormatException e) {
            throw new SkynetException("Please input a Target Number.");
        }
    }
}
