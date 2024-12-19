package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import controller.AssignmentDAO;
import model.Assignment;
import model.Assignment.Status;
import model.Course;
import utils.Utils;

public class AssignmentView {
    protected boolean isInAssignmentView;
    protected boolean isFilteredByWeek;
    protected int filteredWeekNum;
    protected final Scanner scanner;
    private List<Assignment> assignmentList;
    private final AssignmentDAO assignmentDAO;
    private final Course course;
    private final String courseTitle;

    public AssignmentView() {
        this(new Course());
    }

    public AssignmentView(Course course) {
        isInAssignmentView = true;
        isFilteredByWeek = false;
        filteredWeekNum = 0;
        scanner = new Scanner(System.in);
        this.course = course;
        courseTitle = course.getCode() + " " + course.getName();
        assignmentList = new ArrayList<>();
        assignmentDAO = new AssignmentDAO();
    }

    public void open() {
        while (isInAssignmentView) {
            mainMenu();
        }
    }

    private void mainMenu() {
        Utils.loading();
        assignmentList = assignmentDAO.getAssignmentList(course.getId(), filteredWeekNum);

        Utils.clear();
        String header = isFilteredByWeek ? "Filtered by week # " + filteredWeekNum : "All Assignments";
        Utils.printMenuHeader("Assignment View", Utils.getSubHeaderTitle(header, courseTitle));

        printAssignmentList();

        System.out.println();
        Utils.printMenuItem("F", "Filter by week");
        Utils.printMenuItem("A", "Advance status");
        Utils.printCRUDmenu("assignment");
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

    protected Status getNewStatus(Status status) {
        if (status.equals(Status.NOT_STARTED)) {
            status = Status.IN_PROGRESS;
        } else if (status.equals(Status.IN_PROGRESS)) {
            status = Status.COMPLETE;
        }

        return status;
    }

    private void advanceStatus() {
        System.out.print("< Enter assignment # to advance: ");
        String input = scanner.nextLine();

        try {
            int index = Integer.parseInt(input) - 1;
            Assignment assignment = assignmentList.get(index);
            Status newStatus = getNewStatus(assignment.getStatus());
            boolean isDiscussionPost = checkAssignmentIsDiscussionPost(assignment.getName(), assignment.getNotes());

            if (isDiscussionPost) {
                String[] arrStatusNotes = getUpdatedAssignmentStatusAndNotes(assignment.getNotes());
                String status = arrStatusNotes[0];
                String notes = arrStatusNotes[1];
                assignmentDAO.updateAssignmentStatusAndNotes(status, notes, assignment.getId());

            } else if (!newStatus.equals(assignment.getStatus())) {
                assignmentDAO.updateAssignmentStatus(newStatus, assignment.getId());
            }

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
    }

    protected boolean checkAssignmentIsDiscussionPost(String name, String notes) {
        boolean isDiscussionPost = false;

        if ((name.toLowerCase().contains("discussion") || name.toLowerCase().contains("post")) && notes.contains("/")) {
            isDiscussionPost = true;
        }

        return isDiscussionPost;
    }

    protected String[] getUpdatedAssignmentStatusAndNotes(String oldNotes) {
        String[] splitNotes = oldNotes.split("/");

        int currentNumOfPosts = 0;
        int totalNumOfPosts = 0;

        try {
            currentNumOfPosts = Integer.parseInt(splitNotes[0].trim());
            totalNumOfPosts = Integer.parseInt(splitNotes[1].trim());

            if (currentNumOfPosts < totalNumOfPosts) {
                currentNumOfPosts++;
            }
        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("E, MMM dd yyy");
        String formattedDate = currentDate.format(dateFormat);

        Status newStatus = currentNumOfPosts == totalNumOfPosts ? Status.COMPLETE : Status.IN_PROGRESS;
        String newNote = String.format("%d/%d / [%s]", currentNumOfPosts, totalNumOfPosts, formattedDate);

        return new String[] { newStatus.toString(), newNote };
    }

    private void createAssignment() {
        Utils.clear();
        Utils.printMenuHeader("Assignment View",
                Utils.getSubHeaderTitle("Create new Assignment", courseTitle));

        try {
            System.out.print("\n< Week #: ");
            int week = Integer.parseInt(scanner.nextLine());

            System.out.print("< Name: ");
            String name = scanner.nextLine();

            Status status = getStatusInput();

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

    private Status getStatusInput() {
        printGetStatusInstructions();
        Status status = parseStatus(scanner.nextLine());

        return status;
    }

    private void printGetStatusInstructions() {
        System.out.println();
        Utils.printMenuItem("1", Utils.RED + Status.NOT_STARTED.getStatus() + Utils.RESET);
        Utils.printMenuItem("2", Utils.YELLOW + Status.IN_PROGRESS.getStatus() + Utils.RESET);
        Utils.printMenuItem("3", Utils.GREEN + Status.COMPLETE.getStatus() + Utils.RESET);

        System.out.print("< Status: ");
    }

    private Status parseStatus(String input) {
        Status status;

        switch (input) {
            case "2":
                status = Status.IN_PROGRESS;
                break;
            case "3":
                status = Status.COMPLETE;
                break;
            default:
                status = Status.NOT_STARTED;
                break;
        }

        return status;
    }

    private void updateAssignment() {
        System.out.print("\n> Enter assignment # to update: ");
        String input = scanner.nextLine();
        try {
            int courseIndex = Integer.parseInt(input) - 1;
            Assignment assignment = assignmentList.get(courseIndex);

            Utils.clear();
            Utils.printMenuHeader("Assignment View", Utils.getSubHeaderTitle("Update assignment", courseTitle));

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
            Status status;
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
            System.out.println("\n> Update to " + assignment);

            System.out.print("< Proceed with update? (y/n): ");
            String confirm = scanner.nextLine();
            if (Utils.confirm(confirm)) {
                assignmentDAO.updateAssignment(assignment);
            }
        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
    }

    private void deleteAssignment() {
        System.out.print("< Enter assignment # to delete: ");
        String input = scanner.nextLine();
        try {
            int assignmentIndex = Integer.parseInt(input) - 1;
            Assignment assignment = assignmentList.get(assignmentIndex);

            Utils.clear();
            Utils.printMenuHeader("Assignment View", Utils.getSubHeaderTitle("Delete assignment", courseTitle));

            System.out.println("\n> Seleted " + assignment);
            System.out.print("< Delete? (y/n): ");
            String confirm = scanner.nextLine();

            if (Utils.confirmDelete(confirm)) {
                assignmentDAO.deleteAssignment(assignment.getId());
            }
        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
    }

    private void printAssignmentList() {

        String hash = Utils.colorizeTableHeader("#", 1);
        String week = Utils.colorizeTableHeader("Week", 0);
        String name = Utils.colorizeTableHeader("Name", 8);
        String status = Utils.colorizeTableHeader("Status", 3);
        String notes = Utils.colorizeTableHeader("Notes", 13);

        printLine();
        printRow(hash, week, name, status, notes);
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
        int[] colWidths = { 5, 6, 22, 14, 33 };
        String horizonalLine = Utils.generateHorizontalLine(colWidths);
        System.out.println(horizonalLine);
    }

    private void printRow(String lineNum, String week, String name, String status, String notes) {
        String columnFormat = "| %3s | %4s | %20s | %12s | %31s |";

        status = Utils.colorizeStatus(status);

        String row = String.format(columnFormat, lineNum, week, name, status, notes);
        System.out.println(row);
    }

    private void printRow(int lineNum, int week, String name, Status status, String notes) {

        if (name.length() > 20) {
            name = name.substring(0, 18) + "..";
        }
        if (notes.length() > 30) {
            notes = notes.substring(0, 28) + "..";
        }

        printRow(Utils.colorizeLineNum(lineNum), String.valueOf(week), name, status.getStatus(), notes);
    }
}