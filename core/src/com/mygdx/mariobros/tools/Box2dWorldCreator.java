package com.mygdx.mariobros.tools;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.mariobros.screens.GameScreen;
import com.mygdx.mariobros.sprites.Brick;
import com.mygdx.mariobros.sprites.Coin;
import com.mygdx.mariobros.sprites.StaticObject;

import java.util.function.Consumer;

public class Box2dWorldCreator {

    public Box2dWorldCreator(final GameScreen gameScreen) {
        final TiledMap map = gameScreen.getMap();
        batchCreateStaticBody(2, rectangleMapObject -> new StaticObject(gameScreen, rectangleMapObject), map);
        batchCreateStaticBody(3, rectangleMapObject -> new StaticObject(gameScreen, rectangleMapObject), map);
        batchCreateStaticBody(5, rectangleMapObject -> new Brick(gameScreen, rectangleMapObject), map);
        batchCreateStaticBody(4, rectangleMapObject -> new Coin(gameScreen, rectangleMapObject), map);
    }

    /**
     * 批量创建静态刚体
     *
     * @param index 所在图层的索引
     */
    private void batchCreateStaticBody(final int index, final Consumer<RectangleMapObject> consumer,
                                       final TiledMap map) {
        map.getLayers().get(index)
            .getObjects()
            .getByType(RectangleMapObject.class)
            .forEach(consumer);
    }
}
