/**
 * Original Artifact
 * Contact Service class
 *
 * @author Samuel Walters
 *  Update 10/13/2024 To remove all hashmaps and update the class to use a database connection.
 *  Update 10/18/2024 Changed the addContact method to use Integers.
 * Description: This class is used to interact with the database and perform CRUD operations on the contacts table.
 * Will return an observable list of contacts to be used in the JavaFX controller.
 */

package org.example.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.data.DBConnection;

import java.sql.*;

/**
 * 10/6/2024
 * This class is used to interact with the database and perform CRUD operations on the contacts table.
 */
public class ContactService {
    private DBConnection c;

    /**
     * Default constructor
     */
    public ContactService() {
        this.c = new DBConnection();
    }

    /**
     * Constructor that takes in a DBConnection object.
     * @param dbConnection
     */
    public ContactService(DBConnection dbConnection) {
        this.c = dbConnection;
    }

    /**
    @param contact
    Takes in a contact object and adds it to the database.
     */
    public void addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }

        Integer contactId = contact.getId();
        if (contactId != null && contactId > 0) {
            // Update existing contact
            String sql = "INSERT INTO contacts (contactId, firstName, lastName, phone, address) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = c.getDBConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, contactId);
                ps.setString(2, contact.getFirstName());
                ps.setString(3, contact.getLastName());
                ps.setString(4, contact.getPhone());
                ps.setString(5, contact.getAddress());

                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error updating contact", e);
            }
        } else {
            // Insert new contact
            String sql = "INSERT INTO contacts (firstName, lastName, phone, address) VALUES (?, ?, ?, ?)";
            try (Connection conn = c.getDBConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, contact.getFirstName());
                ps.setString(2, contact.getLastName());
                ps.setString(3, contact.getPhone());
                ps.setString(4, contact.getAddress());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        contact.setId(rs.getInt(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error adding contact to the database", e);
            }
        }
    }

    /**
     * Takes in an int contactId and deletes the contact from the database.
     * @param contactId the ID of the contact to delete
     * @throws SQLException if a database access error occurs
     */
    public void deleteContact(Integer contactId) throws SQLException {
        String sql = "DELETE FROM contacts WHERE contactId = ?";
        try (Connection connection = c.getDBConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, contactId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No contact found with id: " + contactId);
            }
        }
    }

    /**
    @param contact
    Takes in a contact object and deletes it from the database.
     */
    public void deleteContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be empty");
        }
        Integer contactId = contact.getId();
        String sql = "DELETE FROM contacts WHERE contactId = ?";
        try {
            c.getDBConnection();
            PreparedStatement pstmt = c.getCon().prepareStatement(sql);
            pstmt.setInt(1, contact.getId());
            pstmt.executeUpdate();
            Integer rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No contact found with id: " + contactId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param contactId
     * @return contact
     * Takes in an int contactId and returns the contact object from the database.
     */
    public Contact getContact(Integer contactId) throws SQLException {
        String sql = "SELECT * FROM contacts WHERE contactId = ?";
        Contact contact = null;
        try (Connection connection = c.getDBConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, contactId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    contact = new Contact(rs.getInt("contactId"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("phone"), rs.getString("address"));
                } else {
                    throw new SQLException("No contact found with id: " + contactId);
                }
            }
        }
        return contact;
    }

    /**
    @param contact
    Takes in a contact object and updates the contact in the database.
 */
    public void updateContact(Contact contact) {
        String sql = "UPDATE contacts SET firstName = ?, lastName = ?, phone = ?, address = ? WHERE contactId = ?";
        try (Connection conn = c.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, contact.getFirstName());
            ps.setString(2, contact.getLastName());
            ps.setString(3, contact.getPhone());
            ps.setString(4, contact.getAddress());
            ps.setInt(5, contact.getId());
            System.out.println(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating contact", e);
        }
    }

    /**
     * @return contactList
     * 9/26 added to support observable list from JavaFX controller.
    *  10/6/2024 Get the list of contacts from the database and return it as an observable list.
    */
    public ObservableList<Contact> getContactsList() {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        try {
            String query = "SELECT contactId, firstName, lastName, phone, address FROM contacts";
            c.getDBConnection();
            Statement st = c.getCon().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Contact contact = new Contact(rs.getInt("contactId"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("phone"), rs.getString("address"));
                contactList.add(contact);
            }
            st.close();
            rs.close();
            c.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contactList;
    }
}