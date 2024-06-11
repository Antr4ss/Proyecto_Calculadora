module Proyecto_Calculadora{
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;


    opens co.edu.uptc.view to javafx.fxml;

    exports co.edu.uptc.view;
    exports co.edu.uptc.control;
    opens co.edu.uptc.control to javafx.fxml;
    exports co.edu.uptc.model;
    opens co.edu.uptc.model to javafx.fxml, com.google.gson;
}