package com.mygdx.mariobros.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.mariobros.config.BitDefinition;
import com.mygdx.mariobros.config.GraphicsConfig;
import com.mygdx.mariobros.screens.GameScreen;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames = new Array<>();

    public Goomba(GameScreen gameScreen, float x, float y) {
        super(gameScreen, x, y);
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }
    }

    @Override
    protected void defineEnemy() {
        // 创建一个物体定义（BodyDef），用于描述物体的物理属性
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / GraphicsConfig.PPM, 32 / GraphicsConfig.PPM); // 设置物体的初始位置
        bodyDef.type = BodyDef.BodyType.DynamicBody; // 设置物体为动态刚体，即可被物理引擎模拟

        // 创建物理刚体并添加到Box2D世界中
        body = world.createBody(bodyDef);

        // 创建一个 FixtureDef，用于描述刚体的形状、密度、摩擦力和弹性等物理属性
        final FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.filter.categoryBits = BitDefinition.ENEMY_BIT;
        fixtureDef.filter.maskBits = BitDefinition.GROUND_BIT
            | BitDefinition.BRICK_BIT | BitDefinition.COIN_BIT
            | BitDefinition.ENEMY_BIT |BitDefinition.OBJECT_BIT;

        // 创建一个圆形的形状
        final CircleShape shape = new CircleShape();
        shape.setRadius(5 / GraphicsConfig.PPM); // 设置半径
        // 将圆形形状赋给 FixtureDef
        fixtureDef.shape = shape;

        // 将 FixtureDef 添加到物理刚体中，以应用其物理属性
        body.createFixture(fixtureDef);

        final EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / GraphicsConfig.PPM, 5 / GraphicsConfig.PPM), new Vector2(2 / GraphicsConfig.PPM, 5 / GraphicsConfig.PPM));
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData("head");
    }
}
