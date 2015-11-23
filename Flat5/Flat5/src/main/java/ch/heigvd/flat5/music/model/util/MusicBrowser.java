package ch.heigvd.flat5.music.model.util;

import ch.heigvd.flat5.music.model.Music;
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
import java.util.ArrayList;
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
            Tag tag = audioFile.getTag();
            musics.add(new Music(tag.getFirst(FieldKey.TITLE), tag.getFirst(FieldKey.ARTIST),
                    tag.getFirst(FieldKey.ALBUM), tag.getFirst(FieldKey.GENRE), tag.getFirst(FieldKey.YEAR),
                    String.valueOf(audioFile.getAudioHeader().getTrackLength()), file.getPath()));
        }

        return musics;
    }
}
