public class Task {
    private final String description;
    private boolean isDone;

    // Constructor
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    // Mark method
    public void markAsDone() {
        isDone = true;
    }

    // Unmark method
    public void markAsNotDone() {
        isDone = false;
    }

    // Display Mark method
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    // Get Description
    public String getDescription() {
        return description;
    }
}
