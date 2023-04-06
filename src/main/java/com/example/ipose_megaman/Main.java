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
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.Map;


import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.example.ipose_megaman.EntityTypes.BULLET;
import static com.example.ipose_megaman.EntityTypes.ENEMY;
import static javafx.application.Platform.exit;

public class Main extends GameApplication {

    private Entity player;

    private int LEVEL = 0;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setHeight(720);
        settings.setWidth(1280);
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

        FXGL.setLevelFromMap("level.tmx");

//        if (LEVEL == 0) {
//
//        }

        /*player = FXGL.entityBuilder()
                .at(300, 300)
                .view(new Rectangle(30, 50, Color.BLUE))
                .buildAndAttach();*/

        player = FXGL.getGameWorld().spawn("player", 350, 950);

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-1500, -500, 250 * 70, 1400);
        viewport.bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);

        setLEVEL();
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 1200);

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
            protected void onCollision(Entity player, Entity platform) {
            }
        });



        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityTypes.PLAYER, EntityTypes.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity player, Entity enemy) {
                int currentNumberOfLives = (int) geti("numberOfLives");
                if (currentNumberOfLives == 0){
                    //exit();
                    getGameController().gotoMainMenu();

                }
                int updatedNumberOfLives = currentNumberOfLives - 1;
                set("numberOfLives",updatedNumberOfLives);
                setLEVEL();


            }
        });



        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityTypes.BULLET, EntityTypes.ENEMY) {
            @Override
            protected void onCollision(Entity bullet, Entity enemy) {
                enemy.removeFromWorld();
                bullet.removeFromWorld();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityTypes.BULLET, EntityTypes.PLATFORM) {
            @Override
            protected void onCollision(Entity bullet, Entity platform) {
                bullet.removeFromWorld();
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityTypes.PLAYER,EntityTypes.ENDDOOR) {
            @Override
            protected void onCollisionBegin(Entity player, Entity enddoor) {
                showMessage("Level complete!", () -> {
                    getGameScene().setBackgroundRepeat("tilesheet.png");
                    LEVEL += 1;
                    setLEVEL();

                });


            }
        });
    }

    public void setLEVEL(){

        set("numberOfShots",10);
        if (LEVEL == 0){
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(350, 950));
            getGameScene().setBackgroundRepeat("background2.jpg");
        }
        else if (LEVEL == 1){
            FXGL.getGameScene().setBackgroundRepeat("background1.jpg");
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(3500, 950));
        }else if (LEVEL == 2){
            FXGL.getGameScene().setBackgroundRepeat("background2.jpg");
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(6900, 950));
        }else if (LEVEL == 3){
            FXGL.getGameScene().setBackgroundColor(Color.BLUE);
                    getDialogService().showInputBox("Voer je naam in", answer -> {
                        PrintWriter out = null;
                        try {
                            out = new PrintWriter(new BufferedWriter(new FileWriter("highscores.txt", true)));
                            out.print(answer + " ");
                            out.print(FXGL.getWorldProperties().getDouble("levelTime").toString());
                            out.println();
                            out.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        getGameController().gotoMainMenu();
                    }
                    );
        }
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
                int currentNumberOfShots = (int) geti("numberOfShots");
                if (canShoot) {
                    if (currentNumberOfShots == 0){return;}

                    FXGL.play("shoot.wav");
                    if (directionRight) {
                        player.getComponent(PlayerComponent.class).shoot(player.getRightX(), player.getBottomY() - player.getHeight() / 2);
                    }else{
                        player.getComponent(PlayerComponent.class).shootLeft(player.getRightX(), player.getBottomY() - player.getHeight() / 2);
                    }
                    canShoot = false;

                    int updatedNumberOfShots = currentNumberOfShots - 1;
                    set("numberOfShots",updatedNumberOfShots);

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
        numberOfLives.setTranslateX(170);
        numberOfLives.setTranslateY(100);
        numberOfLives.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 40)); // set the font
        numberOfLives.setFill(Color.RED); // set the fill color

        numberOfLives.textProperty().bind(FXGL.getWorldProperties().intProperty("numberOfLives").asString());

        FXGL.getGameScene().addUINode(numberOfLives);

        Text time = new Text();
        time.setTranslateX(150);
        time.setTranslateY(40);
        time.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // set the font
        time.setFill(Color.WHITE); // set the fill color

        time.textProperty().bind(FXGL.getWorldProperties().doubleProperty("levelTime").asString());

        FXGL.getGameScene().addUINode(time);

        Text numberOfShots = new Text();
        numberOfShots.setTranslateX(190);
        numberOfShots.setTranslateY(150);
        numberOfShots.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 40)); // set the font
        numberOfShots.setFill(Color.BLUEVIOLET); // set the fill color

        numberOfShots.textProperty().bind(FXGL.getWorldProperties().intProperty("numberOfShots").asString());

        FXGL.getGameScene().addUINode(numberOfShots);

        Text lives = new Text("LIVES:");
        lives.setTranslateX(30);
        lives.setTranslateY(100);
        lives.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 40)); // set the font
        lives.setFill(Color.WHITE);
        FXGL.getGameScene().addUINode(lives);

        Text shots = new Text("SHOTS:");
        shots.setTranslateX(30);
        shots.setTranslateY(150);
        shots.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 40)); // set the font
        shots.setFill(Color.WHITE);
        FXGL.getGameScene().addUINode(shots);

        Text timer = new Text("TIME:");
        timer.setTranslateX(30);
        timer.setTranslateY(50);
        timer.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 40)); // set the font
        timer.setFill(Color.WHITE);
        FXGL.getGameScene().addUINode(timer);
    // UI & LABELS & BACKGROUND
        // MAKE LABEL:  Label myText = new Label("");
        // LINK TO GAME VAR:  myText.textProperty().bind(FXGL.getWorldProperties().intProperty("kills").asString());

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {

        vars.put("numberOfLives",3);
        vars.put("levelTime", 0.0);
        vars.put("numberOfShots",10);

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