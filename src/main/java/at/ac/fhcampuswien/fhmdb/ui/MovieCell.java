package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.contollers.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.datalayer.database.WatchlistEntity;
import at.ac.fhcampuswien.fhmdb.datalayer.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.datalayer.models.Movie;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final JFXButton detailBtn = new JFXButton("Show Details");
    private final JFXButton watchlistBtn = new JFXButton("Add to Watchlist");
    private final JFXButton rmvWatchlistBtn = new JFXButton("Remove");


    private final VBox layout = new VBox(title, detail, genre, detailBtn, watchlistBtn, rmvWatchlistBtn);
    private boolean collapsedDetails = true;

    WatchlistRepository repository = new WatchlistRepository();



    public MovieCell(ClickEventHandler addToWatchlistClicked,ClickEventHandler rmwFromWatchlistClicked) throws DatabaseException {
        super();
        // color scheme
        detailBtn.setStyle("-fx-background-color: #f5c518;");
        watchlistBtn.setStyle("-fx-background-color: #007aff; -fx-text-fill: white;");
        rmvWatchlistBtn.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white;");
        title.getStyleClass().add("text-yellow");
        detail.getStyleClass().add("text-white");
        genre.getStyleClass().add("text-white");
        genre.setStyle("-fx-font-style: italic");
        layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

        // layout
        title.fontProperty().set(title.getFont().font(20));
        detail.setWrapText(true);
        layout.setPadding(new Insets(10));
        layout.spacingProperty().set(10);
        layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);

        detailBtn.setOnMouseClicked(mouseEvent -> {
            if (collapsedDetails) {
                layout.getChildren().add(getDetails());
                collapsedDetails = false;
                detailBtn.setText("Hide Details");
            } else {
                layout.getChildren().remove(6);
                collapsedDetails = true;
                detailBtn.setText("Show Details");
            }

            setGraphic(layout);
        });
        rmvWatchlistBtn.setOnMouseClicked(mouseEvent -> {
            rmwFromWatchlistClicked.onClick(getItem());
            System.out.println("Removed " + getItem().getTitle() + " from watchlist");
        });
        watchlistBtn.setOnMouseClicked(mouseEvent -> {
            addToWatchlistClicked.onClick(getItem());
            System.out.println("Added " + getItem().getTitle() + " to watchlist");

        });
    }

    public void btnSettings() throws DatabaseException{
    // Check if the movie is in the watchlist
    boolean inWatchlist = false;

        inWatchlist = repository.getAll().contains(new WatchlistEntity(getItem()));


        // Set the visibility of the Add to Watchlist and Remove buttons
    if (inWatchlist) {
        layout.setBackground(new Background(new BackgroundFill(Color.web("Green"), null, null)));
        watchlistBtn.setVisible(false);
        rmvWatchlistBtn.setVisible(true);
    } else {
        layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));
        watchlistBtn.setVisible(true);
        rmvWatchlistBtn.setVisible(false);
    }
}

    private VBox getDetails() {
        VBox details = new VBox();
        Label releaseYear = new Label("Release Year: " + getItem().getReleaseYear());
        Label length = new Label("Length: " + getItem().getLengthInMinutes() + " minutes");
        Label rating = new Label("Rating: " + getItem().getRating() + "/10");

        Label directors = new Label("Directors: " + String.join(", ", getItem().getDirectors()));
        Label writers = new Label("Writers: " + String.join(", ", getItem().getWriters()));
        Label mainCast = new Label("Main Cast: " + String.join(", ", getItem().getMainCast()));

        releaseYear.getStyleClass().add("text-white");
        length.getStyleClass().add("text-white");
        rating.getStyleClass().add("text-white");
        directors.getStyleClass().add("text-white");
        writers.getStyleClass().add("text-white");
        mainCast.getStyleClass().add("text-white");

        details.getChildren().add(releaseYear);
        details.getChildren().add(rating);
        details.getChildren().add(length);
        details.getChildren().add(directors);
        details.getChildren().add(writers);
        details.getChildren().add(mainCast);
        return details;
    }
    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setGraphic(null);
            setText(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );

            String genres = movie.getGenres()
                    .stream()
                    .map(Enum::toString)
                    .collect(Collectors.joining(", "));
            genre.setText(genres);

            detail.setMaxWidth(this.getScene().getWidth() - 30);

            setGraphic(layout);
            try {
                btnSettings();
                //throw new DatabaseException("test");
            }catch (DatabaseException e){

                // Problem: Can't stop Programm here and Can't allow asynchronus because it causes an infinet amount of messageboxes
                Platform.runLater(new Runnable() {
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("DatabaseException");
                        alert.setHeaderText("DatabaseException");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                });
                // from https://stackoverflow.com/questions/11662857/javafx-2-1-messagebox

                /*
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("DatabaseException");
                alert.setHeaderText("DatabaseException");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                */
            }

        }
    }
}

