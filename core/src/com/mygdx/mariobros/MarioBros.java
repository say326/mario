package com.mygdx.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.mariobros.screens.GameScreen;

/**
 * MarioBros是游戏的主类，继承自LibGDX中的Game类
 */
public class MarioBros extends Game {

    /**
     * 游戏视口的宽度
     */
    public static final int V_WIDTH = 400;

    /**
     * 游戏视口的高度
     */
    public static final int V_HEIGHT = 220;

    /**
     * 每米所代表的像素数
     */
    public static final float PPM = 100;

    public static final short DEFAULT_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;

    /**
     * 渲染游戏所使用的SpriteBatch
     */
    public SpriteBatch batch;

    public static AssetManager assetManager;

    /**
     * 游戏启动时的初始化方法
     */
    @Override
    public void create() {
        // 创建一个新的SpriteBatch对象
        batch = new SpriteBatch();

        assetManager = new AssetManager();
        assetManager.load("audio/music/mario_music.ogg", Music.class);
        assetManager.load("audio/sounds/coin.wav", Sound.class);
        assetManager.load("audio/sounds/bump.wav", Sound.class);
        assetManager.load("audio/sounds/breakblock.wav", Sound.class);
        assetManager.finishLoading();

        // 设置游戏启动时的屏幕（即第一个显示的画面）为游戏中的GameScreen
        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
//        if (assetManager.update()) {
//
//        }
    }

    /**
     * 游戏结束时的清理方法
     */
    @Override
    public void dispose() {
        // 释放占用的资源，这里我们不需要释放batch对象，因为它是我们在create方法中创建的，
        // 当游戏结束时会自动被销毁。如果我们使用了其他资源，比如纹理、声音等，需要在这里进行释放。
        batch.dispose();
        assetManager.dispose();
    }
}
