package org.example.controllers;
/*
Put header here


 */

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private Label labelMessage;

//  TODO: Make CSS file for the list. Lower priority.
//  TODO: Make a new screen, for when the contact is selected for updates. Maybe have a new button, and it will take the highlighted option.
//  TODO: Make a button for deletion with asking if user is sure?


//   Make new contactService in order to have it handle the contactsList. Will be changed with implementation of Database.
    ContactService contactService = new ContactService();

//    private ObservableList<Contact> contactsList;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showContacts();
    }

    /*
    * called on initialize. As of 9/19/24 the getContactsList() method only pulls from an empty hashmap. On update of the map
    * it should show more.
    *
    * * */
    @FXML
    private void showContacts() {
        ObservableList<Contact> contactsList = contactService.getContactsList();
        colFirstName.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<Contact, String>("LastName"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<Contact, String>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Contact, String>("address"));
        tableView.setItems(contactsList);
    }

    @FXML
    private void handleCreateButtonClick(ActionEvent e) {
        if(nameFirst.getText().isEmpty() || nameLast.getText().isEmpty() || phoneField.getText().isEmpty() || addressField.getText().isEmpty()) {
            labelMessage.setText("Please fill out all fields.");
        } else {
            createContact();
            showContacts();
//            labelMessage.setText("Contact created successfully.");
        }
    }

/*
* On CreateButton click, create contact, and insert into the contactService.
* Currently getting ID in controller. Will be removed when DB is created.
* TODO: Remove ID from controller and have it generated in the ContactService DB implemented.
* */
    @FXML
    private void createContact() {
        try {
//          Reset label message text.
            labelMessage.setText("");
            // Remove the below with implementation of a DB
            String Id = contactService.generateId();
            Contact contact = new Contact(Id, nameLast.getText(), nameFirst.getText(), phoneField.getText(), addressField.getText());
            contactService.addContact(contact);
            clearFields();
        } catch (IllegalArgumentException e) {
            labelMessage.setText(e.getMessage());
        }

    }

//    // Method to show a success message
//    private void showSuccessMessage() {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Success");
//        alert.setHeaderText(null);
//        alert.setContentText("Contact created successfully!");
//        alert.showAndWait();
//    }
//
//    // Method to show an error message
//    private void showErrorMessage(String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }

    // Method to clear all fields
    private void clearFields() {
        nameFirst.clear();
        nameLast.clear();
        phoneField.clear();
        addressField.clear();
    }

}
