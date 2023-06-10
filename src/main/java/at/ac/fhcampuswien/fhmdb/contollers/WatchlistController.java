package at.ac.fhcampuswien.fhmdb.contollers;

import at.ac.fhcampuswien.fhmdb.datalayer.database.WatchlistEntity;
import at.ac.fhcampuswien.fhmdb.datalayer.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.datalayer.models.Movie;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.interfaces.ObserverWatchlist;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.sql.SQLException;


    WatchlistRepository repository = new WatchlistRepository();

    public WatchlistController() throws DatabaseException {
        repository.addObserver(this);
    }

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
    @Override
    public void update(String message) {
        //run later is required because otherwise it doesn't get shown
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Watchlist Update");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
        //Platform.setImplicitExit(false);

    }

}
