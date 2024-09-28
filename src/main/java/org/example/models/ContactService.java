/**
 * Original Artifact
 * Contact Service class
 *
 * @author Samuel Walters
 *
 *  Last update 9/26/24
 */

package org.example.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Map;
import java.util.HashMap;

public class ContactService {
    public Map<String, Contact> contactMap;

    public ContactService() {
        contactMap = new HashMap<>();
    }

    // TODO: Can remove when DB implemented and use phoneNumber as PK.
//  9/26 Created the generateId method to generate a unique ID for each contact.
    public String generateId() {
        return Integer.toString(contactMap.size() + 1);
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

/* 9/26 added to support observable list from JavaFX controller.
    Retrieve contacts from the hashmap implementation. This will get replaced with the DB.
    @return ObservableList<Contact>
*/
    public ObservableList<Contact> getContactsList() {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        try{
            for (Map.Entry<String, Contact> entry : contactMap.entrySet()) {
                contactList.add(entry.getValue());
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return contactList;
    }
}