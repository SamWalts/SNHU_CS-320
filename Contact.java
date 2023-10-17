/**
 * Contact class
 *
 * @author Samuel Walters
 */


package ContactApp;

public class Contact {
    final private String Id;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    public Contact(String Id, String firstName, String lastName, String phone, String address) {
        if (Id == null || Id.length() > 10) {
            throw new IllegalArgumentException("Invalid ID");
        }

        this.Id = Id;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setPhone(phone);
        this.setAddress(address);
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.length() > 10 || firstName.isEmpty()) {
            throw new IllegalArgumentException("Invalid first name");
        }
        this.firstName = firstName;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.length() != 10) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        this.phone = phone;
    }

    public void setAddress(String address) {
        if (address == null || address.length() > 30 || address.isEmpty()) {
            throw new IllegalArgumentException("Invalid address");
        }
        this.address = address;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.length() > 10 || lastName.isEmpty()) {
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
