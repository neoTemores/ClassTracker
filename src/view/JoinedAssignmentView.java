package view;

import java.util.ArrayList;
import java.util.List;
import controller.JoinedAssignmentDAO;
import model.Assignment.Status;
import model.JoinedAssignment;
import model.Term;
import utils.Utils;

public class JoinedAssignmentView extends AssignmentView {
    private final JoinedAssignmentDAO joinedAssignmentDAO;
    private final Term term;
    private final String termTitle;
    private List<JoinedAssignment> joinedAssignmentList;

    public JoinedAssignmentView(Term term) {
        this.term = term;
        termTitle = term.getName() + " " + term.getYear();
        joinedAssignmentDAO = new JoinedAssignmentDAO();
        joinedAssignmentList = new ArrayList<>();
    }

    public void open() {
        while (isInAssignmentView) {
            mainMenu();
        }
    }

    private void mainMenu() {
        Utils.loading();
        joinedAssignmentList = joinedAssignmentDAO.getJoinedAssignmentList(term.getId(), filteredWeekNum);

        Utils.clear();
        String header = isFilteredByWeek ? "Filtered by week # " + filteredWeekNum : "All Assignments";
        Utils.printMenuHeader("Assignment by Term View", Utils.getSubHeaderTitle(header, termTitle));

        printJoinedAssignmentList();

        System.out.println();
        Utils.printMenuItem("F", "Filter by week");
        Utils.printMenuItem("A", "Advance status");
        Utils.printMenuItem("B", "Back");
        Utils.printMenuItem("Q", "Quit");

        Utils.printMenuSelection();

        String input = scanner.nextLine().trim();
        switch (input) {
            case "f":
            case "F":
                openFilterByWeekView();
                break;
            case "a":
            case "A":
                advanceStatus();
                break;
            case "b":
            case "B":
                isInAssignmentView = false;
                break;
            case "q":
            case "Q":
                scanner.close();
                System.out.println("\n> Goodbye.");
                System.exit(0);
                break;
            default:
                String msg = "Error: Invalid input! '%s'";
                Utils.showTempMsg(String.format(msg, input));
                break;
        }
    }

    private void advanceStatus() {
        System.out.print("< Enter assignment # to advance: ");
        String input = scanner.nextLine();

        try {
            int index = Integer.parseInt(input) - 1;
            JoinedAssignment assignment = joinedAssignmentList.get(index);

            Status newStatus = getNewStatus(assignment.getStatus());
            boolean isDiscussionPost = checkAssignmentIsDiscussionPost(assignment.getName(), assignment.getNotes());

            if (isDiscussionPost) {
                String[] arrStatusNotes = getUpdatedAssignmentStatusAndNotes(assignment.getNotes());
                String status = arrStatusNotes[0];
                String notes = arrStatusNotes[1];
                joinedAssignmentDAO.updateAssignmentStatusAndNotes(status, notes, assignment.getId());
            } else if (!newStatus.equals(assignment.getStatus())) {
                joinedAssignmentDAO.updateAssignmentStatus(newStatus, assignment.getId());
            }
        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
    }

    private void printJoinedAssignmentList() {
        String hash = Utils.colorizeTableHeader("#", 1);
        String code = Utils.colorizeTableHeader("Code", 3);
        String courseName = Utils.colorizeTableHeader("Course Name", 12);
        String week = Utils.colorizeTableHeader("Week", 0);
        String assignment = Utils.colorizeTableHeader("Assignment", 5);
        String status = Utils.colorizeTableHeader("Status", 3);
        String notes = Utils.colorizeTableHeader("Notes", 12);

        printLine();
        printRow(hash, code, courseName, week, assignment, status, notes);
        printLine();

        if (joinedAssignmentList.size() == 0) {
            System.out.println("** No assignments found **");
        }

        int lineNum = 1;
        String currentCourse = "";
        // = joinedAssignmentList.size() > 0 ?
        // joinedAssignmentList.get(0).getCourseCode() : ""
        if (joinedAssignmentList.size() > 0) {
            currentCourse = joinedAssignmentList.get(0).getCourseCode();
        }
        for (JoinedAssignment a : joinedAssignmentList) {
            if (!currentCourse.equals(a.getCourseCode())) {
                printLine();
                currentCourse = a.getCourseCode();
            }
            printRow(lineNum, a.getCourseCode(), a.getCourseName(), a.getWeek(), a.getName(), a.getStatus(),
                    a.getNotes());
            printLine();

            lineNum++;
        }
    }

    private void printLine() {
        int[] colWidths = { 5, 12, 37, 6, 22, 14, 31 };
        String horizontalLine = Utils.generateHorizontalLine(colWidths);
        System.out.println(horizontalLine);
    }

    private void printRow(String lineNum, String code, String courseName, String week, String assignmentName,
            String status,
            String notes) {
        String columnFormat = "| %3s | %10s | %35s | %4s | %20s | %12s | %29s |";

        status = Utils.colorizeStatus(status);
        String row = String.format(columnFormat, lineNum, code, courseName, week, assignmentName, status, notes);
        System.out.println(row);
    }

    private void printRow(int lineNum, String code, String courseName, int week, String assignment, Status status,
            String notes) {

        if (assignment.length() > 20) {
            assignment = assignment.substring(0, 18) + "..";
        }
        if (notes.length() > 29) {
            notes = notes.substring(0, 27) + "..";
        }
        if (courseName.length() > 35) {
            courseName = courseName.substring(0, 33) + "..";
        }
        printRow(Utils.colorizeLineNum(lineNum), code, courseName, String.valueOf(week), assignment, status.getStatus(),
                notes);
    }
}
