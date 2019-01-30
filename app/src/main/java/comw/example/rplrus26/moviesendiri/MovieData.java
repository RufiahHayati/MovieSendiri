package comw.example.rplrus26.moviesendiri;

public class MovieData {
    private int id;
    private String title;
    private String poster_path;
    private String overview;
    private String release_date;

    public MovieData(){
        this.setId (id);
        this.setTitle (title);
        this.setPoster_path (poster_path);
        this.setOverview (overview);
        this.setRelease_date (release_date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
