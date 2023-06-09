package com.example.ipose_megaman;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.scene3d.Pyramid;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class MegaManFactory implements EntityFactory {
    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {
        return FXGL.entityBuilder(data)
                //Dit zijn blijkbaar de "key height" en "key width"
                .type(EntityTypes.PLATFORM)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .collidable()
                .with(new PhysicsComponent())
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().friction(0.0f));
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16, 16), BoundingShape.box(14, 8)));

        PlayerComponent playerComponent = new PlayerComponent();
        return FXGL.entityBuilder(data)
                .type(EntityTypes.PLAYER)
                .viewWithBBox("MegaManIdle.png").scale(2.5,3.5)
                .with(physics)
                .collidable()
                .with(playerComponent)
                .build();
    }

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityTypes.ENEMY)
                .viewWithBBox("enemy.png").scale(1.3,1.3)
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("enddoor")
    public Entity newEndDoor(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityTypes.ENDDOOR)
                .viewWithBBox("Black_hole.png")
                .with(new CollidableComponent(true))
                .build();
    }
}
