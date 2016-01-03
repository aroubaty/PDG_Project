package ch.heigvd.flat5.utils;

import ch.heigvd.flat5.api.sound.GetSoundInfo;
import ch.heigvd.flat5.api.sound.TrackInfos;
import ch.heigvd.flat5.api.video.MovieDataGetter;
import ch.heigvd.flat5.api.video.MovieInfos;
import ch.heigvd.flat5.sqlite.MovieManager;
import ch.heigvd.flat5.sqlite.SQLiteConnector;
import ch.heigvd.flat5.sqlite.TrackManager;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.io.File;
import java.util.List;

public class LibraryManager
{
    private static final String LIBVLC_PATH = "src/main/resources/vlc";

    public static void addFileToDB(String directoryPath)
    {
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), LIBVLC_PATH);
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        SQLiteConnector sqLiteConnector = new SQLiteConnector();
        sqLiteConnector.connectToDB();
        sqLiteConnector.initDB();
        TrackManager trackManager = new TrackManager(sqLiteConnector);
        MovieManager movieManager = new MovieManager(sqLiteConnector);
        MovieDataGetter movieDataGetter = new MovieDataGetter();

        File root = new File(directoryPath);


        try {
        /* On parcourt tous les fichiers et souds-dossiers contenus dans la racine.
           Les sous-dossier contiennent les épisodes d'une série, et le nom du sous-dossier doit correspondre au
           nom de la série.
         */
        for (File file : root.listFiles())
        {
            System.out.println();
            if (file.isDirectory())
            {
                /* On vérifie si cette série a déjà été ajouté à la base de donnée. Si ce n'est pas le cas on l'ajoute
                   ainsi que tous les épisodes contenus dans le dossier. */
                String path = file.getPath();
                if (!movieManager.serieIsKnown(path))
                {
                    // On récupère les données de la série sur omdb et on l'ajoute à la base de données.
                    String serieTitle = path.substring(directoryPath.length() + 1);
                    MovieInfos infos = movieDataGetter.searchSerie(serieTitle);
                    if(infos == null)
                    {
                        infos = new MovieInfos();
                        infos.setTitle(serieTitle);
                        infos.setType("series");
                    }
                    int serieID = movieManager.addMovie(infos, path);


                    // On ajoute tous les épisodes à la base de données.
                    for (File episode : file.listFiles())
                    {
                        if(episode.isFile())
                        {
                            String fileName = episode.getPath();
                            if(isMovie(fileName))
                            {
                                MediaMeta metas = mediaPlayerFactory.getMediaMeta(fileName, true);
                                String episodeName = metas.getTitle();
                                episodeName = episodeName.substring(0, episodeName.lastIndexOf('.'));
                                String season = metas.getSeason();
                                String episodeNumber = metas.getEpisode();
                                if(season == null) {season = "";}
                                if(episodeNumber == null) {episodeNumber = "";}
                                metas.release();

                                movieManager.addEpisode(episodeName, serieID,fileName, season, episodeNumber);
                            }
                        }
                    }
                }
            }

            // Il s'agit d'un fichier, donc d'un film ou d'une musique, qu'on ajoute alors à la bibliothèque.
            else
            {
                String path = file.getPath();
                if(isMovie(path))
                {
                    if(!movieManager.isKnown(path))
                    {
                        MediaMeta metas = mediaPlayerFactory.getMediaMeta(path, true);
                        String movieTitle = metas.getTitle();
                        if(movieTitle.contains("."))
                            { movieTitle = movieTitle.substring(0, movieTitle.lastIndexOf('.')); }
                        MovieInfos infos = movieDataGetter.searchFilm(movieTitle);
                        if (infos == null)
                        {
                            infos = new MovieInfos();
                            infos.setTitle(movieTitle);
                            infos.setRuntime(convertToMinutes(metas.getLength()) + " min");
                            infos.setGenre(metas.getGenre());
                            infos.setPoster(metas.getArtworkUrl());
                            infos.setPlot(metas.getDescription());
                            infos.setReleaseDate(metas.getDate());
                            infos.setType("movie");
                        }
                        movieManager.addMovie(infos, path);
                        metas.release();
                    }
                }

                else if(isMusic(path))
                {
                    if(!trackManager.isKnown(path))
                    {
                        TrackInfos infos = GetSoundInfo.doIt(new File(path));
                        trackManager.addTrack(infos, path);
                    }
                }
            }
        }
            mediaPlayerFactory.release();
        }
        catch (Exception e)
        {System.err.println("Error while scanning files for the library " + e.getClass().getName() + ": " + e.getMessage());}
    }

    public static boolean isMusic(String file)
    {
        if(file.contains(".mp3") || file.contains(".flac") || file.contains(".ogg") || file.contains(".wav"))
        { return true; }
        return false;
    }

    public static boolean isMovie(String file)
    {
        if(file.contains(".mp4") || file.contains(".mkv") || file.contains(".avi"))
        { return true; }
        return false;
    }

    public static String convertToMinutes (long msTime)
    {
        return new Long(msTime / 60000).toString();
    }
}
