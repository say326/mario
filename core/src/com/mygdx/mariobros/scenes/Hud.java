package com.mygdx.mariobros.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.mariobros.config.GraphicsConfig;
import com.mygdx.mariobros.constants.LabelStyle;

/**
 * Hud类用于实现平视显示器，即屏幕上方的计分板和状态栏等UI元素。
 * <p>
 * {@memo} Hud是游戏中的头部用户界面，用于显示游戏中的重要信息，例如剩余时间、得分等。
 */
public class Hud implements Disposable {
    /**
     * 舞台，用于显示UI元素。
     */
    public final Stage stage;

    /**
     * 世界计时器，单位为秒。
     */
    private Integer worldTimer = 300;

    /**
     * 时间计数器，用于更新世界计时器。
     */
    private float timeCount = 0;

    /**
     * 分数，用于记录玩家的得分。
     */
    private static Integer score = 0;

    /**
     * 倒计时标题，显示世界计时器的倒计时。
     */
    private final Label countdownLabel = new Label(String.format("%3d", worldTimer), LabelStyle.WHITE);

    /**
     * 分数标题，显示当前玩家的分数。
     */
    private static final Label scoreLabel = new Label(String.format("%06d", score), LabelStyle.WHITE);

    public Hud(final SpriteBatch batch) {
        final Viewport viewPort = new FitViewport(
            GraphicsConfig.V_WIDTH,
            GraphicsConfig.V_HEIGHT,
            new OrthographicCamera()
        );
        stage = new Stage(viewPort, batch);
        addTable();
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

    private Label createLabel(final String text) {
        return new Label(text, LabelStyle.WHITE);
    }

    private void addTable() {
        final Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(createLabel("MARIO")).expandX().padTop(10);
        table.add(createLabel("WORLD")).expandX().padTop(10);
        table.add(createLabel("TIME")).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(createLabel("1-1")).expandX();
        table.add(countdownLabel).expandX();
        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
