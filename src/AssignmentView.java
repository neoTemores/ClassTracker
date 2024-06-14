import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AssignmentView {
    private boolean isInAssignmentView;
    private boolean isFilteredByWeek;
    private int filteredWeekNum;
    private final AssignmentDAO assignmentDAO;
    private List<Assignment> assignmentList;
    private final Scanner scanner;
    private final Course course;
    private final String courseTitle;

    public AssignmentView(Course course) {
        isInAssignmentView = true;
        isFilteredByWeek = false;
        filteredWeekNum = 0;
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
        assignmentList = assignmentDAO.getAssignmentList(course.getId(), filteredWeekNum);

        Utils.clear();
        String header = isFilteredByWeek ? "Filtered by week # " + filteredWeekNum : "All assignments";
        Utils.printMenuHeader("Assignment View", Utils.getSubHeaderTitle(header, courseTitle));

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
        System.out.print("< Enter week # to filter by (0 = all): ");
        String input = scanner.nextLine();

        if (input.matches("[0-9]+")) {
            this.filteredWeekNum = Integer.parseInt(input);

            this.isFilteredByWeek = this.filteredWeekNum != 0;

        } else {
            Utils.showTempMsg("Error: Week must be a number!");
        }
    }

    private void createAssignment() {
        Utils.clear();
        Utils.printMenuHeader("Assignment View", Utils.getSubHeaderTitle("For course", courseTitle));

        try {
            System.out.print("< Week #: ");
            int week = Integer.parseInt(scanner.nextLine());

            System.out.print("< Name: ");
            String name = scanner.nextLine();

            Assignment.Status status = getStatusInput();

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

    private Assignment.Status getStatusInput() {
        printGetStatusInstructions();
        Assignment.Status status = parseStatus(scanner.nextLine());

        return status;
    }

    private void printGetStatusInstructions() {
        System.out.println();
        Utils.printMenuItem("1", Assignment.Status.NOT_STARTED.getStatus());
        Utils.printMenuItem("2", Assignment.Status.IN_PROGRESS.getStatus());
        Utils.printMenuItem("3", Assignment.Status.COMPLETE.getStatus());

        System.out.print("< Status: ");
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
        System.out.print("> Enter assignment # to update: ");
        String input = scanner.nextLine();
        if (input.matches("[0-9]+")) {
            int courseIndex = Integer.parseInt(input) - 1;
            if (courseIndex >= 0 && courseIndex < assignmentList.size()) {
                Utils.clear();
                Utils.printMenuHeader("Assignment View", Utils.getSubHeaderTitle("Update assignment in ", courseTitle));

                Assignment assignment = assignmentList.get(courseIndex);
                System.out.println("> Updating " + assignment);

                System.out.print("< Week: ");
                String weekStr = scanner.nextLine().trim();
                int week = 0;
                if (weekStr.isBlank() || !weekStr.matches("[0-9]+")) {
                    week = assignment.getWeek();
                } else {
                    week = Integer.parseInt(weekStr);
                }

                System.out.print("< Name: ");
                String name = scanner.nextLine().trim();
                if (name.isBlank()) {
                    name = assignment.getName();
                }

                printGetStatusInstructions();
                String statusStr = scanner.nextLine().trim();
                Assignment.Status status;
                if (statusStr.isBlank()) {
                    status = assignment.getStatus();
                } else {
                    status = parseStatus(statusStr);
                }

                System.out.print("< Notes: ");
                String notes = scanner.nextLine().trim();
                if (notes.isBlank()) {
                    notes = assignment.getNotes();
                }

                assignment.setWeek(week);
                assignment.setName(name);
                assignment.setStatus(status);
                assignment.setNotes(notes);
                System.out.println("> Update to " + assignment);

                System.out.print("< Proceed with update? (y/n): ");
                String confirm = scanner.nextLine();
                if (Utils.confirm(confirm)) {
                    assignmentDAO.updateAssignment(assignment);
                }
            } else {
                Utils.showTempMsg("Error: Selected index is out of bounds!");
            }
        }

    }

    private void deleteAssignment() {
        System.out.print("< Enter assignment # to delete: ");
        String input = scanner.nextLine();

        if (input.matches("[0-9]+")) {
            int assignmentIndex = Integer.parseInt(input) - 1;
            if (assignmentIndex >= 0 && assignmentIndex < assignmentList.size()) {

                Utils.clear();
                Utils.printMenuHeader("Assignment View", Utils.getSubHeaderTitle("Delete assignment", courseTitle));

                Assignment assignment = assignmentList.get(assignmentIndex);
                System.out.println("\n> Seleted " + assignment);
                System.out.print("< Delete? (y/n): ");
                String confirm = scanner.nextLine();

                if (Utils.confirm(confirm)) {
                    assignmentDAO.deleteAssignment(assignment.getId());
                }

            } else {
                Utils.showTempMsg("Error: Selected index is out of bounds!");
            }
        }
    }

    private void printAssignmentList() {
        printLine();
        printRow("#", "Week", "Name", "Status", "Notes");
        printLine();

        if (assignmentList.size() == 0) {
            System.out.println("** No assignments found **");
        }

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