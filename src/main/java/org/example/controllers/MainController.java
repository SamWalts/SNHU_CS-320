/*
Artifact Enhancement
Author: Samuel Walters
Date: 9/26/24
Updated: 10/4/24 to include the database connection and implement new buttons to update, and delete contacts.
 */
package org.example.controllers;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.example.models.Contact;
import org.example.models.ContactService;


public class MainController implements Initializable {

    @FXML
    private TableView<Contact> tableView;

    @FXML
    private TableColumn<Contact, String> colFirstName;

    @FXML
    private TableColumn<Contact, String> colLastName;

    @FXML
    private TableColumn<Contact, String> colPhoneNumber;

    @FXML
    private TableColumn<Contact, String> colAddress;

    @FXML
    private TextField nameFirst;

    @FXML
    private TextField nameLast;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField addressField;

    @FXML
    private Button createBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Label labelMessage;

//   Call the ContactService class once to get the contacts list and use it throughout the program.
    ContactService contactService = new ContactService();

//    private ObservableList<Contact> contactsList;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showContacts();
    }

    /*
    * Method to show the contacts in the table view.
    * */
    @FXML
    private void showContacts() {
        ObservableList<Contact> contactsList = contactService.getContactsList();
        colFirstName.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<Contact, String>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Contact, String>("address"));
        tableView.setItems(contactsList);
    }

    /*
    @param e
    Updated 10/4/2024 takes in an action event and determines which button was clicked, then calls the appropriate method.
    All must end with a new call to showContacts() to update the table view.
     */
    @FXML
    private void handleButtonClick(ActionEvent e) {
        System.out.println(((Button) e.getSource()).getId());
        switch(((Button) e.getSource()).getId()) {
            case "deleteBtn":
                deleteContact();
                showContacts();
                break;
            case "createBtn":
                createContact();
                showContacts();
                break;
            case "updateBtn":
                updateContact();
                showContacts();
                break;
            default:
                labelMessage.setText("Invalid button");
        }
    }
    /*
    * Method to create a contact. It will take the information from the text fields and create a new contact.
    * 10/4/2024 updated to use try/catch block to catch IllegalArgumentExceptions thrown by the Contact object.
     */
    @FXML
    private void createContact() {
        if (nameFirst.getText().isEmpty() || nameLast.getText().isEmpty() || phoneField.getText().isEmpty() || addressField.getText().isEmpty()) {
            labelMessage.setText("Please fill out all fields.");
        } else {
            try {
//          Reset label message text.
                labelMessage.setText("");
                Contact contact = new Contact(nameLast.getText(), nameFirst.getText(), phoneField.getText(), addressField.getText());
                contactService.addContact(contact);
                clearFields();
            } catch (IllegalArgumentException e) {
                labelMessage.setText(e.getMessage());
            }
        }
    }

    /*
    * Method to delete a contact. It will take the selected contact from the table view and delete it.
    * 10/4/2024
     */
    @FXML
    private void deleteContact() {
        try {
            labelMessage.setText("");
            Contact contact = tableView.getSelectionModel().getSelectedItem();
            contactService.deleteContact(contact);
            showContacts();
            clearFields();
        } catch (IllegalArgumentException e) {
            labelMessage.setText(e.getMessage());
        }
    }

    /*
    * Method to handle a mouse click event. It will take the selected contact from the table view and populate the text fields.
    * 10/4/2024
     */
    @FXML
    public void mouseClicked(MouseEvent mouseEvent) {
        try {
            labelMessage.setText("");
            Contact contact = tableView.getSelectionModel().getSelectedItem();
            nameFirst.setText(contact.getFirstName());
            nameLast.setText(contact.getLastName());
            phoneField.setText(contact.getPhone());
            addressField.setText(contact.getAddress());
            deleteBtn.setDisable(false);
            updateBtn.setDisable(false);
         } catch (IllegalArgumentException ex) {
            labelMessage.setText(ex.getMessage());
        }
    }

    /*
    * Method to update a contact. It will take the selected contact from the table view and update it.
    * Updated 10/4/2024
    */
    @FXML
    private void updateContact() {
        try {
            labelMessage.setText("");
            Contact contact = tableView.getSelectionModel().getSelectedItem();
            if (contact != null) {
                contact.setFirstName(nameFirst.getText());
                contact.setLastName(nameLast.getText());
                contact.setPhone(phoneField.getText());
                contact.setAddress(addressField.getText());
                contactService.updateContact(contact);
                clearFields();
                showContacts();
            } else {
                labelMessage.setText("No contact selected.");
            }
        } catch (IllegalArgumentException e) {
            labelMessage.setText(e.getMessage());
        }
    }

    /*
    * Method to clear the fields after a contact is created.
    * Called after every successful contact creation.
     */
    private void clearFields() {
        nameFirst.clear();
        nameLast.clear();
        phoneField.clear();
        addressField.clear();
    }

}
