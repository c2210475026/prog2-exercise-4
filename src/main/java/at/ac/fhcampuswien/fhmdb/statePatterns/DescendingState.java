package at.ac.fhcampuswien.fhmdb.statePatterns;

import at.ac.fhcampuswien.fhmdb.datalayer.models.Movie;

import java.util.Comparator;
import java.util.List;

public class DescendingState extends SortState{
    @Override
    public void sortMovies(List<Movie> movies) {
        movies.sort(Comparator.comparing(Movie::getTitle).reversed());
    }
}
