package at.ac.fhcampuswien.fhmdb.datalayer.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    Dao<WatchlistEntity,Long> dao;

    private static WatchlistRepository instance;

    private WatchlistRepository() throws DatabaseException{
        this.dao = Database.getDatabase().getWatchlistDao();
    }

    public static WatchlistRepository getInstance() throws DatabaseException {
        if(instance==null){
            instance = new WatchlistRepository();
        }
        return instance;
    }

    public void removeFromWatchlist(WatchlistEntity movie) throws DatabaseException {
        DeleteBuilder<WatchlistEntity,Long> deleteBuilder = dao.deleteBuilder();
        try {
            deleteBuilder.where().eq("APIID",movie.getApiId());
            deleteBuilder.delete();
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
            dao.create(movie);
        }
        catch (SQLException e) {
            throw new DatabaseException("getAll entries from the Database failed/n" + e.getCause());
        }
    }
}
