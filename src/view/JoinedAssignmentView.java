package view;

import controller.JoinedAssignmentDAO;
import model.Term;
import utils.Utils;

public class JoinedAssignmentView extends AssignmentView {
    private final JoinedAssignmentDAO joinedAssignmentDAO;
    private final Term term;
    private final String termTitle;

    public JoinedAssignmentView(Term term) {
        super(null);
        this.term = term;
        termTitle = term.getName() + " " + term.getYear();
        joinedAssignmentDAO = new JoinedAssignmentDAO();
    }

    public void open() {
        while (isInAssignmentView) {
            mainMenu();
        }
    }

    private void mainMenu() {
        Utils.loading();
        assignmentList = joinedAssignmentDAO.getAssignmentList(term.getId(), filteredWeekNum);

        Utils.clear();
        String header = isFilteredByWeek ? "Filtered by week # " + filteredWeekNum : "All Assignments in Term";
        Utils.printMenuHeader("Assignment by Term View", Utils.getSubHeaderTitle(header, termTitle));

        printJoinedAssignmentList();
        System.out.print("Exit view: ");
        scanner.nextLine();
        isInAssignmentView = false;
    }

    private void printJoinedAssignmentList() {
        String color = Utils.BLACK_BACKGROUND + Utils.WHITE;
        String hash = "#";
        String code = "Code";
        String courseName = "Course Name";
        String week = "Week";
        String assignment = "Assignment";
        String status = "Status";
        String notes = "Notes";

        printLine();
        printRow(hash, code, courseName, week, assignment, status, notes);
        printLine();
    }

    private void printLine() {
        String horizontalLine = "+" + "-".repeat(5) + "+" + "-".repeat(12) + "+" + "-".repeat(37) + "+"
                + "-".repeat(6) + "+" + "-".repeat(18) + "+" + "-".repeat(14) + "+" + "-".repeat(33) + "+";
        System.out.println(horizontalLine);
    }

    private void printRow(String lineNum, String code, String courseName, String week, String assignment, String status,
            String notes) {
        String columnFormat = "| %3s | %10s | %35s | %4s | %16s | %12s | %31s |";

        // status = Utils.colorizeStatus(status);
        String row = String.format(columnFormat, lineNum, code, courseName, week, assignment, status, notes);
        System.out.println(row);
    }
}
