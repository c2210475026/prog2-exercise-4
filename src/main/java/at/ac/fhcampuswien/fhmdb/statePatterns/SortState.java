package at.ac.fhcampuswien.fhmdb.statePatterns;

import at.ac.fhcampuswien.fhmdb.datalayer.models.Movie;

import java.util.List;

public abstract class SortState {

    public abstract void sortMovies(List<Movie> movies);

}
