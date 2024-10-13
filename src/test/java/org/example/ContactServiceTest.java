/**
 * Original Artifact
 * Contact Service testing class
 *
 * Last update 10/6/2024
 * Mocking library Mockito is used to test the ContactService class.
 * This class mocks the database connection that is used in the ContactService class.
 */
package org.example;

import org.example.data.DBConnection;
import org.example.models.Contact;
import org.example.models.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class ContactServiceTest {

    /**
     * Testcontainers is used to create a docker container for the test database.
     * KEEP IN MIND: This will need to be updated inline with the production database.
     */
    @Container
    public MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private DBConnection dbConnection;
    private ContactService testContactService;

    /**
     * Setup the test environment in a docker environment.
     * This is done to provide a clean database for each test.
     * @throws Exception
     */
    @BeforeEach
    public void setupTests() throws Exception {
        dbConnection = new DBConnection(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword());
        testContactService = new ContactService(dbConnection);

        try (Connection connection = dbConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS contacts (contactId INT PRIMARY KEY AUTO_INCREMENT, firstname VARCHAR(45), lastname VARCHAR(45), phone VARCHAR(20), address VARCHAR(75))")) {
            statement.execute();
        }

        Contact testContact = new Contact("Jim", "Beam", "123-456-7890", "123 2nd Street, Jamaica State, 45555, United States");
        testContactService.addContact(testContact);
    }


    /**
     * Test to add a contact to the database
     * Adds with contactService, then checks test database for the added contact
     * @throws Exception
     */
    @Test
    public void addContactTest() throws Exception {
        Contact newContact = new Contact(2, "John", "Lolligag", "1234567890", "Address stuff");
        testContactService.addContact(newContact);

        try (Connection connection = dbConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE contactId = ?")) {
            statement.setInt(1, 2);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next());
                assertEquals("John", resultSet.getString("firstName"));
            }
        }
    }

    /**
     * Test to delete a contact from the database
     * @throws Exception
     */
    @Test
    public void deleteContactTest() throws Exception {
        testContactService.deleteContact(1);

        try (Connection connection = dbConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE contactId = ?")) {
            statement.setInt(1, 1);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertFalse(resultSet.next());
            }
        }
    }

    /**
     * Test for deleting a contact that does not exist
     * @throws Exception
     */
    @Test
    public void deleteNonExistentContact() {
        assertThrows(SQLException.class, () -> {
            testContactService.deleteContact(80000);
        });
    }

    @Test
    public void updateFirstNameTest() throws Exception {
        testContactService.updateContact(new Contact(1, "Test", "Mcgee", "1234567899", "Long Address"));

        try (Connection connection = dbConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE contactId = ?")) {
            statement.setInt(1, 1);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next());
                assertEquals("Test", resultSet.getString("firstname"));
            }
        }
    }

    @Test
    public void updateLastNameTest() throws Exception {
        testContactService.updateContact(new Contact(1, "Jim", "Success", "1234567890", "123 2nd Street, Jamaica State, 45555, United States"));

        try (Connection connection = dbConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE contactId = ?")) {
            statement.setInt(1, 1);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next());
                assertEquals("Success", resultSet.getString("lastname"));
            }
        }
    }

    @Test
    public void updateAddressTest() throws Exception {
        testContactService.updateContact(new Contact(1, "Jim", "Beam", "1234567890", "Success"));

        try (Connection connection = dbConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE contactId = ?")) {
            statement.setInt(1, 1);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next());
                assertEquals("Success", resultSet.getString("address"));
            }
        }
    }

    @Test
    public void updatePhoneNumberTest() throws Exception {
        testContactService.updateContact(new Contact(1, "Jim", "Beam", "1111111111", "123 2nd Street, Jamaica State, 45555, United States"));

        try (Connection connection = dbConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE contactId = ?")) {
            statement.setInt(1, 1);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next());
                assertEquals("1111111111", resultSet.getString("phone"));
            }
        }
    }
}