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
        Utils.printMenuHeader("Assignment View", Utils.generateSubHeaderTitle("For Course", courseTitle));

        printAssignmentList();
        // todo delete:
        String pause = scanner.nextLine();
        isInAssignmentView = false;
    }

    private void printAssignmentList() {
        printLine();
        printRow("#", "Week", "Name", "Status", "Notes");
        printLine();

        int lineNum = 1;
        for (Assignment a : assignmentList) {
            printRow(lineNum, a.getWeek(), a.getName(), a.getStatus(), a.getNotes());
            printLine();
            lineNum++;
        }
    }

    private void printLine() {
        String horizonalLine = "+---+------+------------------+--------------+----------------------+";
        System.out.println(horizonalLine);
    }

    private void printRow(String col1, String col2, String col3, String col4, String col5) {
        String columnFormat = "| %1s | %4s | %16s | %12s | %20s |";
        String row = String.format(columnFormat, col1, col2, col3, col4, col5);
        System.out.println(row);
    }

    private void printRow(int lineNum, int week, String name, Assignment.Status status, String notes) {
        printRow(String.valueOf(lineNum), String.valueOf(week), name, status.toString(), notes);
    }
}