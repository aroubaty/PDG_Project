package ch.heigvd.flat5.sync;

/**
 * Created by Anthony on 09.11.2015.
 */
public interface SyncHandler {

    public void begin(String mediaName);

    public void pause();

    public void playAt(int second);

    public void setAt(int second);

    public void bye();

}
