package com.mygdx.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.mariobros.MarioBros;
import com.mygdx.mariobros.config.GraphicsConfig;
import com.mygdx.mariobros.config.PhysicsConfig;
import com.mygdx.mariobros.listeners.WorldContactListener;
import com.mygdx.mariobros.scenes.Hud;
import com.mygdx.mariobros.sprites.Mario;
import com.mygdx.mariobros.tools.Box2dWorldCreator;
import lombok.Getter;

/**
 * 游戏界面
 */
@Getter
public class GameScreen extends ScreenAdapter {

    /**
     * 游戏
     */
    private final MarioBros game;
    /**
     * 平视显示器内容
     */
    private final Hud hud;
    /**
     * 渲染Box2D调试线条
     */
    private final Box2DDebugRenderer debugRenderer;
    /**
     * 资源管理器
     */
    private final AssetManager assetManager;
    /**
     * 图集
     */
    private final TextureAtlas atlas;
    /**
     * 摄像头
     */
    private OrthographicCamera camera;
    /**
     * 视口
     */
    private Viewport viewport;
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
     * 马里奥
     */
    private Mario mario;
    /**
     * 音乐
     */
    private Music music;

    public MarioBros getGame() {
        return game;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    /**
     * 构建游戏屏幕 并在此过程中完成游戏界面的渲染
     *
     * @param game 游戏主题
     */
    public GameScreen(final MarioBros game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        hud = new Hud(game.getBatch());
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        debugRenderer = new Box2DDebugRenderer();
        initCamera();
        loadMap();
        initBox2DWorld();
        initMusic();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        debugRenderer.render(world, camera.combined);
        final SpriteBatch batch = game.getBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        mario.draw(batch);
        batch.end();
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height);
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

    /**
     * 每帧处理时的页面更新逻辑
     */
    public void update(final float deltaTime) {
        handleInput();
        world.step(
            PhysicsConfig.TIME_STEP,
            PhysicsConfig.VELOCITY_ITERATIONS,
            PhysicsConfig.POSITION_ITERATIONS
        );
        mario.update(deltaTime);
        hud.update(deltaTime);
        camera.position.x = mario.body.getPosition().x;
        camera.update();
        mapRenderer.setView(camera);
    }

    /**
     * 处理玩家输入事件
     */
    private void handleInput() {
        final Vector2 worldCenter = mario.body.getWorldCenter();
        final Vector2 linearVelocity = mario.body.getLinearVelocity();
        final Body body = mario.body;
        final Input input = Gdx.input;
        if (input.isKeyJustPressed(Input.Keys.UP) && linearVelocity.y == 0) {
            body.applyLinearImpulse(new Vector2(0, 4f), worldCenter, true);
        } else if (input.isKeyPressed(Input.Keys.RIGHT) && linearVelocity.x <= 2) {
            body.applyLinearImpulse(new Vector2(0.1f, 0), worldCenter, true);
        } else if (input.isKeyPressed(Input.Keys.LEFT) && linearVelocity.x >= -2) {
            body.applyLinearImpulse(new Vector2(-0.1f, 0), worldCenter, true);
        }
    }

    /**
     * 相机初始化
     */
    private void initCamera() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(
            GraphicsConfig.V_WIDTH / GraphicsConfig.PPM,
            GraphicsConfig.V_HEIGHT / GraphicsConfig.PPM,
            camera
        );
        camera.position.set(
            viewport.getWorldWidth() / 2,
            viewport.getWorldHeight() / 2,
            0
        );
    }

    /**
     * 地图初始化
     */
    private void loadMap() {
        final TmxMapLoader tmxMapLoader = new TmxMapLoader();
        map = tmxMapLoader.load("level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / GraphicsConfig.PPM);
    }

    /**
     * 物理世界初始化
     */
    private void initBox2DWorld() {
        world = new World(new Vector2(0, -10), true);
        mario = new Mario(this);
        world.setContactListener(new WorldContactListener());
        new Box2dWorldCreator(this);
    }

    /**
     * 音乐初始化
     */
    private void initMusic() {
        music = assetManager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();
    }
}
