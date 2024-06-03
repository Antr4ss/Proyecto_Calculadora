module Proyecto_Calculadora{
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens co.edu.uptc.view to javafx.fxml;

    exports co.edu.uptc.view;

}