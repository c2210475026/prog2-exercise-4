package at.ac.fhcampuswien.fhmdb.datalayer.database;

import at.ac.fhcampuswien.fhmdb.datalayer.models.Genre;
import at.ac.fhcampuswien.fhmdb.datalayer.models.Movie;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable(tableName = "watchlistMovie")
public class WatchlistEntity {
    @DatabaseField(generatedId = true)
    long id;
    @DatabaseField
    String apiId;
    @DatabaseField
    String title;
    @DatabaseField
    String description;
    @DatabaseField
    String genres;
    @DatabaseField
    int releaseYear;
    @DatabaseField
    String imgUrl;
    @DatabaseField
    int lengthInMinutes;
    @DatabaseField
    double rating;

    public WatchlistEntity(){

    }

    public WatchlistEntity (String apiId, String title, String description, String genres, int releaseYear, String imgUrl, int lengthInMinutes, double rating) throws DatabaseException {
        try {
            this.apiId = apiId;
            this.title = title;
            this.description = description;
            this.genres = genres;
            this.releaseYear = releaseYear;
            this.imgUrl = imgUrl;
            this.lengthInMinutes = lengthInMinutes;
            this.rating = rating;
        }
        catch (Exception e){
            throw new DatabaseException("Couldn't create WatchlistEntity/n"+ e.getCause());
        }
    }

    public WatchlistEntity(Movie movie) throws DatabaseException{
        try {
            this.apiId = movie.getId();
            this.title = movie.getTitle();
            this.description = movie.getDescription();
            this.genres = genresToString(movie.getGenres());
            this.releaseYear = movie.getReleaseYear();
            this.imgUrl = movie.getImgUrl();
            this.lengthInMinutes = movie.getLengthInMinutes();
            this.rating = movie.getRating();
        }
        catch (DatabaseException databaseException){
            throw databaseException;
        }


    }

    public long getId() {
        return id;
    }

    public String getApiId() {
        return apiId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenres() {
        return genres;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public double getRating() {
        return rating;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setLengthInMinutes(int lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    String genresToString(List<Genre> genres) throws DatabaseException{

        try{
        String genreString="";
        for (Genre genre:genres) {
            genreString = ","+ genre;
        }
        genreString = genreString.substring(1);
        return genreString;
        }
        catch (Exception e){
            throw new DatabaseException("Failure to Convert Genre to String/n"+ e.getCause());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        WatchlistEntity other = (WatchlistEntity) obj;

        if (apiId != null ? !apiId.equals(other.apiId) : other.apiId != null) {
            return false;
        }

        if (title != null ? !title.equals(other.title) : other.title != null) {
            return false;
        }

        if (description != null ? !description.equals(other.description) : other.description != null) {
            return false;
        }

        if (genres != null ? !genres.equals(other.genres) : other.genres != null) {
            return false;
        }

        if (releaseYear != other.releaseYear) {
            return false;
        }

        if (imgUrl != null ? !imgUrl.equals(other.imgUrl) : other.imgUrl != null) {
            return false;
        }

        if (lengthInMinutes != other.lengthInMinutes) {
            return false;
        }

        if (Double.compare(rating, other.rating) != 0) {
            return false;
        }

        return true;
    }


    @Override
    public String toString(){
        // doesn't allow for throwing exceptions
            return this.id + ", " + apiId + ", " + this.title + ", " + this.genres;

    }


}
