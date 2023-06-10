package at.ac.fhcampuswien.fhmdb.contollers;

import at.ac.fhcampuswien.fhmdb.datalayer.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.datalayer.database.WatchlistEntity;
import at.ac.fhcampuswien.fhmdb.datalayer.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.datalayer.models.Genre;
import at.ac.fhcampuswien.fhmdb.datalayer.models.Movie;
import at.ac.fhcampuswien.fhmdb.datalayer.models.SortedState;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;
    @FXML
    public JFXButton watchlistBtn;
    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML
    public JFXComboBox ratingFromComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies;
    WatchlistRepository repository = WatchlistRepository.getInstance();

    private final ClickEventHandler<Movie> onAddToWatchlistClicked = (clickedItem) -> {
        try {
            repository.addToWatchlist(new WatchlistEntity(clickedItem));
            //throw new DatabaseException("test");
        }
        catch (DatabaseException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("DatabaseException");
            alert.setHeaderText("DatabaseException");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            // from https://stackoverflow.com/questions/11662857/javafx-2-1-messagebox
        }
    };

    private final ClickEventHandler<Movie> onRemoveFromWatchlistClicked = (clickedItemRmv) -> {
        try {
            repository.removeFromWatchlist(new WatchlistEntity(clickedItemRmv));
            //throw new DatabaseException("test");
        }

        catch (DatabaseException e){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("DatabaseException");
                    alert.setHeaderText("DatabaseException");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
            // from https://stackoverflow.com/questions/11662857/javafx-2-1-messagebox
        }
    };


    public ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    public SortedState sortedState;

    private static HomeController instance;

    private HomeController() throws DatabaseException {
    }

    public static HomeController getInstance() throws DatabaseException {
        if(instance==null){
            instance =  new HomeController();
        }
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initializeState();
        } catch (MovieApiException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MovieApiException");
            alert.setHeaderText("MovieApiException");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            // from https://stackoverflow.com/questions/11662857/javafx-2-1-messagebox
        }
        try {
            initializeLayout();
        } catch (DatabaseException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("DatabaseException");
            alert.setHeaderText("DatabaseException");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            // from https://stackoverflow.com/questions/11662857/javafx-2-1-messagebox
        }
    }

    public void initializeState() throws MovieApiException {

            List<Movie> result = MovieAPI.getAllMovies();


        setMovies(result);
        setMovieList(result);
        sortedState = SortedState.NONE;

        // test stream methods
        System.out.println("getMostPopularActor");
        System.out.println(getMostPopularActor(allMovies));

        System.out.println("getLongestMovieTitle");
        System.out.println(getLongestMovieTitle(allMovies));

        System.out.println("count movies from Zemeckis");
        System.out.println(countMoviesFrom(allMovies, "Robert Zemeckis"));

        System.out.println("count movies from Steven Spielberg");
        System.out.println(countMoviesFrom(allMovies, "Steven Spielberg"));

        System.out.println("getMoviewsBetweenYears");
        List<Movie> between = getMoviesBetweenYears(allMovies, 1994, 2000);
        System.out.println(between.size());
        System.out.println(between.stream().map(Objects::toString).collect(Collectors.joining(", ")));


    }


    public void initializeLayout()throws DatabaseException {

            movieListView.setItems(observableMovies);   // set the items of the listview to the observable list
            movieListView.setCellFactory(movieListView -> {
                try {
                    return new MovieCell(onAddToWatchlistClicked, onRemoveFromWatchlistClicked);
                }
                    catch (DatabaseException e){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("DatabaseException");
                        alert.setHeaderText("DatabaseException");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                        // from https://stackoverflow.com/questions/11662857/javafx-2-1-messagebox

                    //throw new RuntimeException(e);
                        return new ListCell();
                }
            }); // apply custom cells to the listview

        // genre combobox
        Object[] genres = Genre.values();   // get all genres
        genreComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        genreComboBox.getItems().addAll(genres);    // add all genres to the combobox
        genreComboBox.setPromptText("Filter by Genre");

        // year combobox
        releaseYearComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        // fill array with numbers from 1900 to 2023
        Integer[] years = new Integer[124];
        for (int i = 0; i < years.length; i++) {
            years[i] = 1900 + i;
        }
        releaseYearComboBox.getItems().addAll(years);    // add all years to the combobox
        releaseYearComboBox.setPromptText("Filter by Release Year");

        // rating combobox
        ratingFromComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        // fill array with numbers from 0 to 10
        Integer[] ratings = new Integer[11];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = i;
        }
        ratingFromComboBox.getItems().addAll(ratings);    // add all ratings to the combobox
        ratingFromComboBox.setPromptText("Filter by Rating");
    }

    public void setMovies(List<Movie> movies) {
        allMovies = movies;
    }

    public void setMovieList(List<Movie> movies) {
        observableMovies.clear();
        observableMovies.addAll(movies);
    }

    public void showWatchlist() {
        if(watchlistBtn.getText().equals("to Watchlist")){
            try {
                List<Movie> movies = new ArrayList<>();
                List<WatchlistEntity> watchlistEntities = repository.getAll();
                for (WatchlistEntity entity : watchlistEntities) {
                    movies.add(new Movie(entity));
                }
                observableMovies.clear();
                observableMovies.addAll(movies);

                genreComboBox.setVisible(false);
                ratingFromComboBox.setVisible(false);
                releaseYearComboBox.setVisible(false);
                searchField.setVisible(false);
                searchBtn.setVisible(false);

                watchlistBtn.setText("return to home");
            }
            catch (DatabaseException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("DatabaseException");
                alert.setHeaderText("DatabaseException");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                // from https://stackoverflow.com/questions/11662857/javafx-2-1-messagebox
            }
        }
        else {
            watchlistBtn.setText("to Watchlist");
            observableMovies.clear();
            observableMovies.addAll(allMovies);
            genreComboBox.setVisible(true);
            ratingFromComboBox.setVisible(true);
            releaseYearComboBox.setVisible(true);
            searchField.setVisible(true);
            searchBtn.setVisible(true);
        }

    }




    public void sortMovies(){
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            sortMovies(SortedState.ASCENDING);
        } else if (sortedState == SortedState.ASCENDING) {
            sortMovies(SortedState.DESCENDING);
        }
    }
    // sort movies based on sortedState
    // by default sorted state is NONE
    // afterwards it switches between ascending and descending
    public void sortMovies(SortedState sortDirection) {
        if (sortDirection == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }

    public List<Movie> filterByQuery(List<Movie> movies, String query){
        if(query == null || query.isEmpty()) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream().filter(movie ->
                movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                movie.getDescription().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    public List<Movie> filterByGenre(List<Movie> movies, Genre genre){
        if(genre == null) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream().filter(movie -> movie.getGenres().contains(genre)).toList();
    }

    public void applyAllFilters(String searchQuery, Object genre) {
        List<Movie> filteredMovies = allMovies;

        if (!searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }

        if (genre != null && !genre.toString().equals("No filter")) {
            filteredMovies = filterByGenre(filteredMovies, Genre.valueOf(genre.toString()));
        }

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }

    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().trim().toLowerCase();
        String releaseYear = validateComboboxValue(releaseYearComboBox.getSelectionModel().getSelectedItem());
        String ratingFrom = validateComboboxValue(ratingFromComboBox.getSelectionModel().getSelectedItem());
        String genreValue = validateComboboxValue(genreComboBox.getSelectionModel().getSelectedItem());

        Genre genre = null;
        if(genreValue != null) {
            genre = Genre.valueOf(genreValue);
        }

        List<Movie> movies = getMovies(searchQuery, genre, releaseYear, ratingFrom);
        setMovies(movies);
        setMovieList(movies);
        // applyAllFilters(searchQuery, genre);

        sortMovies(sortedState);
    }

    public String validateComboboxValue(Object value) {
        if(value != null && !value.toString().equals("No filter")) {
            return value.toString();
        }
        return null;
    }

    public List<Movie> getMovies(String searchQuery, Genre genre, String releaseYear, String ratingFrom) {
        try {return MovieAPI.getAllMovies(searchQuery, genre, releaseYear, ratingFrom);}
        catch (MovieApiException e){

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("MovieApiException");
                alert.setHeaderText("MovieApiException");
                alert.setContentText(e.getMessage());
                alert.showAndWait();

            return new ArrayList<Movie>();
        }

    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

    // count which actor is in the most movies
    public String getMostPopularActor(List<Movie> movies) {
        String actor = movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");

        return actor;
    }

    public int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
    }

    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }


}