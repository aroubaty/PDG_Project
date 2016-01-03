package ch.heigvd.flat5.api.video;
import com.google.gson.JsonObject;

/**
 * Classe contenant les informations d'un film (ou d'une série).
 * Par défaut les informations contiennent une chaîne de caractères vides. Il se peut également que certaines
 * informations soient valent null.
 *
 * @author Jan Purro
 */
public class MovieInfos
{
    // Informations directement relatives au film où à la série.
    private String title = "";
    private String year = "";
    private String releaseDate = "";
    private String runtime = "";
    private String genre = "";
    private String plot = "";
    private String imdbRating = "";
    private String imdbVotes = "";
    private String type = "";
    private String metaScore = "";
    private String imdbID = "";
    private String poster = "";

    // Contient le chemin du fichier correspondant à ces informations.
    private String path = "";
    /* Contient l'id, dans la base de données de l'application, du film ou de ls série. Ce champ sert à effectuer la
       recherche des épisodes correspondant à une série à l'intérieur de la base de données.
     */
    private String dbID = "";

    /**
     * Contrstucteur vide. Tout les champs seront vides, excepété le champ dbID, qui sera vide.
     */
    public MovieInfos() {}

    /**
     * Constrtuit un nouvel objet à partir des informations passées en paramètre.
     * @param title Le titre du média.
     * @param year L'année de sortie du média.
     * @param releaseDate La date de sortie du média.
     * @param runtime La duréée du média.
     * @param genre Le ou les genre(s) du média.
     * @param plot Un résumé du média.
     * @param imdbRating La note donnée au média sur IMDb.
     * @param imdbVotes Le nombre de vote déterminatn la note sur IMDb.
     * @param type Le type de média ("movie" ou "series").
     * @param metaScore Le metascore du média.
     * @param imdbID L'id du média sur IMDb.
     * @param poster L'URL du poster du média.
     * @param path Le chemin du média dans la collection de l'utilisateur.
     * @param dbID L'id du média dans la base de donnée de l'application.
     */
    public MovieInfos(String title, String year, String releaseDate, String runtime, String genre, String plot,
                      String imdbRating, String imdbVotes, String type, String metaScore, String imdbID, String poster,
                      String path, String dbID)
    {
        this.title = title;
        this.year = year;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.genre = genre;
        this.plot = plot;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.type = type;
        this.metaScore = metaScore;
        this.imdbID = imdbID;
        this.poster = poster;
        this.path = path;
        this.dbID = dbID;
    }

    /**
     * Construit un nouvel objet à partir d'un objet JSON.
     * Si certains champs sont absents de l'objet JSON, ils vaudront null.
     * Les champs path et dbID ne sont pas initialisés par ce constructeur.
     * @param movieObject L'objet JSON contenant les informations.
     */
    public MovieInfos (JsonObject movieObject)
    {
        title = movieObject.get("Title").getAsString();
        year = movieObject.get("Year").getAsString();
        imdbID = movieObject.get("imdbID").getAsString();
        poster = movieObject.get("Poster").getAsString();
        releaseDate = movieObject.get("Released").getAsString();
        runtime = movieObject.get("Runtime").getAsString();
        genre = movieObject.get("Genre").getAsString();
        type = movieObject.get("Type").getAsString();
        plot = movieObject.get("Plot").getAsString();
        imdbRating = movieObject.get("imdbRating").getAsString();
        imdbVotes = movieObject.get("imdbVotes").getAsString();
        metaScore = movieObject.get("Metascore").getAsString();
    }

    // Méthodes permettant d'obtenir ou de modifier la valeur des champs de l'objet.

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMetaScore() {
        return metaScore;
    }

    public void setMetaScore(String metaScore) {
        this.metaScore = metaScore;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDbID() {
        return dbID;
    }

    public void setDbID(String dbID) {
        this.dbID = dbID;
    }
}
