package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.CourseDAO;
import model.Course;
import model.Term;
import utils.Utils;

public class CourseView {
    private boolean isInCourseView;
    private final CourseDAO courseDAO;
    private List<Course> courseList;
    private final Scanner scanner;
    private final Term term;
    private final String termTitle;

    public CourseView(Term term) {
        isInCourseView = true;
        courseDAO = new CourseDAO();
        scanner = new Scanner(System.in);
        this.term = term;
        courseList = new ArrayList<>();
        termTitle = term.getName() + " " + term.getYear();
    }

    public void open() {

        while (isInCourseView) {
            mainMenu();
        }
    }

    private void mainMenu() {
        Utils.loading();
        courseList = courseDAO.getCourseList(term.getId());

        Utils.clear();
        Utils.printMenuHeader("Course View", Utils.getSubHeaderTitle("For Term", termTitle));

        printCourseList();

        String option1 = String.format("Enter course # (1 - %s)", courseList.size());
        if (courseList.size() == 0) {
            option1 = "No courses found";
        } else if (courseList.size() == 1) {
            option1 = "Enter course #";
        }
        System.out.println();
        Utils.printMenuItem("#", option1);
        Utils.printMenuItem("A", "All assignments");
        Utils.printCRUDmenu("course");
        Utils.printMenuSelection();

        String input = scanner.nextLine().trim();
        if (input.matches("[0-9]+")) {
            // input is a num
            int courseIndex = Integer.parseInt(input) - 1;
            if (courseIndex >= 0 && courseIndex < courseList.size()) {
                openAssignmentView(courseList.get(courseIndex));
            } else {
                Utils.showTempMsg("Error: Selected index is out of bounds!");
            }
        } else {
            switch (input) {
                case "a":
                case "A":
                    openJoinedAssignmentView(term);
                    break;
                case "c":
                case "C":
                    createCourse();
                    break;
                case "u":
                case "U":
                    updateCourse();
                    break;
                case "d":
                case "D":
                    deleteCourse();
                    break;
                case "b":
                case "B":
                    isInCourseView = false;
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
    }

    private void openAssignmentView(Course course) {
        AssignmentView assignmentView = new AssignmentView(course);
        assignmentView.open();
    }

    private void openJoinedAssignmentView(Term term) {
        JoinedAssignmentView joinedAssignmentView = new JoinedAssignmentView(term);
        joinedAssignmentView.open();
    }

    private void createCourse() {
        Utils.clear();
        Utils.printMenuHeader("Course View", Utils.getSubHeaderTitle("Create new Course in Term", termTitle));

        try {
            System.out.print("\n< Code: ");
            String code = scanner.nextLine();

            System.out.print("< Name: ");
            String name = scanner.nextLine();

            Course course = new Course(0, term.getId(), code, name);
            System.out.println("> New " + course);
            System.out.print("\n< Create new course? (y/n): ");

            String confirm = scanner.nextLine();
            if (Utils.confirm(confirm)) {
                courseDAO.createCourse(course);
            }

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
    }

    private void updateCourse() {
        System.out.print("> Enter course # to update: ");
        String input = scanner.nextLine();
        try {
            int courseIndex = Integer.parseInt(input) - 1;
            Course course = courseList.get(courseIndex);

            Utils.clear();
            Utils.printMenuHeader("Course View", Utils.getSubHeaderTitle("Update Course in Term", termTitle));

            System.out.println("\n> Updating " + course);

            System.out.print("< Code: ");
            String code = scanner.nextLine();
            if (code.isBlank()) {
                code = course.getCode();
            }

            System.out.print("< Name: ");
            String name = scanner.nextLine();
            if (name.isBlank()) {
                name = course.getName();
            }

            course.setCode(code);
            course.setName(name);
            System.out.println("> Update to " + course);

            System.out.print("< Proceed with update? (y/n): ");
            String confrim = scanner.nextLine();
            if (Utils.confirm(confrim)) {
                courseDAO.updateCourse(course);
            }
        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
    }

    private void deleteCourse() {
        System.out.print("< Enter course # to delete: ");
        String input = scanner.nextLine();
        try {
            int courseIndex = Integer.parseInt(input) - 1;
            Course course = courseList.get(courseIndex);

            Utils.clear();
            Utils.printMenuHeader("Course View", Utils.getSubHeaderTitle("Delete Course in Term", termTitle));

            System.out.println("\n> Seleted " + course);
            System.out.print("< Delete? (y/n): ");
            String confirm = scanner.nextLine();

            if (Utils.confirmDelete(confirm)) {
                courseDAO.deleteCourse(course.getId());
            }
        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
    }

    private void printCourseList() {

        String hash = Utils.colorizeTableHeader("#", 1);
        String code = Utils.colorizeTableHeader("Code", 3);
        String courseName = Utils.colorizeTableHeader("Course Name", 12);

        printLine();
        printRow(hash, code, courseName);
        printLine();
        if (courseList.size() == 0) {
            System.out.println("** No courses found **");
        }
        int lineNum = 1;
        for (Course c : courseList) {
            printRow(lineNum, c.getCode(), c.getName());
            printLine();
            lineNum++;
        }
    }

    private void printLine() {
        int[] colWidths = { 5, 12, 37 };
        String horizonalLine = Utils.generateHorizontalLine(colWidths);
        System.out.println(horizonalLine);
    }

    private void printRow(String lineNum, String code, String name) {
        String columnFormat = "| %3s | %10s | %35s |";
        String row = String.format(columnFormat, lineNum, code, name);
        System.out.println(row);
    }

    private void printRow(int lineNum, String code, String name) {
        if (code.length() > 10) {
            code = code.substring(0, 8) + "..";
        }
        if (name.length() > 35) {
            name = name.substring(0, 33) + "..";
        }
        printRow(Utils.colorizeLineNum(lineNum), code, name);
    }
}