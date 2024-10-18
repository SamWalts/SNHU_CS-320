/**
 * Original Artifact
 * Contact Class
 *
 * @author Samuel Walters
 *
 * Last update 10/18/24 Changed Id from int to Integer to allow for null values.
 * This class is used to create a contact object.
 * The contact will be created without Id and will be assigned by the database except for testing.
 */
package org.example.models;

public class Contact {
    private Integer Id;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;


//    10/2 Created new constructor to allow for the object to be created without an ID.
//    When the object is then queried to populate a table, it will return the ID in the below constructor.
    public Contact(String firstName, String lastName, String phone, String address) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setPhone(phone);
        this.setAddress(address);
    }

//    10/2 Created new constructor to allow for an ID to be passed in.
    public Contact(Integer Id, String firstName, String lastName, String phone, String address) {
        this.Id = Id;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setPhone(phone);
        this.setAddress(address);
    }

    //    9/26 Updated the firstName increasing the allowed length.
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.length() > 20 || firstName.isEmpty()) {
            throw new IllegalArgumentException("Invalid first name");
        }
        this.firstName = firstName;
    }

/**
* 9/26 Updated the phone number to allow for more than 10 digits.
* Added a regex to remove any non-digit characters from the phone number.
* */
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

// 10/4 Updated the address to allow for 75 characters.
    public void setAddress(String address) {
        if (address == null || address.length() > 75 || address.isEmpty()) {
            throw new IllegalArgumentException("Invalid address");
        }
        this.address = address;
    }

//    9/26 Updated the lastName increasing the allowed length.
    public void setLastName(String lastName) {
        if (lastName == null || lastName.length() > 30 || lastName.isEmpty()) {
            throw new IllegalArgumentException("Invalid Last name");
        }
        this.lastName = lastName;
    }

    public Integer getId() {
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

    public void setId(Integer contactId) {
        this.Id = contactId;
    }
}
