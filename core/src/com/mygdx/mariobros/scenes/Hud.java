package com.mygdx.mariobros.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.mariobros.MarioBros;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Hud类用于实现平视显示器，即屏幕上方的计分板和状态栏等UI元素。
 * <p>
 * {@memo} Hud是游戏中的头部用户界面，用于显示游戏中的重要信息，例如剩余时间、得分等。
 */
public class Hud implements Disposable {
    /**
     * 舞台，用于显示UI元素。
     */
    public Stage stage;

    /**
     * 视口，用于控制UI元素的大小和位置。
     */
    private Viewport viewPort;

    /**
     * 世界计时器，单位为秒。
     */
    private Integer worldTimer;

    /**
     * 时间计数器，用于更新世界计时器。
     */
    private float timeCount;

    /**
     * 分数，用于记录玩家的得分。
     */
    private static Integer score;

    /**
     * 倒计时标题，显示世界计时器的倒计时。
     */
    private Label countdownLabel;

    /**
     * 分数标题，显示当前玩家的分数。
     */
    private static Label scoreLabel;

    /**
     * 时间标题，标明该UI元素显示的是时间信息。
     */
    private Label timeLabel;

    /**
     * 级别标题，显示当前游戏关卡的级别。
     */
    private Label levelLabel;

    /**
     * 世界标题，标明该UI元素显示的是世界信息。
     */
    private Label worldLabel;

    /**
     * 马里奥标题，标明该UI元素显示的是玩家角色信息。
     */
    private Label marioLabel;

    /**
     * Hud 类是游戏屏幕上方的一个 UI（用户界面）层，用于显示时间、分数、当前游戏世界等信息。
     * 在构造方法中，初始化了世界计时器、时间计数器和分数，设置了 UI 层的布局和样式，并添加到舞台中以显示在屏幕上方。
     *
     * @param batch 用于绘制 HUD 的 SpriteBatch 对象
     */
    public Hud(final SpriteBatch batch) {
        // 初始化计时器、得分和时间
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        // 创建视口和舞台
        viewPort = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, batch);

        // 创建一个表格，设置对齐方式和填充方式
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        // 创建标签，设置格式和颜色
        countdownLabel = new Label(String.format("%3d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // 将标签添加到表格中，并设置填充和间距
        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        // 将表格添加到舞台
        stage.addActor(table);
    }

    public void update(float delta) {
        timeCount += delta;
        if (timeCount >= 1) {
            worldTimer--;
            countdownLabel.setText(String.format("%3d", worldTimer));
            timeCount = 0;
        }
    }

    public static void getScore(int value) {
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
