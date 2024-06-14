import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AssignmentView {
    private boolean isInAssignmentView;
    private final AssignmentDAO assignmentDAO;
    private List<Assignment> assignmentList;
    private final Scanner scanner;
    private final Course course;
    private final String courseTitle;

    public AssignmentView(Course course) {
        isInAssignmentView = true;
        assignmentDAO = new AssignmentDAO();
        scanner = new Scanner(System.in);
        this.course = course;
        assignmentList = new ArrayList<>();
        courseTitle = course.getCode() + " " + course.getName();
    }

    public void open() {
        while (isInAssignmentView) {
            mainMenu();
        }
    }

    private void mainMenu() {
        assignmentList = assignmentDAO.getAssignmentList(course.getId());

        Utils.clear();
        Utils.printMenuHeader("Assignment View", generateSubheaderText("For Course"));
        System.out.println(course);
        String pause = scanner.nextLine();
        isInAssignmentView = false;
    }

    private String generateSubheaderText(String subheader) {
        String subheaderFormat = "%s (%s)";
        return String.format(subheaderFormat, subheader, courseTitle);
    }
}