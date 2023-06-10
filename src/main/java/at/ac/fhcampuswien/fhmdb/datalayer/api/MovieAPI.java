package at.ac.fhcampuswien.fhmdb.datalayer.api;

import at.ac.fhcampuswien.fhmdb.datalayer.models.Genre;
import at.ac.fhcampuswien.fhmdb.datalayer.models.Movie;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import okhttp3.*;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MovieAPI {
    private static final String URL = "http://prog2.fh-campuswien.ac.at/movies"; // https if certificates work
    private static final OkHttpClient client = new OkHttpClient();

    public static List<Movie> getAllMovies() throws MovieApiException{


            return getAllMovies(null, null, null, null);

    }

    public static List<Movie> getAllMovies(String query, Genre genre, String releaseYear, String ratingFrom) throws MovieApiException{
        String url = new MovieAPIRequestBuilder(URL)
                .query(query)
                .genre(genre)
                .releaseYear(releaseYear)
                .ratingFrom(ratingFrom)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "http.agent")  // needed for the server to accept the request
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            Gson gson = new Gson();
            Movie[] movies = gson.fromJson(responseBody, Movie[].class);

            return Arrays.asList(movies);
        } catch (Exception e) {

            throw new MovieApiException("getAllMovies failed/n"+ e.getCause());

        }

    }

    public Movie requestMovieById(UUID id)throws MovieApiException{
        String url = new MovieAPIRequestBuilder(URL).id(id).build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            Gson gson = new Gson();
            return gson.fromJson(response.body().string(), Movie.class);
        } catch (Exception e) {
            throw new MovieApiException("requestMovieByID failed/n"+ e.getCause());
        }

        //return null;
    }
}
