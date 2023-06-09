package com.example.ipose_megaman;

import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static com.almasb.fxgl.dsl.FXGL.image;
import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.example.ipose_megaman.EntityTypes.BULLET;

public class PlayerComponent extends Component {

    public int jumps = 1;

    public PhysicsComponent physics;

    public Texture texture;

    public PlayerComponent(){
        Image image = image("assets/textures/MegaManIdle.png");
        texture = new Texture(image);
    }

    @Override
    public void onAdded() {
        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            if (isOnGround) {
                jumps = 1;
            }
        });
    }
    public void left() {
        getEntity().setScaleX(-2.5);
        physics.setVelocityX(-300);
    }

    public void right() {
        getEntity().setScaleX(2.5);
        physics.setVelocityX(300);
    }

    public void jump() {
        if (jumps == 0)
            return;

        physics.setVelocityY(-600);

        jumps--;
    }

    public void stop() {
        physics.setVelocityX(0);
    }



    void shoot(double positionX, double positionY) {
        entityBuilder()
                .type(BULLET)
                .at(positionX, positionY)
                .viewWithBBox(new Circle(10.0, 5.0, 5.0, Color.WHITE))
                .collidable()
                .with(new ProjectileComponent(new Point2D(1.0, 0.0), 650.0))
                .buildAndAttach();
    }

    void shootLeft(double positionX, double positionY) {
        entityBuilder()
                .type(BULLET)
                .at(positionX, positionY)
                .viewWithBBox(new Circle(10.0, 5.0, 5.0, Color.WHITE))
                .collidable()
                .with(new ProjectileComponent(new Point2D(-1.0, 0.0), 650.0))
                .buildAndAttach();
    }
}
