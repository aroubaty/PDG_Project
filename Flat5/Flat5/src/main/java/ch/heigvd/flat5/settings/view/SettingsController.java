/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heigvd.flat5.settings.view;

import ch.heigvd.flat5.AppConfig;
import ch.heigvd.flat5.MainApp2;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

/**
 * Classe contrôleur FXML pour la vue Settings.fxml
 *
 * @author Jérôme
 */
public class SettingsController implements Initializable {

    // Composants JavaFX
    @FXML Button friendsManager;
    @FXML Button browse;
    @FXML Label mediaPath;
    @FXML TextField name;
    @FXML TextField ipAddress;
    @FXML Button validateContact;
    @FXML TableView<Contact> contacts;
    @FXML TableColumn<Contact, String> contName;
    @FXML TableColumn<Contact, String> contIP;

    private MainApp2 mainApp;
    private BorderPane rootLayout;
    private ObservableList<Contact> friends = FXCollections.observableArrayList();
    private ContactManager contactManager;

    /**
     * Initialisation de la classe contrôleur
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Configurations des colonnes de la table
        contName.setCellValueFactory(new PropertyValueFactory("name"));
        contIP.setCellValueFactory(new PropertyValueFactory("address"));

        // Configuration des cases éditables
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

        // Ouverture de la connexion Sqlite
        SQLiteConnector sqLiteConnector = new SQLiteConnector();
        sqLiteConnector.connectToDB();
        sqLiteConnector.initDB();
        contactManager = new ContactManager(sqLiteConnector);

        // Récupération des contacts depuis la DB
        friends = FXCollections.observableArrayList(contactManager.getContacts());
        contacts.setItems(friends);
    }

    /**
     * Getter for property 'friends'.
     *
     * @return Value for property 'friends'.
     */
    public ObservableList<Contact> getFriends() {
        return friends;
    }

    /**
     * Setter for property 'friends'.
     *
     * @param friends Value to set for property 'friends'.
     */
    public void setFriends(ObservableList<Contact> friends) {
        this.friends = friends;
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp2 mainApp) {
        this.mainApp = mainApp;
        this.rootLayout = mainApp.getRootLayout();
    }

    /**
     * Getter for property 'mediaPath'.
     *
     * @return Value for property 'mediaPath'.
     */
    public Label getMediaPath() {
        return mediaPath;
    }

    /**
     * Setter for property 'mediaPath'.
     *
     * @param mediaPath Value to set for property 'mediaPath'.
     */
    public void setMediaPath(Label mediaPath) {
        this.mediaPath = mediaPath;
    }

    /**
     * Setter for property 'path'.
     *
     * @param path Value to set for property 'path'.
     */
    public void setPath(String path) {
        mediaPath.setText(path);
    }

    /**
     * Action sur l'ajout d'un contact
     */
    @FXML
    private void handleAddContact() {
        name.setText("");
        ipAddress.setText("");
        name.setVisible(true);
        ipAddress.setVisible(true);
        validateContact.setVisible(true);
    }

    /**
     * Action sur la validation d'ajout d'un contact
     */
    @FXML
    private void handleValidateContact() {
        name.setVisible(false);
        ipAddress.setVisible(false);
        validateContact.setVisible(false);

        // Si l'addresse n'est pas un format IP standard
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

    /**
     * Action sur la suppression d'un contact
     */
    @FXML
    private void handleDeleteContact() {
        Contact toRemove = contacts.getSelectionModel().getSelectedItem();
        if(toRemove != null) {
            contactManager.removeContact(toRemove.getId());
            friends.removeAll(contacts.getSelectionModel().getSelectedItem());
        }

    }

    /**
     * Ouverture du dialogue permettant la sélection du chemin vers les médias
     */
    @FXML
    public void setPathValue()
    {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        final File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            String dir = selectedDirectory.getAbsolutePath();
            mediaPath.setText(dir);
            mainApp.setPath(dir);
        }
    }
}
