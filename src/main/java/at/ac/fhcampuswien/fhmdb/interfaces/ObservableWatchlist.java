package at.ac.fhcampuswien.fhmdb.interfaces;

public interface ObservableWatchlist {
    void addObserver(ObserverWatchlist observer);
    void removeObserver(ObserverWatchlist observer);
    void notifyObservers(String message);
}
