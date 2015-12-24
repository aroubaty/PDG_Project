/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heigvd.flat5.settings.view;

import ch.heigvd.flat5.AppConfig;
import ch.heigvd.flat5.music.view.MusicController;
import ch.heigvd.flat5.sqlite.Contact;
import ch.heigvd.flat5.sqlite.ContactManager;
import ch.heigvd.flat5.sqlite.SQLiteConnector;
import com.google.common.net.InetAddresses;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author jermoret
 */
public class SettingsController implements Initializable {

    @FXML Button friendsManager;
    @FXML Button browse;
    @FXML Label mediaPath;
    @FXML TextField name;
    @FXML TextField ipAddress;
    @FXML Button validateContact;
    @FXML TableView<Contact> contacts;
    @FXML TableColumn<Contact, String> contName;
    @FXML TableColumn<Contact, String> contIP;

    ObservableList<Contact> friends = FXCollections.observableArrayList();
    ContactManager contactManager;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        contName.setCellValueFactory(new PropertyValueFactory("name"));
        contIP.setCellValueFactory(new PropertyValueFactory("address"));

        contName.setCellFactory(TextFieldTableCell.forTableColumn());
        contName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Contact, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Contact, String> t) {
                Contact toModify = ((Contact) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                );
                toModify.setName(t.getNewValue());
                contactManager.updateContact(toModify);
            }
        });
        contIP.setCellFactory(TextFieldTableCell.forTableColumn());
        contIP.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Contact, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Contact, String> t) {
                Contact toModify = ((Contact) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                );
                toModify.setAddress(t.getNewValue());
                contactManager.updateContact(toModify);
            }
        });

        SQLiteConnector sqLiteConnector = new SQLiteConnector();
        sqLiteConnector.connectToDB();
        sqLiteConnector.initDB();
        contactManager = new ContactManager(sqLiteConnector);

        friends = FXCollections.observableArrayList(contactManager.getContacts());
        contacts.setItems(friends);
    }

    @FXML
    private void handleAddContact() {
        name.setText("");
        ipAddress.setText("");
        name.setVisible(true);
        ipAddress.setVisible(true);
        validateContact.setVisible(true);
    }

    @FXML
    private void handleValidateContact() {
        name.setVisible(false);
        ipAddress.setVisible(false);
        validateContact.setVisible(false);

        if(!InetAddresses.isInetAddress(ipAddress.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur ajout d'un ami");
            alert.setHeaderText("Adresse IP de l'ami incorrect");
            alert.showAndWait();
            return;
        }

        long id = contactManager.addContact(new Contact(name.getText(), ipAddress.getText()));
        friends.add(new Contact(id, name.getText(),ipAddress.getText()));
    }

    @FXML
    private void handleDeleteContact() {
        Contact toRemove = contacts.getSelectionModel().getSelectedItem();
        if(toRemove != null) {
            contactManager.removeContact(toRemove.getId());
            friends.removeAll(contacts.getSelectionModel().getSelectedItem());
        }
    }
}
