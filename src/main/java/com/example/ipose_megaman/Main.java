package com.example.ipose_megaman;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.FXGLForKtKt;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.particle.ParticleSystem;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.CollisionResult;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.example.ipose_megaman.EntityTypes;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.Map;


import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.example.ipose_megaman.EntityTypes.BULLET;
import static javafx.application.Platform.exit;

public class Main extends GameApplication {

    private Entity player;

    private static final int LEVEL = 2;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setHeight(10 * 70);
        settings.setWidth(15 * 70);
        settings.setTitle("Tetris Man 2");
        settings.setVersion("1.0");
        settings.setMainMenuEnabled(true);
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

        FXGL.setLevelFromMap("level0.tmx");

//        if (LEVEL == 0) {
//
//        }

        /*player = FXGL.entityBuilder()
                .at(300, 300)
                .view(new Rectangle(30, 50, Color.BLUE))
                .buildAndAttach();*/

        player = FXGL.getGameWorld().spawn("player", 50, 650);

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-1500, -500, 250 * 70, 1050);
        viewport.bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);

        getGameScene().setBackgroundRepeat("clouds.jpg");


    }



    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 1500);



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



        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityTypes.PLAYER, EntityTypes.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity player, Entity enemy) {
                int currentNumberOfLives = (int) geti("numberOfLives");
                if (currentNumberOfLives == 0){
                    exit();
                }
                int updatedNumberOfLives = currentNumberOfLives - 1;
                set("numberOfLives",updatedNumberOfLives);
                player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(50, 650));


            }
        });



        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityTypes.BULLET, EntityTypes.ENEMY) {
            @Override
            protected void onCollision(Entity bullet, Entity enemy) {
                enemy.removeFromWorld();
                bullet.removeFromWorld();
            }
        });
    }

    private boolean canShoot = true;

    private boolean directionRight = true;
    @Override
    protected void initInput(){
        Input input = FXGL.getInput();

        input.addAction(new UserAction("Move Right") {

            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).right();
                directionRight = true;
            }
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).left();
                directionRight = false;
                //Level level = setLevelFromMap("level" + LEVEL  + ".tmx");

            }
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.A);


        input.addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).jump();

            }
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.SPACE);


        input.addAction(new UserAction("Shoot") {


            @Override
            protected void onActionBegin() {
                if (canShoot) {
                    if (directionRight) {
                        player.getComponent(PlayerComponent.class).shoot(player.getRightX(), player.getBottomY() - player.getHeight() / 2);
                    }else{
                        player.getComponent(PlayerComponent.class).shootLeft(player.getRightX(), player.getBottomY() - player.getHeight() / 2);
                    }
                    canShoot = false;
                }
            }

            protected void onActionEnd() {
                canShoot = true;
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.ENTER);

        // User Input
    }


    @Override
    protected void initUI() {
        Text numberOfLives = new Text();
        numberOfLives.setTranslateX(50);
        numberOfLives.setTranslateY(100);
        numberOfLives.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 40)); // set the font
        numberOfLives.setFill(Color.RED); // set the fill color

        numberOfLives.textProperty().bind(FXGL.getWorldProperties().intProperty("numberOfLives").asString());

        FXGL.getGameScene().addUINode(numberOfLives);

        Text time = new Text();
        time.setTranslateX(50);
        time.setTranslateY(50);
        time.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // set the font
        time.setFill(Color.WHITE); // set the fill color

        time.textProperty().bind(FXGL.getWorldProperties().doubleProperty("levelTime").asString());

        FXGL.getGameScene().addUINode(time);



    // UI & LABELS & BACKGROUND
        // MAKE LABEL:  Label myText = new Label("");
        // LINK TO GAME VAR:  myText.textProperty().bind(FXGL.getWorldProperties().intProperty("kills").asString());

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {

        vars.put("numberOfLives",3);
        vars.put("levelTime", 0.0);
        // GAME VARIABLES
    }

    @Override
    protected void onUpdate(double tpf) {
        inc("levelTime", tpf);

        //if (player.getY() > getAppHeight()) {
        //
        //}
    }

    public static void main(String[] args) {
        launch(args);
    }
}