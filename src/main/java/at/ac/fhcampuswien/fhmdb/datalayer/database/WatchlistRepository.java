package at.ac.fhcampuswien.fhmdb.datalayer.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.interfaces.ObservableWatchlist;
import at.ac.fhcampuswien.fhmdb.interfaces.ObserverWatchlist;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository implements ObservableWatchlist{
    Dao<WatchlistEntity,Long> dao;
    private List<ObserverWatchlist> observers = new ArrayList<>();


    public WatchlistRepository() throws DatabaseException{
        this.dao = Database.getDatabase().getWatchlistDao();
    }

    public void removeFromWatchlist(WatchlistEntity movie) throws DatabaseException {
        DeleteBuilder<WatchlistEntity,Long> deleteBuilder = dao.deleteBuilder();
        try {
            deleteBuilder.where().eq("APIID",movie.getApiId());
            deleteBuilder.delete();
            notifyObservers("Movie has been removed from Watchlist.");
        } catch (SQLException e) {
            throw new DatabaseException("removeFromWatchlist/n"+e.getCause());
        }

    }

    public List<WatchlistEntity> getAll() throws DatabaseException {
        try {
            return dao.queryForAll();
        }
        catch (SQLException e) {
            throw new DatabaseException("getAll entries from the Database failed/n" + e.getCause());
        }
    }

    public void addToWatchlist(WatchlistEntity movie) throws DatabaseException {
        try {
            if (isMovieOnWatchlist(movie)) {
                notifyObservers("Movie is already on the watchlist.");
                return; // Exit the method if movie is already on the watchlist
            }

            dao.create(movie);
            notifyObservers("Movie added to watchlist");
        }
        catch (SQLException e) {
            throw new DatabaseException("getAll entries from the Database failed/n" + e.getCause());
        }
    }
    private boolean isMovieOnWatchlist(WatchlistEntity movie) throws DatabaseException {
        try {
            QueryBuilder<WatchlistEntity, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq("APIID", movie.getApiId());
            return queryBuilder.query().size() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to check if movie is on watchlist." + e.getCause());
        }
    }
    @Override
    public void addObserver(ObserverWatchlist observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ObserverWatchlist observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (ObserverWatchlist observer : observers) {
            observer.update(message);
        }
    }
}
