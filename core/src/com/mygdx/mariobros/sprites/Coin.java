package com.mygdx.mariobros.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.mygdx.mariobros.config.BitDefinition;
import com.mygdx.mariobros.screens.GameScreen;


/**
 * 硬币类，继承自InteractiveTileObject
 */
public class Coin extends InteractiveTileObject {

    /**
     * 地图集合
     */
    private final TiledMapTileSet tileSet;

    /**
     * 空硬币地图块id
     */
    private static final int BLANK_COIN_ID = 28;

    /**
     * 构造函数，初始化Coin实例。
     *
     * @param screen GameScreen实例，硬币所在的游戏屏幕
     * @param bounds Rectangle实例，硬币所在的矩形范围
     */
    public Coin(final GameScreen screen, final RectangleMapObject bounds) {
        super(screen, bounds);
        // 获取当前地图的地图集合
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        // 设置刚体用户数据
        fixture.setUserData(this);
        // 设置硬币的过滤器，用于在碰撞检测中识别硬币
        setCategoryFilter(BitDefinition.COIN_BIT);
    }

    /**
     * 处理头部碰撞事件，当玩家头部碰撞到硬币时被调用。
     */
    @Override
    public void onHeadHit() {
        // 获取游戏资源管理器
        final AssetManager assetManager = game.getAssetManager();
        // 获取当前硬币所在的地图块
        final TiledMapTileLayer.Cell cell = getCell();

        if (cell.getTile().getId() == BLANK_COIN_ID) {
            // 如果硬币块已经被碰撞过了，播放碰撞音效
            assetManager.get("audio/sounds/bump.wav", Sound.class).play();
        } else {
            // 如果硬币块还没被碰撞过，播放金币音效
            assetManager.get("audio/sounds/coin.wav", Sound.class).play();
            // 将当前块的纹理变更为被碰撞过的金币
            cell.setTile(tileSet.getTile(BLANK_COIN_ID));
        }
    }
}
