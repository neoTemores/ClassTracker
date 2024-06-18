package model;

public class Assignment {

    public enum Status {
        NOT_STARTED("Not Started"),
        IN_PROGRESS("In Progress"),
        COMPLETE("Complete");

        private String status;

        private Status(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    private int id;
    private int courseId;
    private int week;
    private String name;
    private Status status;
    private String notes;

    public Assignment() {
        this(0, 0, 0, "name - n/a", Status.NOT_STARTED, null);
    }

    public Assignment(int id, int courseId, int week, String name, Status status, String notes) {
        this.id = id;
        this.courseId = courseId;
        this.week = week;
        this.name = name;
        this.status = status;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Assignment [id=" + id + ", courseId=" + courseId + ", week=" + week + ", name=" + name + ", status="
                + status + ", notes=" + notes + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}