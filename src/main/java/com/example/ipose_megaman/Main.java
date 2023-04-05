package com.example.ipose_megaman;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.CollisionHandler;
import com.example.ipose_megaman.EntityTypes;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

public class Main extends GameApplication {

    private Entity player;

    private static final int LEVEL = 0;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setHeight(10 * 70);
        settings.setWidth(15 * 70);
        settings.setTitle("Mega Man, but AWESOME!");
        settings.setVersion("1.0");
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
        getGameWorld().addEntityFactory(new MegaManFactory());

        FXGL.setLevelFromMap("map.tmx");
//        if (LEVEL == 0) {
//
//        }

        /*player = FXGL.entityBuilder()
                .at(300, 300)
                .view(new Rectangle(30, 50, Color.BLUE))
                .buildAndAttach();*/

        player = FXGL.getGameWorld().spawn("player", 50, 50);

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-1500, 0, 250 * 70, getAppHeight());
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
        viewport.setLazy(true);
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

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityTypes.PLAYER,EntityTypes.PLATFORM) {
            @Override
            protected void onCollision(Entity Player, Entity Platform) {

            }
        });
    }

    @Override
    protected void initInput(){
        Input input = FXGL.getInput();

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                player.translateX(3);
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                player.translateX(-3);
            }
        }, KeyCode.A);

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