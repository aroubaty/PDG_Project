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

            AudioFile audioFile = null;
            try {
                audioFile = (AudioFile) AudioFileIO.read(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Tag tag = audioFile.getTag(); // Récupération des tags ID3
            TrackInfos trackInfos = GetSoundInfo.doIt(file); // Récupération des informations API Spotify

            String title, artist, album, genre, year, length, pathFile;
            Image cover = null;

            DateFormat sdf = new SimpleDateFormat("m:ss");
            length = sdf.format(new Date(audioFile.getAudioHeader().getTrackLength() * 1000));

            if((title = tag.getFirst(FieldKey.TITLE)) == null) {
                title = file.getName();
                musics.add(new Music(title, file.getPath(), length));
                continue;
            }
            if((artist = tag.getFirst(FieldKey.ARTIST)) == null) {
                artist = trackInfos.artist;
            }
            if((album = tag.getFirst(FieldKey.ALBUM)) == null) {
                album = trackInfos.album;
            }
            if((genre = tag.getFirst(FieldKey.GENRE)) == null) {
                genre = trackInfos.genre; //TODO Not found yet dans le code à Anthony ???
            }
            if((year = tag.getFirst(FieldKey.YEAR)) == null) {
                //artist = trackInfos.artist; //TODO Spotify API get year ??
            }

            if(trackInfos != null && trackInfos.cover != null) {
                cover = new Image(trackInfos.cover);
            }
            //System.out.println("Cover : " + trackInfos.cover);


            pathFile = file.getPath();

            musics.add(new Music(
                    title,
                    artist,
                    album,
                    genre,
                    year,
                    length,
                    pathFile,
                    cover
            ));
        }

        return musics;
    }
}
