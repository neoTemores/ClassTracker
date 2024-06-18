package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.TermDAO;
import model.Term;
import utils.Utils;

public class TermView {
    private boolean isInTermView;
    private final TermDAO termDAO;
    private List<Term> termList;
    private final Scanner scanner;

    public TermView() {
        isInTermView = true;
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
        Utils.clear();
        Utils.printMenuHeader("Term View", "Main Menu");

        System.out.println();
        Utils.printMenuItem("1", "Get active term");
        Utils.printMenuItem("2", "View all terms");
        Utils.printMenuItem("C", "Create new term");
        Utils.printMenuItem("Q", "Quit");
        Utils.printMenuSelection();

        String selection = scanner.nextLine().trim();

        switch (selection) {
            case "1":
                showTermsMenu("Active Term", true);
                break;
            case "2":
                showTermsMenu("All Terms", false);
                break;
            case "c":
            case "C":
                createTerm();
                break;
            case "q":
            case "Q":
                scanner.close();
                System.out.println("\n> Goodbye.");
                this.isInTermView = false;
                break;
            default:
                String msg = "Error: Invalid input! '%s'";
                Utils.showTempMsg(String.format(msg, selection));
                break;
        }
    }

    private void showTermsMenu(String menuHeader, boolean isFilterByActive) {
        boolean isInSubMenu = true;

        while (isInSubMenu) {
            Utils.loading();
            termList = termDAO.getTermList(isFilterByActive);
            Utils.clear();
            Utils.printMenuHeader("Term View", menuHeader);

            printTermList();
            String option1 = String.format("Enter term # (1 - %s)", termList.size());
            if (termList.size() == 0) {
                option1 = "No terms found";
            } else if (termList.size() == 1) {
                option1 = "Enter term #";
            }
            System.out.println();
            Utils.printMenuItem("#", option1);
            Utils.printCRUDmenu();
            Utils.printMenuSelection();

            String input = scanner.nextLine().trim();

            if (input.matches("[0-9]+")) {
                // input is a num
                int termIndex = Integer.parseInt(input) - 1;

                if (termIndex >= 0 && termIndex < termList.size()) {
                    openCourseView(termList.get(termIndex));
                } else {
                    Utils.showTempMsg("Error: Selected index is out of bounds!");
                }
            } else {
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
                        isInSubMenu = false;
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

    }

    private void printTermList() {
        String hash = Utils.BLACK_BACKGROUND + Utils.WHITE + " # " + Utils.RESET;
        String year = Utils.BLACK_BACKGROUND + Utils.WHITE + "Year" + Utils.RESET;
        String term = Utils.BLACK_BACKGROUND + Utils.WHITE + "   Term   " + Utils.RESET;
        String isActive = Utils.BLACK_BACKGROUND + Utils.WHITE + " isActive " + Utils.RESET;

        printLine();
        printRow(hash, year, term, isActive);
        printLine();

        int lineNum = 1;
        for (Term t : termList) {
            printRow(lineNum, t.getYear(), t.getName(), t.isActive());
            printLine();
            lineNum++;
        }
    }

    private void openCourseView(Term term) {
        // Mimic the loop structure from this class
        CourseView courseView = new CourseView(term);
        courseView.open();
    }

    private void createTerm() {
        Utils.clear();
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
            System.out.print("\n< Create new term? (y/n): ");

            String confirm = scanner.nextLine();
            if (Utils.confirm(confirm)) {
                termDAO.createTerm(term);
            }
        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
    }

    private void updateTerm() {

        System.out.print("> Enter term # to update: ");
        String input = scanner.nextLine();

        if (input.matches("[0-9]+")) {
            int termIndex = Integer.parseInt(input) - 1;
            if (termIndex >= 0 && termIndex < termList.size()) {
                Utils.clear();
                Utils.printMenuHeader("Term View", "Update Term");

                Term term = termList.get(termIndex);
                System.out.println("> Updating " + term);

                System.out.print("< Name: ");
                String name = scanner.nextLine().trim();
                if (name.isBlank()) {
                    name = term.getName();
                }

                System.out.print("< Year: ");
                String yearStr = scanner.nextLine().trim();
                int year = 0;
                if (yearStr.isBlank() || !yearStr.matches("[0-9]+")) {
                    year = term.getYear();
                } else {
                    year = Integer.parseInt(yearStr);
                }

                System.out.print("< isActive (t/f): ");
                String isActiveStr = scanner.nextLine().trim();
                boolean isActive = term.isActive();
                if (isActiveStr.equalsIgnoreCase("t") || isActiveStr.equalsIgnoreCase("true")) {
                    isActive = true;
                } else if (isActiveStr.equalsIgnoreCase("f") || isActiveStr.equalsIgnoreCase("false")) {
                    isActive = false;
                }

                term.setName(name);
                term.setYear(year);
                term.setActive(isActive);
                System.out.println("> Update to " + term);

                System.out.print("< Proceed with update? (y/n): ");
                String confirm = scanner.nextLine();
                if (Utils.confirm(confirm)) {
                    termDAO.updateTerm(term);
                }
            } else {
                Utils.showTempMsg("Error: Selected index is out of bounds!");
            }

        }
    }

    private void deleteTerm() {

        System.out.print("< Enter term # to delete: ");
        String input = scanner.nextLine();

        if (input.matches("[0-9]+")) {
            int termIndex = Integer.parseInt(input) - 1;
            if (termIndex >= 0 && termIndex < termList.size()) {

                Utils.clear();
                Utils.printMenuHeader("Term View", "Delete Term");

                Term term = termList.get(termIndex);
                System.out.println("\n> Seleted " + term);
                System.out.print("> Delete? (y/n): ");
                String confirm = scanner.nextLine();

                if (Utils.confirm(confirm)) {
                    termDAO.deleteTerm(term.getId());
                }
            } else {
                Utils.showTempMsg("Error: Selected index is out of bounds!");
            }
        }
    }

    private void printRow(String col1, String col2, String col3, String col4) {
        String columnFormat = "| %3s | %4s | %10s | %10s |";

        String row = String.format(columnFormat, col1, col2, col3, col4);
        System.out.println(row);
    }

    private void printRow(int lineNum, int year, String name, boolean isActive) {

        printRow(Utils.colorizeLineNum(lineNum), String.valueOf(year), name, Utils.colorizeBool(isActive));
    }

    private void printLine() {
        String horizonalLine = "+-----+------+------------+------------+";

        System.out.println(horizonalLine);
    }
}
