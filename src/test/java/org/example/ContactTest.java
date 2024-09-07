/**
 * Contact Testing class
 *
 * @author Samuel Walters
 */
package org.example;


import org.example.models.Contact;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ContactTest {

       @Test
        void idTest() {
                Contact contact = new Contact("123", "Samuel", "Walters", "4322344322", "emailstuff");
                assertNotNull(contact.getId());
                assertEquals("123", contact.getId());
        }

        @Test
        void firstNameTest() {
                Contact contact = new Contact("1234", "Sam", "Walters", "4322344322", "emailstuff");
                assertNotNull(contact.getFirstName());
                assertThrows(IllegalArgumentException.class, () -> {
                        contact.setFirstName("");
                });
            assertThrows(IllegalArgumentException.class, () -> {
                contact.setFirstName("12345678900");
            });
        }

        @Test
        void lastNameTest() {
           Contact contact = new Contact("1234", "Sam", "Walters", "4322344322", "emailstuff");
           assertThrows(IllegalArgumentException.class, () -> {
               contact.setLastName("");
           });
           assertThrows(IllegalArgumentException.class, () -> {
               contact.setLastName("2342345234234234234234234234234234234");
           });
        }

        @Test
        void phoneTest() {
            Contact contact = new Contact("1234", "Sam", "Walters", "4322344322", "emailstuff");
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

        @Test
        void addressTest() {
            Contact contact = new Contact("1234", "Sam", "Walters", "4322344322", "emailstuff");
            assertThrows(IllegalArgumentException.class, () -> {
                contact.setAddress("");
            });
            assertThrows(IllegalArgumentException.class, () -> {
                contact.setAddress("123456789004444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444400");
            });
        }
    }

