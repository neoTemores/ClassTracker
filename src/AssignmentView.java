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

        System.out.println();
        Utils.printMenuItem("W", "Filter by week #");
        Utils.printCRUDmenu();
        Utils.printMenuSelection();

        String input = scanner.nextLine().trim();
        switch (input) {
            case "w":
            case "W":
                openFilterByWeekView();
                break;
            case "c":
            case "C":
                createAssignment();
                break;
            case "u":
            case "U":
                updateAssignment();
                break;
            case "d":
            case "D":
                deleteAssignment();
                break;
            case "b":
            case "B":
                isInAssignmentView = false;
                break;
            case "":
                break;
            default:
                scanner.close();
                System.exit(0);
                break;
        }
    }

    private void openFilterByWeekView() {

    }

    private void createAssignment() {
        Utils.clear();
        Utils.printMenuHeader("Assignment View", Utils.generateSubHeaderTitle("For Course", courseTitle));

        try {
            System.out.print("< Week #: ");
            int week = Integer.parseInt(scanner.nextLine());

            System.out.print("< Name: ");
            String name = scanner.nextLine();

            System.out.println();
            Utils.printMenuItem("1", Assignment.Status.NOT_STARTED.getStatus());
            Utils.printMenuItem("2", Assignment.Status.IN_PROGRESS.getStatus());
            Utils.printMenuItem("3", Assignment.Status.COMPLETE.getStatus());

            System.out.print("< Status: ");
            Assignment.Status status = parseStatus(scanner.nextLine());

            System.out.print("< Notes: ");
            String notes = scanner.nextLine();

            Assignment assignment = new Assignment(0, course.getId(), week, name, status, notes);
            System.out.println("> New " + assignment);
            System.out.print("\n< Create new assignment? (y/n): ");

            String confrim = scanner.nextLine();
            if (Utils.confirm(confrim)) {
                assignmentDAO.createAssignment(assignment);
            }
        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
    }

    private Assignment.Status parseStatus(String input) {
        Assignment.Status status;
        switch (input) {
            case "2":
                status = Assignment.Status.IN_PROGRESS;
                break;
            case "3":
                status = Assignment.Status.COMPLETE;
                break;
            default:
                status = Assignment.Status.NOT_STARTED;
                break;
        }

        return status;
    }

    private void updateAssignment() {

    }

    private void deleteAssignment() {
        System.out.print("< Enter assignment # to delete: ");
        String input = scanner.nextLine();

        if (input.matches("[0-9]+")) {
            int assignmentIndex = Integer.parseInt(input) - 1;
            if (assignmentIndex >= 0 && assignmentIndex < assignmentList.size()) {
                // todo HANDLE DELETE
            } else {
                Utils.showTempMsg("Error: Selected index is out of bounds!");
            }
        }
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
        printRow(String.valueOf(lineNum), String.valueOf(week), name, status.getStatus(), notes);
    }
}