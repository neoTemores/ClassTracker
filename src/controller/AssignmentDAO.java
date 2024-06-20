package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Assignment;
import model.Assignment.Status;
import utils.DBconnection;
import utils.Utils;

public class AssignmentDAO {
    protected DBconnection connection;

    public AssignmentDAO() {
        this(new DBconnection());
    }

    public AssignmentDAO(DBconnection connection) {
        this.connection = connection;
    }

    public void createAssignment(Assignment assignment) {
        String query = "INSERT INTO assignment(courseId, week, name, status, notes) VALUES(?, ?, ?, ?, ?)";

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setInt(1, assignment.getCourseId());
            statement.setInt(2, assignment.getWeek());
            statement.setString(3, assignment.getName());
            statement.setString(4, assignment.getStatus().toString());
            statement.setString(5, assignment.getNotes());

            statement.executeUpdate();

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
        connection.close();
    }

    public Assignment getAssignment(int id) {
        Assignment assignment = null;
        String query = "SELECT * FROM assignment WHERE id = ?";

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setInt(1, id);

            ResultSet data = statement.executeQuery();

            while (data.next()) {
                assignment = Utils.mapAssignment(data);
            }

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
        connection.close();

        return assignment;
    }

    public List<Assignment> getAssignmentList(int courseId, int weekNum) {
        List<Assignment> assignmentList = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM assignment ");

        if (courseId > 0 && weekNum > 0) {
            sb.append("WHERE courseId = ? AND week = ? ");
        } else if (courseId > 0) {
            sb.append("WHERE courseID = ? ");
        }

        sb.append("ORDER BY week");

        String query = sb.toString();

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);

            if (courseId > 0 && weekNum > 0) {
                statement.setInt(1, courseId);
                statement.setInt(2, weekNum);
            } else if (courseId > 0) {
                statement.setInt(1, courseId);
            }

            ResultSet data = statement.executeQuery();

            while (data.next()) {
                assignmentList.add(Utils.mapAssignment(data));
            }

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
        connection.close();

        return assignmentList;
    }

    public void updateAssignment(Assignment assignment) {
        String query = "UPDATE assignment SET week = ?, name = ?, status = ?, notes = ? WHERE id = ?";

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setInt(1, assignment.getWeek());
            statement.setString(2, assignment.getName());
            statement.setString(3, assignment.getStatus().toString());
            statement.setString(4, assignment.getNotes());
            statement.setInt(5, assignment.getId());

            statement.executeUpdate();

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
        connection.close();
    }

    public void updateAssignmentStatus(Status status, int assignmentId) {
        String query = "UPDATE assignment SET status = ? WHERE id = ?";
        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setString(1, status.toString());
            statement.setInt(2, assignmentId);

            statement.executeUpdate();

        } catch (Exception e) {

            Utils.showTempMsg(e.toString());
        }

        connection.close();
    }

    public void deleteAssignment(int id) {
        String query = "DELETE FROM assignment WHERE id = ?";

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
        connection.close();
    }
}