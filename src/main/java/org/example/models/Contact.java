/**
 * Contact class
 *
 * @author Samuel Walters
 */

package org.example.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contact {
    final private String Id;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    public Contact(String Id, String firstName, String lastName, String phone, String address) {
        if (Id == null || Id.length() > 20) {
            throw new IllegalArgumentException("Invalid ID");
        }

        this.Id = Id;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setPhone(phone);
        this.setAddress(address);
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.length() > 20 || firstName.isEmpty()) {
            throw new IllegalArgumentException("Invalid first name");
        }
        this.firstName = firstName;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Must enter a phone number.");
        }
        if (phone.length() < 10) {
            throw new IllegalArgumentException("Phone number must be 10 digits.");
        }
        if (phone.length() > 10 ) {
            phone = phone.replaceAll("\\D+", "");
            if (phone.length() > 10 || phone.isEmpty()) {
                throw new IllegalArgumentException("Phone number must contain 10 digits");
            }
            this.phone = phone;
        }
        else {
            this.phone = phone;
        }
    }

    public void setAddress(String address) {
        if (address == null || address.length() > 30 || address.isEmpty()) {
            throw new IllegalArgumentException("Invalid address");
        }
        this.address = address;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.length() > 30 || lastName.isEmpty()) {
            throw new IllegalArgumentException("Invalid Last name");
        }
        this.lastName = lastName;
    }

    public String getId() {
        return Id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
