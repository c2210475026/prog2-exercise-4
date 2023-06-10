package at.ac.fhcampuswien.fhmdb.contollers;

import at.ac.fhcampuswien.fhmdb.datalayer.database.WatchlistEntity;
import at.ac.fhcampuswien.fhmdb.datalayer.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.datalayer.models.Movie;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.sql.SQLException;

public class WatchlistController{

    WatchlistRepository repository = new WatchlistRepository();
    private final ClickEventHandler<Movie> onAddToWatchlistClicked = (clickedItem) -> {
    try {
    repository.addToWatchlist(new WatchlistEntity(clickedItem));
    }

    catch (DatabaseException e) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("DatabaseException");
        alert.setHeaderText("DatabaseException");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        // from https://stackoverflow.com/questions/11662857/javafx-2-1-messagebox
    }
    };


    public WatchlistController() throws DatabaseException {
    }
}
