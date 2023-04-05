package com.example.ipose_megaman;

import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.example.ipose_megaman.EntityTypes.BULLET;

public class PlayerComponent extends Component {

    public int jumps = 1;

    public PhysicsComponent physics;

    public PlayerComponent(){
        Image image = image("MegaManIdle.png");

    }


    public void left() {
        getEntity().setScaleX(-1);
        physics.setVelocityX(-170);
    }

    public void right() {
        getEntity().setScaleX(1);
        physics.setVelocityX(170);
    }

    public void jump() {
        if (jumps == 0)
            return;

        physics.setVelocityY(-300);

        //jumps--;
    }

    void shoot(double positionX, double positionY) {
        entityBuilder()
                .type(BULLET)
                .at(positionX, positionY)
                .viewWithBBox(new Circle(10.0, 5.0, 5.0, Color.BLUE))
                .collidable()
                .with(new ProjectileComponent(new Point2D(1.0, 0.0), 350.0))
                .buildAndAttach();


    }

    public void stop() {
        physics.setVelocityX(0);
    }
}
