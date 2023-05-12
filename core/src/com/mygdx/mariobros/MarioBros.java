package com.mygdx.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.mariobros.screens.GameScreen;
import com.mygdx.mariobros.tools.AssetLoader;

/**
 * MarioBros是游戏的主类，继承自LibGDX中的Game类
 */
public class MarioBros extends Game {

    /**
     * 渲染游戏所使用的SpriteBatch
     */
    private SpriteBatch batch;

    /**
     * 资源管理器
     */
    private AssetManager assetManager;

    public SpriteBatch getBatch() {
        return batch;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * 游戏启动时的初始化方法
     */
    @Override
    public void create() {
        // 创建一个新的SpriteBatch对象
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        AssetLoader.loadAssets(assetManager);
        // 设置游戏启动时的屏幕（即第一个显示的画面）为游戏中的GameScreen
        setScreen(new GameScreen(this));
    }

    /**
     * 游戏结束时的清理方法
     */
    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
    }
}
