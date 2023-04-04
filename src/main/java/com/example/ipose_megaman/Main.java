package com.example.ipose_megaman;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.physics.CollisionHandler;
import com.example.ipose_megaman.EntityTypes;
import com.almasb.fxgl.parser.text.TextLevelParser

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

public class Main extends GameApplication {

    private Entity player;

    @Override
    protected void initSettings(GameSettings settings) {
//        SETTING
        settings.setHeight(10*16);
        settings.setWidth(15*16);
        settings.setTitle("Hello World");
        settings.setVersion("");
    }

    @Override
    protected void initGame() {
//        PLAYER
//        player = FXGL.entityBuilder()
//                .at(80, 80)
//                .viewWithBBox("")
//                .with(new CollidableComponent(true))
//                .type(EntityTypes.PLAYER)
//                .scale(0.3,0.3)
//                .buildAndAttach();

//        GAME TIMER
//        FXGL.getGameTimer().runAtInterval(() -> {
//                RANDOM INT: int randomPos = ThreadLocalRandom.current().nextInt(0, FXGL.getGameScene().getAppWidth() -180);
//        }, Duration.millis(500));
        TextLevelParser parser = new TextLevelParser(new MegaManFactory());
        Level level = parser.parse("megaman.json");
        getGameWorld().setLevel(level);

    }

    @Override
    protected void initPhysics() {
//        COLLISION HANDLER
//        FXGL.getPhysicsWorld()
//                .addCollisionHandler(new CollisionHandler(EntityTypes.PLAYER, Ander entity type) {
//                    @Override
//                    protected void onCollision(Entity player, Entity sms) {
//                        sms.removeFromWorld();
//                        FXGL.inc("kills", +1);
//                    }
//                });
    }

    @Override
    protected void initInput(){
        // User Input
    }

    @Override
    protected void initUI() {
        // UI & LABELS & BACKGROUND
        // MAKE LABEL:  Label myText = new Label("");
        // LINK TO GAME VAR:  myText.textProperty().bind(FXGL.getWorldProperties().intProperty("kills").asString());

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        // GAME VARIABLES
    }

    public static void main(String[] args) {
        launch(args);
    }
}