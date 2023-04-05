module com.example.ipose_megaman {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    opens assets.levels;
    opens com.example.ipose_megaman to javafx.fxml;
    opens assets.textures;
    exports com.example.ipose_megaman;
}