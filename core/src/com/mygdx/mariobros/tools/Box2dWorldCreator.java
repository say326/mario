package com.mygdx.mariobros.tools;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.mariobros.MarioBros;
import com.mygdx.mariobros.sprites.Brick;
import com.mygdx.mariobros.sprites.Coin;

import java.util.function.Consumer;

public class Box2dWorldCreator {

    private World world;

    private TiledMap map;

    public Box2dWorldCreator(final World world, final TiledMap map) {
        this.world = world;
        this.map = map;
        batchCreateStaticBody(2, null);
        batchCreateStaticBody(3, null);
        batchCreateStaticBody(5, rectangleMapObject -> {
            final Rectangle rect = rectangleMapObject.getRectangle();
            new Brick(world, map, rect);
        });
        batchCreateStaticBody(4, rectangleMapObject -> {
            final Rectangle rect = rectangleMapObject.getRectangle();
            new Coin(world, map, rect);
        });
    }

    /**
     * 批量创建静态刚体
     *
     * @param index 所在图层的索引
     */
    private void batchCreateStaticBody(final int index, final Consumer<RectangleMapObject> consumer) {
        // 创建物体定义对象
        final BodyDef bodyDef = new BodyDef();

        // 创建形状对象
        final PolygonShape shape = new PolygonShape();

        // 创建夹具定义对象
        final FixtureDef fixtureDef = new FixtureDef();

        final Consumer<RectangleMapObject> defaultFunc = (rectangleMapObject) ->  {
            // 获取矩形对象的位置和尺寸
            final Rectangle rect = rectangleMapObject.getRectangle();
            final float halfWidth = rect.getWidth() / 2; //获取半宽
            final float halfHeight = rect.getHeight() / 2; //获取半高

            // 设置为静态物体
            bodyDef.type = BodyDef.BodyType.StaticBody;
            // 设置物体重心
            bodyDef.position.set((rect.getX() + halfWidth) / MarioBros.PPM, (rect.getY() + halfHeight) / MarioBros.PPM);
            // 创建刚体
            final Body body = world.createBody(bodyDef);
            // 设置形状
            shape.setAsBox(halfWidth / MarioBros.PPM, halfHeight / MarioBros.PPM);
            fixtureDef.shape = shape;
            // 创建夹具
            body.createFixture(fixtureDef);
        };

        final Consumer<RectangleMapObject> createFunc = consumer != null ? consumer : defaultFunc;

        // 获取所有矩形对象，并对其进行处理
        map.getLayers().get(index)
            .getObjects()
            .getByType(RectangleMapObject.class)
            .forEach(createFunc);
    }
}
