package com.example.ipose_megaman;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.scene.image.Image;

import static com.almasb.fxgl.dsl.FXGL.image;

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

    public void stop() {
        physics.setVelocityX(0);
    }
}
