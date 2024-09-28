/**
 * Original Artifact
 * Contact Service testing class
 *
 * @author Samuel Walters
 *
 * Last update 9/26/24
 */
package org.example;

import org.example.models.Contact;
import org.example.models.ContactService;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ContactServiceTest {
    private ContactService testContactService;

    @BeforeEach
    public void setupTests() {
        testContactService = new ContactService();
        Contact testContact = new Contact("000", "Jim", "Beam", "1234567890", "123 2nd Street, Jamaica");
        testContactService.addContact(testContact);
    }

    @Test
    public void addContactTest() {
        Contact newContact = new Contact("001", "John", "Lolligag", "1234567890", "Address stuff");
        testContactService.addContact(newContact);
        assertNotNull(testContactService.getContact("001"));
        assertEquals(newContact, testContactService.getContact("001"));
        assertThrows(IllegalArgumentException.class, () -> {
            testContactService.getContact("");
        });
        assertNull(testContactService.getContact("134234"));
    }

    @Test
    public void deleteContactTest() {
        testContactService.deleteContact("000");
        // Cannot get a Contact that no longer exists.
        assertNull(testContactService.getContact("000"));
    }
//    9/26 Updated the contact ID.
    @Test
    public void deleteNonExistentContact() {
        assertThrows(IllegalArgumentException.class, () -> {
            testContactService.deleteContact("80088");
        });
    }

//    9/26 Changed the test to increase the amount of characters in the name.
    @Test
    public void updateFirstNameTest() {
        testContactService.updateContactMap("000", "firstname", "Success");
        assertEquals("Success", testContactService.getContact("000").getFirstName());
        //Should not update this name, as it breaks the requirement of 20 chars only
        assertThrows(IllegalArgumentException.class, () -> {
            testContactService.updateContactMap("000", "firstname", "12345678900asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf00");
        });
    }

//    9/26 Changed the test to increase the amount of characters in the name.
    @Test
    public void updateLastNameTest() {
        testContactService.updateContactMap("000", "lastname", "Success");
        assertEquals("Success", testContactService.getContact("000").getLastName());
        //Should not update this name, as it breaks the requirement of 30 chars only
        assertThrows(IllegalArgumentException.class, () -> {
            testContactService.updateContactMap("000", "lastname", "1234561awdfasdfasdfadsfasdfadsfadsf231231231231231231231231231237890000");
        });
    }

//   9/26  Changed the test to increase the amount of characters in the address.
    @Test
    public void updateAddressTest() {
        testContactService.updateContactMap("000", "address", "Success");
        assertEquals("Success", testContactService.getContact("000").getAddress());
        assertThrows(IllegalArgumentException.class, () -> {
            testContactService.updateContactMap("000", "address", "1234567444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444890000");
        });
    }

    @Test
    public void updatePhoneNumberTest() {
        testContactService.updateContactMap("000", "phone", "1111111111");
        assertEquals("1111111111", testContactService.getContact("000").getPhone());
        // If too long will throw and not update
        assertThrows(IllegalArgumentException.class, () -> {
            testContactService.updateContactMap("000", "phone", "1234567444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444890000");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            testContactService.updateContactMap("000", "phone", "1");
        });
    }
}
