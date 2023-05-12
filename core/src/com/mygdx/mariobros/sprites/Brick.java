package com.mygdx.mariobros.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.mygdx.mariobros.config.BitDefinition;
import com.mygdx.mariobros.scenes.Hud;
import com.mygdx.mariobros.screens.GameScreen;

/**
 * 砖块类，继承自InteractiveTileObject
 */
public class Brick extends InteractiveTileObject {

    /**
     * 砖块被摧毁时所能获取到的分数
     */
    private static final int DESTROY_SCORE = 200;

    /**
     * 构造函数，初始化Brick实例。
     *
     * @param screen GameScreen实例，硬币所在的游戏屏幕
     * @param bounds Rectangle实例，硬币所在的矩形范围
     */
    public Brick(final GameScreen screen, final RectangleMapObject bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(BitDefinition.BRICK_BIT);
    }

    /**
     * 处理头部碰撞事件，当玩家头部碰撞到砖块时被调用。
     */
    @Override
    public void onHeadHit() {
        setCategoryFilter(BitDefinition.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.getScore(DESTROY_SCORE);
        game.getAssetManager()
            .get("audio/sounds/breakblock.wav", Sound.class)
            .play();
    }
}

