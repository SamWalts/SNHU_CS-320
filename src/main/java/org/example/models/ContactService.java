/**
 * Original Artifact
 * Contact Service class
 *
 * @author Samuel Walters
 *  Last update 10/6/2024
 *
 *  This class is used to interact with the database and perform CRUD operations on the contacts table.
 */

package org.example.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.data.DBConnection;

import java.sql.*;
import java.util.Map;
import java.util.HashMap;

public class ContactService {
    private DBConnection c = new DBConnection();

    public Map<String, Contact> contactMap;

    public ContactService() {
        contactMap = new HashMap<>();
    }

    /*
    @param contact
    Takes in a contact object and adds it to the database.
     */
    public void addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be empty");
        }
        try {
            c.getDBConnection();
            String sql = "INSERT INTO contacts (firstName, lastName, phone, address) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = c.getCon().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, contact.getFirstName());
            ps.setString(2, contact.getLastName());
            ps.setString(3, contact.getPhone());
            ps.setString(4, contact.getAddress());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                contact.setId(rs.getInt(1));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding contact to the database", e);
        }
    }

    /*
    @param contactId
    Takes in an integer contactId and deletes the contact from the database.
     */
    public void deleteContact(Integer contactId) {
        String sql = "DELETE FROM contacts WHERE contactId = ?";
        try {
            c.getDBConnection();
            PreparedStatement pstmt = c.getCon().prepareStatement(sql);
            pstmt.setInt(1, contactId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    @param contact
    Takes in a contact object and deletes it from the database.
     */
    public void deleteContact(Contact contact) {
        String sql = "DELETE FROM contacts WHERE contactId = ?";
        try {
            c.getDBConnection();
            PreparedStatement pstmt = c.getCon().prepareStatement(sql);
            pstmt.setInt(1, contact.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    @return contact
    Takes in an integer contactId and returns the contact object from the database.
     */
    public Contact getContact(Integer contactId) {
        if (contactId == null) {
            throw new IllegalArgumentException("ContactId cannot be empty or null.");
        }
        String sql = "SELECT * FROM contacts WHERE contactId = ?";
        Contact contact = null;
        try {
            c.getDBConnection();
            PreparedStatement pstmt = c.getCon().prepareStatement(sql);
            pstmt.setInt(1, contactId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                contact = new Contact(rs.getInt("contactId"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("phone"), rs.getString("address"));
                return contact;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contact;
    }
/*
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
    /* 9/26 added to support observable list from JavaFX controller.
        Get the list of contacts from the database and return it as an observable list.
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