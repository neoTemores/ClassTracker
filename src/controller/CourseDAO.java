package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Course;
import utils.DBconnection;
import utils.Utils;

public class CourseDAO {
    private DBconnection connection;

    public CourseDAO() {
        this(new DBconnection());
    }

    public CourseDAO(DBconnection connection) {
        this.connection = connection;
    }

    public void createCourse(Course course) {
        String query = "INSERT INTO course (termId, code, name) VALUES(?, ?, ?)";

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setInt(1, course.getTermId());
            statement.setString(2, course.getCode());
            statement.setString(3, course.getName());

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        connection.close();
    }

    public Course getCourse(int id) {
        Course course = null;
        String query = "SELECT * FROM course WHERE id = ?";

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setInt(1, id);

            ResultSet data = statement.executeQuery();

            while (data.next()) {
                course = Utils.mapCourse(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        connection.close();

        return course;
    }

    public List<Course> getCourseList(int termId) {
        List<Course> courseList = new ArrayList<>();
        String query = "SELECT * FROM course";
        if (termId > 0) {
            query += " WHERE termId = ?";
        }

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            if (termId > 0) {
                statement.setInt(1, termId);
            }
            ResultSet data = statement.executeQuery();

            while (data.next()) {
                courseList.add(Utils.mapCourse(data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connection.close();

        return courseList;
    }

    public void updateCourse(Course course) {
        String query = "UPDATE course SET code = ?, name = ? WHERE id = ?";

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setString(1, course.getCode());
            statement.setString(2, course.getName());
            statement.setInt(3, course.getId());

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        connection.close();
    }

    public void deleteCourse(int id) {
        String query = "DELETE FROM course WHERE id = ?";

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        connection.close();
    }
}