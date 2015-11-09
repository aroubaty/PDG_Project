package ch.heigvd.flat5.sync;

/**
 * Interface pour l'éxecution de commandes entrantes
 *
 * @author Anthony
 */
public interface SyncHandler {

    /**
     * Lorsque l'on reçoit le nom du média
     * @param mediaName
     *          Nom du média
     */
    public void begin(String mediaName);

    /**
     * Lorsque l'on reçoit "pause"
     */
    public void pause();

    /**
     * Lorsque l'on doit lire la vidéo à partir de N secondes
     * @param second
     *          Nombres de seconde
     */
    public void playAt(int second);

    /**
     * Lorsque l'on doit mettre le média à N secondes
     * @param second
     *          Nombres de seconde
     */
    public void setAt(int second);

    /**
     * Lorsque l'on termine la connection
     */
    public void bye();

}
