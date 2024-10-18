/**
 * Original Artifact-- ENHANCEMENT
 * Contact Service testing class
 * Author: Samuel Walters
 * Last update 10/18/2024
 * Each test is designed to test a different method in the ContactService class.
 * The tests are designed to test the CRUD operations of the ContactService class.
 */
package org.example;

import javafx.collections.ObservableList;
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
     * Uses standard login for test database that will be destroyed on test completion.
     */
    @Container
    public MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private DBConnection dbConnection;
    private ContactService testContactService;

    /**
     * 10/13/2024
     * Setup the test environment in a docker environment.
     * Each test will have a single contact in the database to begin with.
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

        Contact testContact = new Contact("Jim", "Brom", "123-456-7890", "123 2nd Street, Jamaica State, 45555, United States");
        testContactService.addContact(testContact);
    }

    /**
     * 10/16/2024
     * Test to add a contact to the database
     * Adds with contactService, then checks test database for the added contact
     * @throws Exception
     */
    @Test
    public void addContactTest() throws Exception {
        Contact newContact = new Contact(10, "John", "Lolligag", "1234567890", "Address stuff");
        testContactService.addContact(newContact);

        try (Connection connection = dbConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE contactId = ?")) {
            statement.setInt(1, 10);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next());
                assertEquals("John", resultSet.getString("firstName"));
            }
        }
        // Test for adding null contact
        assertThrows(IllegalArgumentException.class, () -> {
            testContactService.addContact(null);
        });
    }

    /**
     * 10/16/2024
     * Test to get a list of contacts from the database
     * Adds contacts to the database, then retrieves the list of contacts
     * @throws Exception
     */
    @Test
    public void getContactsListTest() throws Exception {
        // Add some contacts to the database
        Contact contact1 = new Contact("John", "Doe", "1234567890", "123 Main St");
        Contact contact2 = new Contact("Jane", "Smith", "0987654321", "456 Elm St");
        testContactService.addContact(contact1);
        testContactService.addContact(contact2);

        // Retrieve the list of contacts. Uses javaFX ObservableList.
        ObservableList<Contact> contactsList = testContactService.getContactsList();

        // Verify the list size returned
        assertEquals(3, contactsList.size());

        // Verify the details of the First added contact
        Contact retrievedContact1 = contactsList.get(1);
        assertEquals("John", retrievedContact1.getFirstName());
        assertEquals("Doe", retrievedContact1.getLastName());
        assertEquals("1234567890", retrievedContact1.getPhone());
        assertEquals("123 Main St", retrievedContact1.getAddress());

        // Verify the details of the second added contact
        Contact retrievedContact2 = contactsList.get(2);
        assertEquals("Jane", retrievedContact2.getFirstName());
        assertEquals("Smith", retrievedContact2.getLastName());
        assertEquals("0987654321", retrievedContact2.getPhone());
        assertEquals("456 Elm St", retrievedContact2.getAddress());
    }

    /**
     * 10/16/2024
     * Test to add a contact to the database with null values
     */
    @Test
    public void addContactTestNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            testContactService.addContact(null);
        });
    }

    /**
     * 10/16/2024
     * Test to delete a contact from the database
     * @throws Exception
     */
    @Test
    public void deleteContactTestContactID() throws Exception {
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
     * 10/16/2024
     * Test to delete a contact from the database using a contact object
     * @throws Exception
     */
    @Test
    public void deleteContactTestObject() throws Exception {
        testContactService.deleteContact(new Contact(1, "Jim", "Doe", "123-456-7890", "123 2nd Street, Jamaica State, 45555, United States"));

        try (Connection connection = dbConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE contactId = ?")) {
            statement.setInt(1, 1);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertFalse(resultSet.next());
            }
        }
    }

    /**
     * 10/18/2024
     * Test for deleting a contact that does not exist
     * @throws Exception
     */
    @Test
    public void deleteNonExistentContact() {
        assertThrows(SQLException.class, () -> {
            testContactService.deleteContact(800000);
        });
    }

    /**
     * 10/16/2024
     * Test to update the first name of a contact
     * @throws Exception
     */
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

    /**
     * 10/15/2024
     * Test to update the last name of a contact
     * @throws Exception
     */
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

    /**
     * 10/18/2024
     * Test to update the address of a contact
     * @throws Exception
     */
    @Test
    public void updateAddressTest() throws Exception {
//        Make a new contact object to update the address
        Contact testContact = new Contact(1, "Jim", "Bromide", "1234567890", "Not Good");
//        Call Contact to update the address
        testContact.setAddress("Success");
//        Call contactService to update the contact in DB
        testContactService.updateContact(testContact);

//        Check in the DB to see if the address was updated
        try (Connection connection = dbConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE contactId = ?")) {
            statement.setInt(1, 1);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next());
                assertEquals("Success", resultSet.getString("address"));
            }
        }
    }

    /**
     * 10/18/2024
     * Test to update the phone number of a contact
     * @throws Exception
     */
    @Test
    public void updatePhoneNumberTest() throws Exception {
        testContactService.updateContact(new Contact(1, "Jim", "Gaffergand", "1111111111", "123 2nd Street, Jamaica State, 45555, United States"));

        try (Connection connection = dbConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE contactId = ?")) {
            statement.setInt(1, 1);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next());
                assertEquals("1111111111", resultSet.getString("phone"));
            }
        }
    }

    /**
     * 10/18/2024
     * Test to update a contact. This test is to check if the contact is updated correctly.
     * @throws SQLException
     */
    @Test
    public void getContactTest() throws SQLException {
        //Contact that I am inserting using the contactService.addContact method
        testContactService.addContact(new Contact(5, "Sarah", "Doe", "1234567890", "123 2nd Street, Jamaica State, 45555, United States"));
        Contact contact = testContactService.getContact(5);
        assertNotNull(contact);
        //Checking to see if the contact that I inserted is the same as the contact that I am retrieving
        Contact retrievedContact = testContactService.getContact(5);

        // Asserts for each field that should match what is retrieved. If they do not match, the test will fail.
        assertEquals("Sarah", retrievedContact.getFirstName());
        assertEquals("Doe", retrievedContact.getLastName());
        assertEquals("1234567890", retrievedContact.getPhone());
        assertEquals("123 2nd Street, Jamaica State, 45555, United States", retrievedContact.getAddress());
    }

    /**
     * 10/18/2024
     * Test to ensure that an exception is thrown when trying to get a contact that does not exist.
     */
    @Test
    public void getFakeContact() {
        assertThrows(SQLException.class, () -> {
            testContactService.getContact(664465454);
        });
    }
}