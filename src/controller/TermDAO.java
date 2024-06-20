package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Term;
import utils.DBconnection;
import utils.Utils;

public class TermDAO {
    private DBconnection connection;

    public TermDAO() {
        this(new DBconnection());
    }

    public TermDAO(DBconnection connection) {
        this.connection = connection;
    }

    public void insertTestData(int amount) {
        for (int i = 0; i < amount; i++) {
            Term term = new Term(0, "Test" + i, i, false);
            createTerm(term);
        }
    }

    public void toggleIsActive(int termId, boolean isActive) {
        String query = "UPDATE term SET isActive = ? WHERE id = ?";

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setBoolean(1, isActive);
            statement.setInt(2, termId);

            statement.executeUpdate();

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
        connection.close();
    }

    public void createTerm(Term term) {
        String query = "INSERT INTO term (name, year, isActive) VALUES(?, ?, ?)";

        connection.open();
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setString(1, term.getName());
            statement.setInt(2, term.getYear());
            statement.setBoolean(3, term.isActive());

            statement.executeUpdate();

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
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

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
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
        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
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

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
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

        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
        connection.close();
    }
}