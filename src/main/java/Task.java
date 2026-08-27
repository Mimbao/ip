public abstract class Task {
    private final String description;
    private TaskStatus status;

    // Constructor
    public Task(String description) {
        this.description = description;
        this.status = TaskStatus.NOT_DONE;
    }

    // Mark method
    public void markAsDone() {
        status = TaskStatus.DONE;
    }

    // Unmark method
    public void markAsNotDone() {
        status = TaskStatus.NOT_DONE;
    }

    // Display Mark method
    public String getStatusIcon() {
        return status.getIcon();
    }

    // Get Description
    public String getDescription() {
        return description;
    }

    // Get status
    public boolean isDone() {
        return status == TaskStatus.DONE;
    }

    public abstract String toString();
}
