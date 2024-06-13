import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    public boolean open() {

        while (isInCourseView) {
            mainMenu();
        }

        return false;
    }

    private void mainMenu() {
        // term.getId()
        courseList = courseDAO.getCourseList(0);

        Utils.printMenuHeader("Course View", generateSubheaderText("Main Menu"));

        printCourseList();

        String option1 = String.format("Enter course # (1 - %s)", courseList.size());
        System.out.println();
        Utils.printMenuItem("#", option1);
        Utils.printMenuItem("C", "Create");
        Utils.printMenuItem("U", "Update");
        Utils.printMenuItem("D", "Delete");
        Utils.printMenuItem("B", "Back");
        Utils.printMenuItem("Q", "Quit");
        Utils.printMenuSelection();

        String input = scanner.nextLine();

        // todo: switch over input
        isInCourseView = false;
    }

    private String generateSubheaderText(String subheader) {
        String subheaderFormat = "%s (%s)";
        return String.format(subheaderFormat, subheader, termTitle);
    }

    private void printCourseList() {
        printLine();
        printRow("#", "Code", "Course Name");
        printLine();

        int lineNum = 1;
        for (Course c : courseList) {
            printRow(lineNum, c.getCode(), c.getName());
            printLine();
            lineNum++;
        }
    }

    private void printLine() {
        String horizonalLine = "+---+------------+-----------------------------------+";
        System.out.println(horizonalLine);
    }

    private void printRow(String col1, String col2, String col3) {
        String columnFormat = "| %1s | %10s | %33s |";
        String row = String.format(columnFormat, col1, col2, col3);
        System.out.println(row);
    }

    private void printRow(int lineNum, String code, String name) {
        if (name.length() > 30) {
            name = name.substring(0, 30) + "...";
        }
        printRow(String.valueOf(lineNum), code, name);
    }
}