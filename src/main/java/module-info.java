module com.example.ipose_megaman {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    opens assets.textures;
    opens com.example.ipose_megaman to javafx.fxml;
    exports com.example.ipose_megaman;
}