package skynet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {
    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static Task parseTodo(String command) throws SkyNETException {
        String description = command.substring(4).trim();
        if (description.isEmpty()) {
            throw new SkyNETException("Please input a target.");
        }
        return new Todo(description);
    }

    public static Task parseDeadline(String command) throws SkyNETException {
        String details = command.substring(8).trim();
        int byIndex = details.indexOf(" /by ");
        if (byIndex == -1) {
            throw new SkyNETException(
                    "Please use the format: deadline DESCRIPTION /by yyyy-MM-dd HH:mm");
        }
        String description = details.substring(0, byIndex).trim();
        String byText = details.substring(byIndex + 5).trim();
        if (description.isEmpty() || byText.isEmpty()) {
            throw new SkyNETException(
                    "A deadline needs both a description and a deadline time.");
        }

        try {
            LocalDateTime by = LocalDateTime.parse(
                    byText, INPUT_FORMATTER);

            return new Deadline(description, by);

        } catch (DateTimeParseException e) {
            throw new SkyNETException(
                    "Invalid date. Please use yyyy-MM-dd HH:mm.");
        }
    }

    public static Task parseEvent(String command) throws SkyNETException {
        int fromIndex = command.indexOf(" /from ");
        int toIndex = command.indexOf(" /to ");
        if (fromIndex == -1 || toIndex == -1 || fromIndex > toIndex) {
            throw new SkyNETException(
                    "Please use the format: event DESCRIPTION "
                            + "/from yyyy-MM-dd HH:mm "
                            + "/to yyyy-MM-dd HH:mm");
        }

        String description = command.substring(6, fromIndex).trim();
        String fromText = command.substring(fromIndex + 7, toIndex).trim();
        String toText = command.substring(toIndex + 5).trim();

        if (description.isEmpty() || fromText.isEmpty() || toText.isEmpty()) {
            throw new SkyNETException(
                    "An event needs a description, start time, and end time.");
        }

        try {
            LocalDateTime from = LocalDateTime.parse(
                    fromText, INPUT_FORMATTER);
            LocalDateTime to = LocalDateTime.parse(
                    toText, INPUT_FORMATTER);

            return new Event(description, from, to);

        } catch (DateTimeParseException e) {
            throw new SkyNETException(
                    "Invalid date. Please use yyyy-MM-dd HH:mm.");
        }
    }

    public static String parseFind(String command) throws SkyNETException {
        String keyword = command.substring(4).trim();

        if (keyword.isEmpty()) {
            throw new SkyNETException("Please provide a keyword to find.");
        }

        return keyword;
    }

    public static int getTaskIndex(
            String command, int commandLength, int taskCount)
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
}
