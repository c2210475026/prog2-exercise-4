package at.ac.fhcampuswien.fhmdb.contollers;

@FunctionalInterface
public interface ClickEventHandler<T> {
    void onClick(T t);
}