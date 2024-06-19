package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.JoinedAssignmentDAO;
import model.Assignment.Status;
import model.JoinedAssignment;
import model.Term;
import utils.Utils;

public class JoinedAssignmentView {
    private boolean isInJoinedAssignmentView;
    private boolean isFilteredByWeek;
    private int filteredWeekNum;
    private final Scanner scanner;
    private final JoinedAssignmentDAO joinedAssignmentDAO;
    private final Term term;
    private final String termTitle;
    private List<JoinedAssignment> joinedAssignmentList;

    public JoinedAssignmentView(Term term) {
        isInJoinedAssignmentView = true;
        isFilteredByWeek = false;
        filteredWeekNum = 0;
        this.term = term;
        termTitle = term.getName() + " " + term.getYear();
        scanner = new Scanner(System.in);
        joinedAssignmentDAO = new JoinedAssignmentDAO();
        joinedAssignmentList = new ArrayList<>();
    }

    public void open() {
        while (isInJoinedAssignmentView) {
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
                isInJoinedAssignmentView = false;
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

    // todo Extract this by implementing interface IAssignment on POJOs and
    // extending AssignmentView
    protected void openFilterByWeekView() {
        System.out.print("< Enter week # to filter by (0 = all): ");
        String input = scanner.nextLine();
        try {
            this.filteredWeekNum = Integer.parseInt(input);
            this.isFilteredByWeek = this.filteredWeekNum != 0;
        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
    }

    // todo compelte. need dao method to update status
    private void advanceStatus() {

    }

    private void printJoinedAssignmentList() {
        String hash = Utils.colorizeTableHeader("#", 1);
        String code = Utils.colorizeTableHeader("Code", 3);
        String courseName = Utils.colorizeTableHeader("Course Name", 12);
        String week = Utils.colorizeTableHeader("Week", 0);
        String assignment = Utils.colorizeTableHeader("Assignment", 3);
        String status = Utils.colorizeTableHeader("Status", 3);
        String notes = Utils.colorizeTableHeader("Notes", 13);

        printLine();
        printRow(hash, code, courseName, week, assignment, status, notes);
        printLine();

        if (joinedAssignmentList.size() == 0) {
            System.out.println("** No assignments found **");
        }

        int lineNum = 1;
        for (JoinedAssignment a : joinedAssignmentList) {
            printRow(lineNum, a.getCourseCode(), a.getCourseName(), a.getWeek(), a.getName(), a.getStatus(),
                    a.getNotes());
            printLine();
            lineNum++;
        }
    }

    private void printLine() {
        int[] colWidths = { 5, 12, 37, 6, 18, 14, 33 };
        String horizontalLine = Utils.generateHorizontalLine(colWidths);
        System.out.println(horizontalLine);
    }

    private void printRow(String lineNum, String code, String courseName, String week, String assignmentName,
            String status,
            String notes) {
        String columnFormat = "| %3s | %10s | %35s | %4s | %16s | %12s | %31s |";

        status = Utils.colorizeStatus(status);
        String row = String.format(columnFormat, lineNum, code, courseName, week, assignmentName, status, notes);
        System.out.println(row);
    }

    private void printRow(int lineNum, String code, String courseName, int week, String assignment, Status status,
            String notes) {

        if (assignment.length() > 16) {
            assignment = assignment.substring(0, 14) + "..";
        }
        if (notes.length() > 30) {
            notes = notes.substring(0, 28) + "..";
        }

        printRow(Utils.colorizeLineNum(lineNum), code, courseName, String.valueOf(week), assignment, status.getStatus(),
                notes);
    }
}
