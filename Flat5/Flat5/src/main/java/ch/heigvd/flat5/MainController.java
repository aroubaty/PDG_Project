package ch.heigvd.flat5;

import ch.heigvd.flat5.sync.SyncManager;
import ch.heigvd.flat5.utils.FileLister;

import java.util.List;

/**
 * Created by Anthony on 23.11.2015.
 */
public class MainController {
    public static void main(String[] args){
        //Initiialisation de l'application
        new MainController();

        //Lancement de l'interface
        MainApp2.main(args);
    }

    SyncManager syncManager;

    public MainController(){
        //Initialisation de la DB

        //Initialisation de la partie TCP
        //TODO syncHandler
        syncManager = new SyncManager(AppConfig.DEFAULT_PORT, null);

        //Initialisation de la bibliothèque
        List<String> allMusics = FileLister.fromDirectory(AppConfig.MUSIC_DIRECTORY);
        List<String> allFilms = FileLister.fromDirectory(AppConfig.FILM_DIRECTORY);

    }
}
