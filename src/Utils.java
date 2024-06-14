import java.sql.ResultSet;

public class Utils {

    public static void clear() {
        System.out.println("\033[H\033[2J");
    }

    public static void showTempMsg(String msg) {
        System.out.println("> ** " + msg + " **");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printMenuHeader(String header, String subheader) {
        String formattedHeader = String.format("%s |", header);
        String formattedSubheader = String.format("-> %s |", subheader);

        String topLine = generateUnderline(formattedHeader);
        String middleLine = generateUnderline(formattedHeader);
        String bottomLine = generateUnderline(formattedSubheader);

        int lengthDiff = formattedSubheader.length() - formattedHeader.length();

        for (int i = 0; i < lengthDiff; i++) {
            middleLine = "-" + middleLine;
        }

        System.out.println(topLine);
        System.out.println(formattedHeader);
        System.out.println(middleLine);
        System.out.println(formattedSubheader);
        System.out.println(bottomLine);
    }

    public static String generateSubHeaderTitle(String subHeader, String subTitle) {
        String subheaderFormat = "%s (%s)";
        return String.format(subheaderFormat, subHeader, subTitle);
    }

    private static String generateUnderline(String title) {
        String underline = "+";
        for (int i = 0; i < title.length() - 1; i++) {
            underline = "-" + underline;
        }
        return underline;
    }

    public static void printCRUDmenu() {
        printMenuItem("C", "Create");
        printMenuItem("U", "Update");
        printMenuItem("D", "Delete");
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
        return input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes");
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            Assignment.Status status = Assignment.Status.valueOf(data.getString("status"));
            String notes = data.getString("notes");
            assignment = new Assignment(id, courseId, week, name, status, notes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return assignment;
    }
}