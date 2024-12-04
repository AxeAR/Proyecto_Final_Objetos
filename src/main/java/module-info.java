module com.axear.proyecto_final_objetos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.axear.proyecto_final_objetos to javafx.fxml;
    exports com.axear.proyecto_final_objetos;
    exports com.axear.proyecto_final_objetos.Controllers;
    opens com.axear.proyecto_final_objetos.Controllers to javafx.fxml;
    exports com.axear.proyecto_final_objetos.Objects;
    opens com.axear.proyecto_final_objetos.Objects to javafx.fxml;
    exports com.axear.proyecto_final_objetos.DAO;
    opens com.axear.proyecto_final_objetos.DAO to javafx.fxml;
}