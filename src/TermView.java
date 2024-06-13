import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TermView {
    private final String columnFormat;
    private final String horizonalLine;
    private final TermDAO termDAO;
    private List<Term> termList;
    private Boolean isInTermView;
    private final Scanner scanner;

    public TermView() {
        isInTermView = true;
        this.columnFormat = "| %1s | %4s | %10s | %10s |";
        this.horizonalLine = "+---+------+------------+------------+";
        termDAO = new TermDAO();
        termList = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public boolean open() {

        while (isInTermView) {
            mainMenu();
        }

        return false;
    }

    private void mainMenu() {
        Utils.printMenuHeader("Term View", "Main Menu");

        System.out.println();
        Utils.printMenuItem("1", "See active terms");
        Utils.printMenuItem("2", "View all terms");
        Utils.printMenuItem("C", "Create new term");
        Utils.printMenuItem("Q", "Quit");
        Utils.printMenuSelection();

        String selection = scanner.nextLine();

        switch (selection) {
            case "1":
                termList = termDAO.getTermList(true);
                showTermsMenu("Active Terms");
                break;
            case "2":
                termList = termDAO.getTermList(false);
                showTermsMenu("All Terms");
                break;
            case "c":
            case "C":
                createTerm();
                break;
            default:
                this.isInTermView = false;
                scanner.close();
        }
    }

    // private void printHeader() {
    // System.out.println("\n----------+");
    // System.out.println("Term View |");
    // System.out.println("----------+");
    // }

    private void showTermsMenu(String menuHeader) {
        // printHeader();
        // System.out.println(menuHeader);
        // String underline = "+";
        // for (int i = 0; i < menuHeader.length() - 1; i++) {
        // underline = "-" + underline;
        // }
        // System.out.println(underline);

        Utils.printMenuHeader("Term View", menuHeader);

        printTermList();
        String option1 = "\n* #. Enter term # (1 - %s)";
        System.out.println(String.format(option1, termList.size()));
        System.out.println("* C. Create");
        System.out.println("* U. Update");
        System.out.println("* D. Delete");
        System.out.println("* B. Back");
        System.out.println("* Q. Quit");

        System.out.print("\n< Selection: ");
        String input = scanner.nextLine();

        if (input.matches("[^0-9]+")) {
            switch (input) {
                case "c":
                case "C":
                    createTerm();
                    break;
                case "u":
                case "U":
                    updateTerm();
                    break;
                case "d":
                case "D":
                    deleteTerm();
                    break;
                case "b":
                case "B":
                    break;
                default:
                    scanner.close();
                    System.exit(0);
                    break;
            }
        } else {
            // input.matches("[0-9]+"
            int termIndex = Integer.parseInt(input) - 1;

            if (termIndex >= 0 && termIndex < termList.size()) {
                openCourseView(termList.get(termIndex));
            } else {
                System.out.println("> Selected index is out of bounds");
            }
        }
    }

    private void printTermList() {

        printLine();
        printRow("#", "year", "Term", "isActive");
        printLine();

        int lineNum = 1;
        for (Term t : termList) {
            printRow(lineNum, t.getYear(), t.getName(), t.isActive());
            printLine();
            lineNum++;
        }
    }

    private void openCourseView(Term term) {
        System.out.println("View courses in " + term);
        /*
         * //Mimic the loop structure from this class
         * CourseView courseView = new CourseView(term);
         * courseView.open();
         */
    }

    private void createTerm() {
        // printHeader();
        // System.out.println("Create new term |");
        // System.out.println("----------------+");

        Utils.printMenuHeader("Term View", "Create New Term");

        try {
            System.out.print("< Year: ");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.print("< Name: ");
            String name = scanner.nextLine();
            System.out.print("< isActive (t/f): ");
            boolean isActive = scanner.nextLine().equalsIgnoreCase("t");

            Term term = new Term(0, name, year, isActive);
            System.out.println("> New " + term);
            System.out.print("< Create new term? (y/n): ");

            String confrim = scanner.nextLine();
            if (Utils.confrim(confrim)) {
                termDAO.createTerm(term);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void updateTerm() {
        // printHeader();
        // System.out.println("-> Update Term |");
        // System.out.println("---------------+");

        Utils.printMenuHeader("Term View", "Update Term");

        System.out.print("> Enter Term # to update: ");
        String input = scanner.nextLine();

        if (input.matches("[0-9]+")) {
            int termIndex = Integer.parseInt(input) - 1;
            if (termIndex >= 0 && termIndex < termList.size()) {
                Term term = termList.get(termIndex);
                System.out.println("> Updating " + term);

                System.out.print("> Name: ");
                String name = scanner.nextLine();
                if (name.trim().length() == 0) {
                    name = term.getName();
                }

                System.out.print("> Year: ");
                String yearStr = scanner.nextLine();
                int year = 0;
                if (yearStr.trim().length() == 0) {
                    year = term.getYear();
                } else {
                    year = Integer.parseInt(yearStr);
                }

                System.out.print("> isActive (t/f): ");
                String isActiveStr = scanner.nextLine();
                boolean isActive;
                if (isActiveStr.equalsIgnoreCase("t") || isActiveStr.equalsIgnoreCase("true")) {
                    isActive = true;
                } else if (isActiveStr.equalsIgnoreCase("f") || isActiveStr.equalsIgnoreCase("false")) {
                    isActive = false;
                } else {
                    isActive = term.isActive();
                }

                term.setName(name);
                term.setYear(year);
                term.setActive(isActive);
                System.out.println("> Update to " + term);

                System.out.print("< Proceed with update? (y/n): ");
                String confirm = scanner.nextLine();
                if (Utils.confrim(confirm)) {
                    termDAO.updateTerm(term);
                }
            }

        }
    }

    private void deleteTerm() {
        // printHeader();
        // System.out.println("-> Delete Term |");
        // System.out.println("---------------+");
        Utils.printMenuHeader("Term View", "Delete Term");

        System.out.print("< Enter term # to delete: ");
        String input = scanner.nextLine();

        if (input.matches("[0-9]+")) {
            int termIndex = Integer.parseInt(input) - 1;
            if (termIndex >= 0 && termIndex < termList.size()) {
                Term term = termList.get(termIndex);
                System.out.println("\n> Seleted " + term);
                System.out.print("> Delete? (y/n): ");
                String confirm = scanner.nextLine();

                if (Utils.confrim(confirm)) {
                    termDAO.deleteTerm(term.getId());
                }
            }
        }
    }

    private void printRow(String col1, String col2, String col3, String col4) {
        String row = String.format(columnFormat, col1, col2, col3, col4);
        System.out.println(row);
    }

    private void printRow(int lineNum, int year, String name, boolean isActive) {
        printRow(String.valueOf(lineNum), String.valueOf(year), name, String.valueOf(isActive));
    }

    private void printLine() {
        System.out.println(horizonalLine);
    }
}
