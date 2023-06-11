package at.ac.fhcampuswien.fhmdb.contollers;

import at.ac.fhcampuswien.fhmdb.datalayer.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import javafx.util.Callback;

public class ControllerFactory implements Callback<Class<?>,Object> {

    @Override
    public Object call(Class<?> aClass) {
        try{
            if (aClass == HomeController.class) {
                return HomeController.getInstance();
            }
            return aClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
