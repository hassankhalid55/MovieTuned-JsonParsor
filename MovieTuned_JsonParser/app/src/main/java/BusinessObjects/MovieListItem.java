package BusinessObjects;

import android.util.Log;

/**
 * Created by Hassan Khalid on 27/05/2016.
 */
public class MovieListItem {
    String title;
    String cover_path;
    String cover_URL;
    String id;

    public MovieListItem() {
        this.cover_URL = "https://image.tmdb.org/t/p/w370";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover_path() {
        return cover_path;
    }

    public void setCover_path(String cover_path) {
        Log.i("fsf", "setCover_path: " + cover_path);
        this.cover_path = cover_URL;
        this.cover_path += cover_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
