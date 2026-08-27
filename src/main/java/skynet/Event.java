package skynet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event with a description, start date and end date.
 */
public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an event task.
     *
     * @param description the description of the deadline
     * @param from the date and time by which the event starts
     * @param to the date and time by which the event ends
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the date and time of when the event starts.
     *
     * @return start date and time as a LocalDateTime
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Gets the date and time of when the event ends.
     *
     * @return end date and time as a LocalDateTime
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns a string representation of the event.
     *
     * @return the event represented as a string
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm a");

        return "[E][" + getStatusIcon() + "] " + getDescription()
                + " (from: " + from.format(formatter)
                + " to: " + to.format(formatter) + ")";
    }
}
