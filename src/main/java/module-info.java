module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;

    requires ormlite.jdbc;


    requires com.jfoenix;
    requires okhttp3;
    requires com.google.gson;
    requires java.sql;

    opens at.ac.fhcampuswien.fhmdb.datalayer.models to com.google.gson;
    opens at.ac.fhcampuswien.fhmdb.datalayer.database to ormlite.jdbc;
    opens at.ac.fhcampuswien.fhmdb to javafx.fxml;
    exports at.ac.fhcampuswien.fhmdb.datalayer.models;
    exports at.ac.fhcampuswien.fhmdb;
    exports at.ac.fhcampuswien.fhmdb.datalayer.database;
    exports at.ac.fhcampuswien.fhmdb.contollers;
    opens at.ac.fhcampuswien.fhmdb.contollers to javafx.fxml;

}