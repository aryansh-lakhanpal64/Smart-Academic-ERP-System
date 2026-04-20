package model;

public class Notification {
    private int targetId; // -1 means all students
    private String message;

    public Notification(int targetId, String message) {
        this.targetId = targetId;
        this.message = message;
    }

    public int getTargetId() { return targetId; }
    public String getMessage() { return message; }
}
