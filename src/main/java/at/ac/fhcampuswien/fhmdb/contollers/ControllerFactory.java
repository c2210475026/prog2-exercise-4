package at.ac.fhcampuswien.fhmdb.contollers;

import javafx.util.Callback;

public class ControllerFactory implements Callback<Class<?>,Object> {
    @Override
    public Object call(Class<?> aClass) {
        try{
            return (HomeController) aClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
