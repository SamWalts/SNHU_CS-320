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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {
    @Mock
    private DBConnection mockDBConnection;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private ContactService testContactService;

    @BeforeEach
    public void setupTests() throws Exception {
        when(mockDBConnection.getDBConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(1)).thenReturn(1);

        Contact testContact = new Contact(1, "Jim", "Beam", "1234567890", "123 2nd Street, Jamaica State, 45555, United States");
        testContactService.addContact(testContact);
    }

    @Test
    public void addContactTest() throws Exception {
        Contact newContact = new Contact(1, "John", "Lolligag", "1234567890", "Address stuff");
        testContactService.addContact(newContact);

        verify(mockPreparedStatement, times(2)).executeUpdate();
        assertNotNull(testContactService.getContact(1));
    }

    @Test
    public void deleteContactTest() throws Exception {
        doNothing().when(mockPreparedStatement).executeUpdate();
        testContactService.deleteContact(1);

        verify(mockPreparedStatement, times(1)).executeUpdate();
        assertNull(testContactService.getContact(1));
    }

    @Test
    public void deleteNonExistentContact() {
        assertThrows(IllegalArgumentException.class, () -> {
            testContactService.deleteContact(80088);
        });
    }

    @Test
    public void updateFirstNameTest() throws Exception {
        doNothing().when(mockPreparedStatement).executeUpdate();
        testContactService.updateContact(new Contact(1, "Test", "Mcgee", "1234567899", "Long Address"));

        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void updateLastNameTest() throws Exception {
        doNothing().when(mockPreparedStatement).executeUpdate();
        testContactService.updateContact(new Contact(1, "Jim", "Success", "1234567890", "123 2nd Street, Jamaica State, 45555, United States"));

        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void updateAddressTest() throws Exception {
        doNothing().when(mockPreparedStatement).executeUpdate();
        testContactService.updateContact(new Contact(1, "Jim", "Beam", "1234567890", "Success"));

        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void updatePhoneNumberTest() throws Exception {
        doNothing().when(mockPreparedStatement).executeUpdate();
        testContactService.updateContact(new Contact(1, "Jim", "Beam", "1111111111", "123 2nd Street, Jamaica State, 45555, United States"));

        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
}