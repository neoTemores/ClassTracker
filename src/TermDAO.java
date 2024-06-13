import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TermDAO {
    private DBconnection connection;

    public TermDAO() {
        this(new DBconnection());
    }

    public TermDAO(DBconnection connection) {
        this.connection = connection;
    }

    public void createTerm(Term term) {
        String query = "INSERT INTO term (name, year, isActive) VALUES(?, ?, ?)";

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setString(1, term.getName());
            statement.setInt(2, term.getYear());
            statement.setBoolean(3, term.isActive());

            int res = statement.executeUpdate();
            System.out.println("\n> New Term Successfully Created!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    public Term getTerm(int id) {
        Term term = null;
        String query = "SELECT * FROM term WHERE id = ?";

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setInt(1, id);

            ResultSet data = statement.executeQuery();

            while (data.next()) {
                term = Utils.mapTerm(data);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();

        return term;
    }

    public List<Term> getTermList(Boolean isFilterByActive) {
        List<Term> termList = new ArrayList<>();
        String query = "SELECT * FROM term";
        if (isFilterByActive) {
            query += " WHERE isActive = true";
        }
        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);

            ResultSet data = statement.executeQuery();

            while (data.next()) {
                termList.add(Utils.mapTerm(data));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();

        return termList;
    }

    public void updateTerm(Term term) {
        String query = "UPDATE term SET name = ?, year = ?, isActive = ? WHERE id = ?";

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setString(1, term.getName());
            statement.setInt(2, term.getYear());
            statement.setBoolean(3, term.isActive());
            statement.setInt(4, term.getId());

            statement.executeUpdate();
            System.out.println("> Term Successfully Updated!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        connection.close();
    }

    public void deleteTerm(int id) {
        String query = "DELETE FROM term WHERE ID = ?";

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setInt(1, id);

            statement.executeUpdate();
            System.out.println("> Term Successfully Deleted!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        connection.close();
    }
}