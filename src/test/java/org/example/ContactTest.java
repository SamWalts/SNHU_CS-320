/**
 * Original Artifact
 * Contact Object Testing class
 *
 * @author Samuel Walters
 *
 * Last update 10/6/2024
 * This class tests the Contact object. It does not test the database connection.
 */
package org.example;


import org.example.models.Contact;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ContactTest {

       @Test
        void idTest() {
                Contact contact = new Contact(1234, "Samuel", "Walters", "4322344322", "emailstuff");
                assertEquals(1234, contact.getId());
        }

//        9/26 Updated the test to increase the amount of characters in the first name.
        @Test
        void firstNameTest() {
                Contact contact = new Contact(1234, "Sam", "Walters", "4322344322", "emailstuff");
                contact.setFirstName("Jimmy");
                assertEquals("Jimmy", contact.getFirstName());
                assertNotNull(contact.getFirstName());
                assertThrows(IllegalArgumentException.class, () -> {
                        contact.setFirstName("");
                });
            assertThrows(IllegalArgumentException.class, () -> {
                contact.setFirstName("1234567892161651516516516156165165100");
            });
        }

//      9/26 Updated the test to increase the amount of characters in the last name.
        @Test
        void lastNameTest() {
           Contact contact = new Contact(123, "Sam", "Walters", "4322344322", "emailstuff");
           assertThrows(IllegalArgumentException.class, () -> {
               contact.setLastName("");
           });
           assertThrows(IllegalArgumentException.class, () -> {
               contact.setLastName("2342345234234234234234234122345678912345678912345678912345678934234234234");
           });
        }

        @Test
        void phoneTest() {
            Contact contact = new Contact(123, "Sam", "Walters", "4322344322", "emailstuff");
            assertThrows(IllegalArgumentException.class, () -> {
                contact.setPhone("");
            });
            assertThrows(IllegalArgumentException.class, () -> {
               contact.setPhone("1234567890000");
            });
            assertThrows(IllegalArgumentException.class, () -> {
                contact.setPhone("1");
            });
        }

//      9/26 Added this test to specifically check that the contact.setPhone removes all but the numbers.
        @Test
        void phoneInputTest() {
            Contact contact = new Contact(123, "`Sam", "Walters", "4322344322", "emailstuff");
            contact.setPhone("(555)555-5555");
            assertEquals("5555555555", contact.getPhone());
        }

        @Test
        void addressTest() {
            Contact contact = new Contact(1234, "Sam", "Walters", "4322344322", "emailstuff");
            assertThrows(IllegalArgumentException.class, () -> {
                contact.setAddress("");
            });
            assertThrows(IllegalArgumentException.class, () -> {
                contact.setAddress("123456789004444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444400");
            });
        }
    }

