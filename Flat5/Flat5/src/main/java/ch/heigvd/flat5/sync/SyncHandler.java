package ch.heigvd.flat5.sync;

/**
 * Interface pour l'�xecution de commandes entrantes
 *
 * @author Anthony
 */
public interface SyncHandler {

    /**
     * Lorsque l'on re�oit le nom du m�dia
     * @param mediaName
     *          Nom du m�dia
     */
    public void begin(String mediaName);

    /**
     * Lorsque l'on re�oit "pause"
     */
    public void pause();

    /**
     * Lorsque l'on re�oit "play"
     */
    public void play();

    /**
     * Lorsque l'on doit mettre le m�dia � N secondes
     * @param second
     *          Nombres de seconde
     */
    public void setAt(int second);

    /**
     * Lorsque l'on termine la connection
     */
    public void bye();

}
