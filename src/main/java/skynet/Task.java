package skynet;

public abstract class Task {
    private final String description;
    private TaskStatus status;

    public Task(String description) {
        this.description = description;
        this.status = TaskStatus.NOT_DONE;
    }

    public void markAsDone() {
        status = TaskStatus.DONE;
    }

    public void markAsNotDone() {
        status = TaskStatus.NOT_DONE;
    }

    public String getStatusIcon() {
        return status.getIcon();
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return status == TaskStatus.DONE;
    }

    public abstract String toString();
}
