package at.ac.fhcampuswien.fhmdb.datalayer.api;

import at.ac.fhcampuswien.fhmdb.datalayer.models.Genre;

import java.util.UUID;

public class MovieAPIRequestBuilder implements Builder{

    private final String url;
    private String query;
    private Genre genre;
    private String releaseYear;
    private String ratingFrom;

    private UUID id;

    public MovieAPIRequestBuilder(String url){
        this.url = url;
        query = null;
        genre = null;
        releaseYear = null;
        ratingFrom = null;
        id = null;
    }

    @Override
    public MovieAPIRequestBuilder query(String query) {
        this.query = query;
        return this;
    }

    @Override
    public MovieAPIRequestBuilder genre(Genre genre) {
        this.genre = genre;
        return this;
    }

    @Override
    public MovieAPIRequestBuilder releaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    @Override
    public MovieAPIRequestBuilder ratingFrom(String ratingFrom) {
        this.ratingFrom = ratingFrom;
        return this;
    }

    public MovieAPIRequestBuilder id(UUID id){
        this.id = id;
        return this;
    }

    public String build(){
        StringBuilder newUrl = new StringBuilder(url);

        if (id != null) {
            newUrl.append("/").append(id);
        }else{
            if ( (query != null && !query.isEmpty()) ||
                    genre != null || releaseYear != null || ratingFrom != null) {

                newUrl.append("?");

                // check all parameters and add them to the url
                if (query != null && !query.isEmpty()) {
                    newUrl.append("query=").append(query).append("&");
                }
                if (genre != null) {
                    newUrl.append("genre=").append(genre).append("&");
                }
                if (releaseYear != null) {
                    newUrl.append("releaseYear=").append(releaseYear).append("&");
                }
                if (ratingFrom != null) {
                    newUrl.append("ratingFrom=").append(ratingFrom).append("&");
                }
            }
        }
        return newUrl.toString();
    }
}
