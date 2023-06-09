package at.ac.fhcampuswien.fhmdb.datalayer.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class Database {
    public static final String DB_URL = "jdbc:h2:file: ./db/watchlistdb";
    public static final String username ="user";
    public static final String password ="password";

    private static Database instance;

    private static ConnectionSource connectionSource;
    private Dao<WatchlistEntity,Long> dao;

    private Database()throws DatabaseException {
        try {
            createConnectionSource();
            dao = DaoManager.createDao(connectionSource, WatchlistEntity.class);
            createTables();

        } catch (SQLException e) {
            throw new DatabaseException("Database couldn't be created/n"+e.getCause());
        }
    }

    public Dao<WatchlistEntity,Long> getWatchlistDao(){
        return dao;
    }

    public static Database getDatabase()throws DatabaseException{
        if(instance==null){
            instance = new Database();
        }
        return instance;
    }

    private static void createConnectionSource() throws SQLException {
        connectionSource = new JdbcConnectionSource(DB_URL,username,password);
    }

    public ConnectionSource getConnectionSource(){
        return connectionSource;
    }

    private static void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, WatchlistEntity.class);
    }


}
