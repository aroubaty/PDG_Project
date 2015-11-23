package ch.heigvd.flat5.utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Anthony on 23.11.2015.
 */
public class FileLister {

    /**
     * Fonction qui va lister récursivement le contenu du dossier
     * @param directoryPath
     *      Chemin du dossier
     * @return
     *      liste des chemins des fichiers
     */
    public static List<String> fromDirectory(String directoryPath){
        File root = new File(directoryPath);
        LinkedList<String> list = new LinkedList<>();

        for(String file : root.list()) {
            if(!file.contains(".")){
                //c'est une dossier
                for(String directoryFiles : FileLister.fromDirectory(directoryPath + "\\" + file))
                    list.add(directoryFiles);
            }else{
                //c'est un fichier
                list.add(directoryPath + "\\" + file);
            }

        }
        return (List<String>)list.clone();
    }
}
