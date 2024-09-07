/**
 * Contact Service class
 *
 * @author Samuel Walters
 */

package org.example.models;

import java.util.Map;
import java.util.HashMap;

public class ContactService {
    public Map<String, Contact> contactMap;

    public ContactService() {
        contactMap = new HashMap<>();
    }

    public void addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be empty");
        }
        String contactId = contact.getId();
        if (contactMap.containsKey(contactId)) {
            throw new IllegalArgumentException("Contact ID: " + contactId + "already exists.");
        }
        contactMap.put(contactId, contact);
    }

    public void deleteContact(String contactId) {
        if (contactId == null) {
            throw new IllegalArgumentException("ContactId cannot be empty");
        }
        if (!contactMap.containsKey(contactId)) {
            throw new IllegalArgumentException("The contactId was not found");
        }
        contactMap.remove(contactId);
    }

    public Contact getContact(String contactId) {
        if (contactId == null || contactId.isEmpty()) {
            throw new IllegalArgumentException("ContactId cannot be empty or null.");
        }
        return contactMap.get(contactId);
    }
    public void updateContactMap(String contactId, String updateField, String updateValue) {
        if (contactId == null || contactId.isEmpty()) {
            throw new IllegalArgumentException("Must have non empty non null contact ID.");
        }
        if (!contactMap.containsKey(contactId)) {
            throw new IllegalArgumentException("The contactId was not found");
        }
        Contact contact = contactMap.get(contactId);

        switch(updateField.toLowerCase()) {
            case "firstname" :
                contact.setFirstName(updateValue);
                    break;
            case "lastname" :
                contact.setLastName(updateValue);
                break;
            case ("phone"):
                contact.setPhone(updateValue);
                break;
            case "address" :
                contact.setAddress(updateValue);
                break;
            default:
                throw new IllegalArgumentException("Updatable field names are: firstname, lastname, phone, and address.");
        }
    }
}