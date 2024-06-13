import java.util.List;

public class TrackerAppTests {

    public static void testChainDelete() {
        TermDAO termDAO = new TermDAO();
        // termDAO.createTerm(new Term());

        CourseDAO courseDAO = new CourseDAO();
        Course course = new Course();
        course.setTermId(2);
        // courseDAO.createCourse(course);

        AssignmentDAO assignmentDAO = new AssignmentDAO();
        Assignment assignment = new Assignment();
        assignment.setCourseId(2);
        // assignmentDAO.createAssignment(assignment);

        termDAO.deleteTerm(0);
    }

    public static void testAssignment() {
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        System.out.println("\nGet list of assignments");
        List<Assignment> assignmentList = assignmentDAO.getAssignmentList();
        for (Assignment a : assignmentList) {
            System.out.println(a);
        }

        System.out.println("\nCreate new assignment:");
        Assignment assignment = new Assignment(0, 1, 1, "Discussions",
                Assignment.Status.notStarted, "0/3");
        assignmentDAO.createAssignment(assignment);
        assignmentList = assignmentDAO.getAssignmentList();
        for (Assignment a : assignmentList) {
            System.out.println(a);
        }

        System.out.println("\nUpdate assignment");

        Assignment oldAssignment = assignmentList.get(assignmentList.size() - 1);
        Assignment newAssignment = new Assignment(oldAssignment.getId(), oldAssignment.getCourseId(),
                5, "New Name", Assignment.Status.inProgress, "updated notes");

        assignmentDAO.updateAssignment(newAssignment);

        System.out.println("\nGet assignment by id:");
        System.out.println(assignmentDAO.getAssignment(newAssignment.getId()));

        System.out.println("\nDelete one assignment:");
        assignmentDAO.deleteAssignment(newAssignment.getId());

        assignmentList = assignmentDAO.getAssignmentList();
        for (Assignment a : assignmentList) {
            System.out.println(a);
        }
    }

    public static void testCourse() {
        CourseDAO courseDAO = new CourseDAO();

        System.out.println("\nGet all courses");
        List<Course> courseList = courseDAO.getCourseList();
        for (Course c : courseList) {
            System.out.println(c);
        }
        System.out.println("\nCreating new course:");
        courseDAO.createCourse(new Course(0, 1, "testCode", "testName"));

        courseList = courseDAO.getCourseList();
        for (Course c : courseList) {
            System.out.println(c);
        }

        System.out.println("\nGet one course:");
        Course course1 = courseDAO.getCourse(courseList.get(courseList.size() - 1).getId());
        System.out.println(course1);

        System.out.println("\nUpdating course");
        courseDAO.updateCourse(new Course(course1.getId(), course1.getTermId(), "newCode", "newName"));
        System.out.println("\nSee updated course");
        System.out.println(courseDAO.getCourse(course1.getId()));

        System.out.println("delete course:");
        courseDAO.deleteCourse(course1.getId());

        courseList = courseDAO.getCourseList();
        for (Course c : courseList) {
            System.out.println(c);
        }
    }

    public static void testTerm() {
        TermDAO termDAO = new TermDAO();

        System.out.println("\nGet all terms:");
        List<Term> termList = termDAO.getTermList(false);
        for (Term term : termList) {
            System.out.println(term);
        }

        System.out.println("\nInserting new term:");
        termDAO.createTerm(new Term(0, "test", 2024, false));

        System.out.println("\nGet all terms again:");
        termList = termDAO.getTermList(false);
        for (Term term : termList) {
            System.out.println(term);
        }

        Term termToUpdate = termList.get(termList.size() - 1);
        System.out.println("\nTerm to update is:");
        System.out.println(termToUpdate);

        System.out.println("\nPerform update: ");
        Term updatedTerm = new Term(termToUpdate.getId(), "new name", termToUpdate.getYear() - 10,
                !termToUpdate.isActive());
        termDAO.updateTerm(updatedTerm);

        System.out.println("\nSee updated term:");
        System.out.println(termDAO.getTerm(updatedTerm.getId()));

        System.out.println("\nDelete term:");
        termDAO.deleteTerm(updatedTerm.getId());

        System.out.println("\nGet all terms again:");
        termList = termDAO.getTermList(false);
        for (Term term : termList) {
            System.out.println(term);
        }
    }
}