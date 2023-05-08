package com.mygdx.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.mariobros.MarioBros;
import com.mygdx.mariobros.listeners.WorldContactListener;
import com.mygdx.mariobros.scenes.Hud;
import com.mygdx.mariobros.sprites.Mario;
import com.mygdx.mariobros.tools.Box2dWorldCreator;

/**
 * 游戏界面
 */
public class GameScreen extends ScreenAdapter {

    /**
     * 游戏
     */
    private MarioBros game;

    /**
     * 摄像头
     */
    private OrthographicCamera camera;

    /**
     * 视口
     */
    private Viewport viewport;

    /**
     * 平视显示器内容
     */
    private Hud hud;

    /**
     * 地图加载器
     */
    private TmxMapLoader mapLoader;

    /**
     * 地图
     */
    private TiledMap map;

    /**
     * 地图渲染器
     */
    private OrthogonalTiledMapRenderer mapRenderer;

    /**
     * Box2D
     */
    private World world;

    /**
     * 渲染Box2D调试线条
     */
    private Box2DDebugRenderer debugRenderer;

    /**
     * 马里奥
     */
    private Mario mario;

    /**
     * 图集
     */
    private TextureAtlas atlas;

    private Music music;

    public TextureAtlas getAtlas() {
        return atlas;
    }

    /**
     * 构建游戏屏幕 并在此过程中完成游戏界面的渲染
     *
     * @param game 游戏主题
     */
    public GameScreen(final MarioBros game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;

        // 创建摄像头
        camera = new OrthographicCamera();

        // 创建自适应视口，使游戏在不同大小屏幕上显示效果一致
        viewport = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, camera);

        // 创建Hud对象并传入SpriteBatch对象
        hud = new Hud(game.batch);

        // 加载地图文件
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");

        // 创建地图渲染器
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);

        // 设置摄像头位置
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        // 创建世界，设定世界重力和是否使静态物体休眠
        world = new World(new Vector2(0, -10), true);

        // 创建Box2DDebugRenderer对象以便调试
        debugRenderer = new Box2DDebugRenderer();

        // 创建Mario对象
        mario = new Mario(world, this);

        world.setContactListener(new WorldContactListener());

        // 使世界中的物体实体化
        new Box2dWorldCreator(world, map);

        music = MarioBros.assetManager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render(float delta) {
        // 调用 update 方法，更新游戏逻辑
        update(delta);

        // 清空屏幕，准备渲染
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 渲染地图
        mapRenderer.render();

        // 渲染调试线条，方便调试
        debugRenderer.render(world, camera.combined);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        mario.draw(game.batch);
        game.batch.end();

        // 渲染 HUD
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    /**
     * 每帧处理时的页面更新逻辑
     */
    public void update(final float deltaTime) {
        // 处理输入事件
        handleInput();

        // 时间步长 即更新物理世界的时间间隔。
        final float timeStep = 1 / 60f;

        // 速度迭代次数 即物理引擎在计算速度时迭代的次数。迭代次数越多，计算结果越精确，但运算时间也会增加
        final int velocityIterations = 6;

        // 位置迭代次数 即物理引擎在计算位置时迭代的次数。与速度迭代次数类似，迭代次数越多，计算结果越精确，但运算时间也会增加
        final int positionIterations = 2;

        // 更新物理世界 迈出一步。这将执行碰撞检测、集成和约束解决方案。
        world.step(timeStep, velocityIterations, positionIterations);

        // 更新马里奥位置
        mario.update(deltaTime);

        hud.update(deltaTime);

        // 更新摄像头位置
        camera.position.x = mario.body.getPosition().x;
        camera.update();

        // 根据摄像头坐标定位地图位置
        mapRenderer.setView(camera);
    }

    /**
     * 处理玩家输入事件
     */
    private void handleInput() {
        // 获取马里奥当前的物理世界中心位置
        final Vector2 worldCenter = mario.body.getWorldCenter();

        // 如果玩家按下了向上键
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && mario.body.getLinearVelocity().y == 0) {
            // 给马里奥一个向上的线性冲量，使其跳起来
            mario.body.applyLinearImpulse(new Vector2(0, 4f), worldCenter, true);
        }

        // 如果玩家按下了向右键，且马里奥当前的水平速度不超过2
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && mario.body.getLinearVelocity().x <= 2) {
            // 给马里奥一个向右的线性冲量，使其向右移动
            mario.body.applyLinearImpulse(new Vector2(0.1f, 0), worldCenter, true);
        }

        // 如果玩家按下了向左键，且马里奥当前的水平速度不超过2
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && mario.body.getLinearVelocity().x >= -2) {
            // 给马里奥一个向左的线性冲量，使其向左移动
            mario.body.applyLinearImpulse(new Vector2(-0.1f, 0), worldCenter, true);
        }
    }

    @Override
    public void dispose() {
        world.dispose();
        map.dispose();
        mapRenderer.dispose();
        debugRenderer.dispose();
        hud.dispose();
        atlas.dispose();
    }
}
