package model;

public class JoinedAssignment extends Assignment {
    private String courseCode;
    private String courseName;

    public JoinedAssignment(int id, int courseId, int week, String name, Status status, String notes, String courseCode,
            String courseName) {
        super(id, courseId, week, name, status, notes);
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "JoinedAssignment [courseCode=" + courseCode + ", courseName=" + courseName + "], " + super.toString();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
