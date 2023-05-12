package com.mygdx.mariobros.sprites;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.mygdx.mariobros.config.BitDefinition;
import com.mygdx.mariobros.screens.GameScreen;

/**
 * 静态物体类，继承自InteractiveTileObject
 */
public class StaticObject extends InteractiveTileObject {

    public StaticObject(final GameScreen screen, final RectangleMapObject bounds) {
        super(screen, bounds);
        setCategoryFilter(BitDefinition.OBJECT_BIT);
    }

    @Override
    public void onHeadHit() {
        //TODO 暂无碰撞事件
    }
}
