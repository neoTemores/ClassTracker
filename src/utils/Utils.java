package utils;

import java.sql.ResultSet;

import model.Assignment;
import model.Assignment.Status;
import model.Course;
import model.JoinedAssignment;
import model.Term;

public class Utils {
    public final static String CLEAR_SCREEN = "\033[H\033[2J";

    public final static String RESET = "\u001B[0m";
    public final static String BLACK = "\u001B[30m";
    public final static String RED = "\u001B[31m";
    public final static String GREEN = "\u001B[32m";
    public final static String YELLOW = "\u001B[33m";
    public final static String BLUE = "\u001B[34m";
    public final static String PURPLE = "\u001B[35m";
    public final static String CYAN = "\u001B[36m";
    public final static String WHITE = "\u001B[37m";

    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";

    public static final String HEADER_COLOR = Utils.CYAN;

    public static void clear() {
        System.out.println(CLEAR_SCREEN);
    }

    public static void loading() {
        System.out.print("\n> Loading...");
    }

    public static String colorizeTableHeader(String header, int padding) {

        String headerFmt = "%s%s%s%s%s";
        return String.format(headerFmt, Utils.HEADER_COLOR, " ".repeat(padding), header, " ".repeat(padding), RESET);
    }

    public static String colorizeStatus(String status) {
        String colorizedStatus = null;

        switch (status) {
            case "Not Started":
                colorizedStatus = Utils.RED + BLACK_BACKGROUND + status + Utils.RESET + " ";
                break;
            case "In Progress":
                colorizedStatus = Utils.YELLOW + BLACK_BACKGROUND + status + Utils.RESET + " ";
                break;
            case "Complete":
                colorizedStatus = Utils.GREEN + BLACK_BACKGROUND + status + Utils.RESET + " ".repeat(4);
                break;
            default:
                colorizedStatus = status;
                break;
        }
        return colorizedStatus;
    }

    public static String colorizeBool(Boolean isTrue) {
        String boolRed = RED + isTrue.toString() + RESET + " ".repeat(5);
        String boolGreen = GREEN + isTrue.toString() + RESET + " ".repeat(6);

        return isTrue ? boolGreen : boolRed;
    }

    public static String colorizeLineNum(int lineNum) {
        String colorizedLineNume = String.valueOf(lineNum);
        int length = colorizedLineNume.length();

        if (length == 1) {
            colorizedLineNume = " " + colorizedLineNume + " ";
        } else if (length == 2) {
            colorizedLineNume = " " + colorizedLineNume;
        }
        colorizedLineNume = Utils.HEADER_COLOR + colorizedLineNume + RESET;

        return colorizedLineNume;
    }

    public static void showTempMsg(String msg) {
        System.out.println("\n> ** " + msg + " **");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printMenuHeader(String header, String subheader) {
        String appTitle = "| " + Utils.HEADER_COLOR + "Neo's Assignment Tracker App" + RESET + " |";
        String outline = "+" + "-".repeat(appTitle.length() - 11) + "+";
        System.out.println(outline);
        System.out.println(appTitle);
        System.out.println(outline + "\n");

        String formattedHeader = String.format("| %s |", header);
        String formattedSubheader = String.format("| -> %s |", subheader);

        String topLine = generateUnderline(formattedHeader);
        String middleLine = generateUnderline(formattedHeader);
        String bottomLine = generateUnderline(formattedSubheader);

        int lengthDiff = formattedSubheader.length() - formattedHeader.length();

        for (int i = 0; i < lengthDiff; i++) {
            middleLine = "-" + middleLine;
        }

        System.out.println("+" + topLine);
        System.out.println(formattedHeader);
        System.out.println("+" + middleLine);
        System.out.println(formattedSubheader);
        System.out.println("+" + bottomLine);
    }

    public static String getSubHeaderTitle(String subHeader, String subTitle) {
        String subheaderFormat = "%s (%s)";
        return String.format(subheaderFormat, subHeader, subTitle);
    }

    private static String generateUnderline(String title) {
        String underline = "+";
        for (int i = 1; i < title.length() - 1; i++) {
            underline = "-" + underline;
        }
        return underline;
    }

    public static String generateHorizontalLine(int[] colWidths) {
        String horizontalLine = "+";
        for (int i = 0; i < colWidths.length; i++) {
            horizontalLine += "-".repeat(colWidths[i]) + "+";
        }
        return horizontalLine;
    }

    public static void printCRUDmenu(String item) {
        printMenuItem("C", "Create " + item);
        printMenuItem("U", "Update " + item);
        printMenuItem("D", "Delete " + item);
        printMenuItem("B", "Back");
        printMenuItem("Q", "Quit");
    }

    public static void printMenuItem(String key, String value) {
        String itemFormat = "* %s. %s";
        System.out.println(String.format(itemFormat, key, value));
    }

    public static void printMenuSelection() {
        System.out.print("\n< Selection: ");
    }

    public static boolean confirm(String input) {
        return input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes") || input.equals("1");
    }

    public static Term mapTerm(ResultSet data) {
        Term term = null;
        try {
            int id = data.getInt("id");
            String name = data.getString("name");
            int year = data.getInt("year");
            boolean isActive = data.getBoolean("isActive");

            term = new Term(id, name, year, isActive);

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
        return term;
    }

    public static Course mapCourse(ResultSet data) {
        Course course = null;
        try {
            int id = data.getInt("id");
            int termId = data.getInt("termId");
            String code = data.getString("code");
            String name = data.getString("name");

            course = new Course(id, termId, code, name);

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
        return course;
    }

    public static Assignment mapAssignment(ResultSet data) {
        Assignment assignment = null;
        try {
            int id = data.getInt("id");
            int courseId = data.getInt("courseId");
            int week = data.getInt("week");
            String name = data.getString("name");
            Status status = Status.valueOf(data.getString("status"));
            String notes = data.getString("notes");

            assignment = new Assignment(id, courseId, week, name, status, notes);

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
        return assignment;
    }

    public static JoinedAssignment mapJoinedAssignment(ResultSet data) {
        JoinedAssignment joinedAssignment = null;
        try {
            String courseCode = data.getString("code");
            String courseName = data.getString("courseName");
            int assignmentId = data.getInt("assignmentId");
            int courseId = data.getInt("courseId");
            int week = data.getInt("week");
            String assignmentName = data.getString("assignmentName");
            Status status = Status.valueOf(data.getString("status"));
            String notes = data.getString("notes");

            joinedAssignment = new JoinedAssignment(assignmentId, courseId, week, assignmentName, status, notes,
                    courseCode, courseName);

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }

        return joinedAssignment;
    }
}