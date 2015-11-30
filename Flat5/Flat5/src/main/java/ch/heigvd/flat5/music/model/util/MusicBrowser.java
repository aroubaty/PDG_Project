package ch.heigvd.flat5.music.model.util;

import ch.heigvd.flat5.api.sound.GetSoundInfo;
import ch.heigvd.flat5.api.sound.TrackInfos;
import ch.heigvd.flat5.music.model.Music;
import javafx.scene.image.Image;
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

    public List<Music> getMusicsInFolder(String path) {
        ArrayList<Music> musics = new ArrayList<>();
        File dir = new File(path);

        File[] files = dir.listFiles((dir1, name) -> {
            for(String ext : acceptedExts) {
                if(name.toLowerCase().endsWith(ext))
                    return true;
            }
            return false;
        });

        for (File file : files) {

            TrackInfos trackInfos = GetSoundInfo.doIt(file);

            Image imgCover;
            if(trackInfos.urlCover != null) {
                imgCover = new Image(trackInfos.urlCover);
            } else {
                ClassLoader cl = getClass().getClassLoader();
                imgCover = new Image(cl.getResourceAsStream("img/no_cover.png"));
            }

            musics.add(new Music(
                    trackInfos.title,
                    trackInfos.artist,
                    trackInfos.album,
                    trackInfos.genre,
                    trackInfos.year,
                    trackInfos.length,
                    file.getPath(),
                    imgCover
            ));
        }

        return musics;
    }
}
