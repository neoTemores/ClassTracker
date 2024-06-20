package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.JoinedAssignment;
import utils.DBconnection;
import utils.Utils;

public class JoinedAssignmentDAO extends AssignmentDAO {

    public JoinedAssignmentDAO() {
        super();
    }

    public JoinedAssignmentDAO(DBconnection connection) {
        super(connection);
    }

    public List<JoinedAssignment> getJoinedAssignmentList(int termId, int weekNum) {
        List<JoinedAssignment> joinedAssignmentList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(
                "SELECT c.code, c.name AS courseName, a.id AS assignmentId, a.courseId, a.week, a.name AS assignmentName, a.status, a.notes ");
        sb.append("FROM course c JOIN assignment a ");
        sb.append("WHERE c.id = a.courseId AND c.termId = ? ");

        if (weekNum > 0) {
            sb.append("AND a.week = ? ORDER BY c.code");
        } else {
            sb.append("ORDER BY c.code, a.week");
        }

        String query = sb.toString();

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setInt(1, termId);
            if (weekNum > 0) {
                statement.setInt(2, weekNum);
            }

            ResultSet data = statement.executeQuery();

            while (data.next()) {
                joinedAssignmentList.add(Utils.mapJoinedAssignment(data));
            }
        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
        connection.close();
        return joinedAssignmentList;
    }
}
