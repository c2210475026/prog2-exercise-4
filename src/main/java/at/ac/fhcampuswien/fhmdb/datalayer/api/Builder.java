package at.ac.fhcampuswien.fhmdb.datalayer.api;

import at.ac.fhcampuswien.fhmdb.datalayer.models.Genre;

public interface Builder {
    MovieAPIRequestBuilder query(String query);
    MovieAPIRequestBuilder genre(Genre genre);
    MovieAPIRequestBuilder releaseYear(String releaseYear);
    MovieAPIRequestBuilder ratingFrom(String ratingFrom);

}
