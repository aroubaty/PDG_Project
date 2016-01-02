package ch.heigvd.flat5.music.model.util;

import ch.heigvd.flat5.api.sound.GetSoundInfo;
import ch.heigvd.flat5.api.sound.TrackInfos;
import ch.heigvd.flat5.music.model.Music;
import ch.heigvd.flat5.sqlite.SQLiteConnector;
import ch.heigvd.flat5.sqlite.TrackManager;
import javafx.scene.image.Image;
import javafx.scene.media.Track;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author : Moret Jérôme
 * Date : 23.11.2015
 * Description :
 */
public class MusicBrowser {

    private String[] acceptedExts;

    public MusicBrowser(String[] exts) {
        acceptedExts = exts;
    }

    public List<Music> getMusics() {
        SQLiteConnector sqLiteConnector = new SQLiteConnector();
        sqLiteConnector.connectToDB();
        TrackManager trackManager = new TrackManager(sqLiteConnector);
        ArrayList<Music> musics = new ArrayList<>();

        for (TrackInfos infos : trackManager.getTracks()) {

            Image imgCover;
            if(infos.urlCover != null) {
                imgCover = new Image(infos.urlCover);
            } else {
                ClassLoader cl = getClass().getClassLoader();
                imgCover = new Image(cl.getResourceAsStream("img/no_cover.png"));
            }

            musics.add(new Music(
                    infos.title,
                    infos.artist,
                    infos.album,
                    infos.genre,
                    infos.year,
                    infos.length,
                    infos.path,
                    imgCover
            ));
        }

        return musics;
    }
}
