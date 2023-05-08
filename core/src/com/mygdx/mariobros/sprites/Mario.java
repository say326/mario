package com.mygdx.mariobros.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.mariobros.MarioBros;
import com.mygdx.mariobros.enums.States;
import com.mygdx.mariobros.screens.GameScreen;

import java.util.stream.Stream;

/**
 * 马里奥精灵类
 * <p>
 * {@memo} 刚体是物理引擎中的一个概念，指的是不会发生形变的物体。
 * 在二维游戏中，刚体可以理解为由多个形状（如圆形、矩形、多边形等）组成的不可变的物体，可以受到力和碰撞的影响，而且运动速度、方向和旋转角度都是可变的。在Box2D中，刚体是通过Body和Fixture两个类来表示的。
 * 其中，Body表示刚体的基本属性，如位置、速度、角速度等，而Fixture表示刚体的形状和物理特性，如密度、摩擦力等。
 */
public class Mario extends Sprite {

    /**
     * 当前状态
     */
    public States currentState;

    /**
     * 先前状态
     */
    public States previousState;

    /**
     * 马里奥奔跑动画
     */
    private Animation<TextureRegion> marioRun;

    /**
     * 马里奥跳跃动画
     */
    private Animation<TextureRegion> marioJump;

    /**
     * 马里奥动画的播放时间
     */
    private float stateTimer;

    /**
     * 是否面朝右边
     */
    private boolean runningRight;

    /**
     * Box2D物理世界
     */
    public World world;

    /**
     * 马里奥的物理刚体
     */
    public Body body;

    /**
     * 马里奥站立状态的纹理
     */
    private TextureRegion marioStand;

    /**
     * 构造函数，用于初始化马里奥的物理刚体
     *
     * @param world Box2D世界对象
     */
    public Mario(World world, GameScreen gameScreen) {
        super(gameScreen.getAtlas().findRegion("little_mario"));
        this.world = world;

        // 设置初始状态为STANDING，计时器为0，纹理面向右侧
        currentState = States.STANDING;
        previousState = States.STANDING;
        stateTimer = 0;
        runningRight = true;

        // 创建marioRun动画
        marioRun = createAnimation(1, 3);

        // 创建marioJump动画
        marioJump = createAnimation(4, 2);

        // 创建marioStand纹理
        marioStand = new TextureRegion(getTexture(), 0, 11, 16, 16);

        // 定义Mario的物理属性
        defineMario();

        // 设置Mario的边界和初始纹理
        setBounds(0, 0, 16 / MarioBros.PPM, 16 / MarioBros.PPM);
        setRegion(marioStand);
    }

    /**
     * 根据当前物理世界中的刚体状态，更新马里奥在屏幕上的位置和纹理帧
     *
     * @param deltaTime 经过的时间，以秒为单位
     */
    public void update(float deltaTime) {
        // 更新马里奥在屏幕上的位置，使其与物理世界中的刚体位置保持同步
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        // 更新马里奥的纹理帧
        setRegion(getFrame(deltaTime));
    }

    /**
     * 获取当前帧的纹理
     *
     * @param deltaTime 经过时间
     * @return 当前帧纹理
     */
    public TextureRegion getFrame(float deltaTime) {
        // 获取当前状态
        currentState = getState();
        // 创建一个TextureRegion变量用于存储当前状态的纹理
        TextureRegion textureRegion;
        // 根据当前状态获取相应的纹理
        switch (currentState) {
            case JUMPING:
                // 如果状态为跳跃，则获取跳跃动画的当前帧 由于正常来说人物在空中不能二次起跳 将looping置为false
                textureRegion = marioJump.getKeyFrame(stateTimer, false);
                break;
            case RUNNING:
                // 如果状态为奔跑，则获取奔跑动画的当前帧
                textureRegion = marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                // 如果状态为下落、站立或其他，则使用站立的纹理
                textureRegion = marioStand;
                break;
        }
        // 如果Mario正在向左运动且当前纹理未翻转，则翻转纹理并将runningRight设置为false
        if ((body.getLinearVelocity().x < 0 || !runningRight) && !textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            runningRight = false;
            // 如果Mario正在向右运动且当前纹理已翻转，则翻转纹理回去并将runningRight设置为true
        } else if ((body.getLinearVelocity().x > 0 || runningRight) && textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            runningRight = true;
        }

        // 更新状态计时器
        stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;
        // 更新前一个状态为当前状态
        previousState = currentState;
        // 返回当前纹理
        return textureRegion;
    }

    /**
     * 根据Mario的物理状态来判断当前运动状态
     *
     * @return 当前的运动状态
     */
    public States getState() {
        // 如果Mario的垂直速度大于0，或者上一帧的状态为JUMPING且垂直速度不为0，说明Mario正在跳跃
        if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y > 0 && previousState == States.JUMPING)) {
            return States.JUMPING;
        }
        // 如果Mario的垂直速度小于0，说明Mario正在下落
        else if (body.getLinearVelocity().y < 0) {
            return States.FALLING;
        }
        // 如果Mario的水平速度不为0，说明Mario正在奔跑
        else if (body.getLinearVelocity().x != 0) {
            return States.RUNNING;
        }
        // 如果以上情况都不符合，说明Mario正在站立
        else {
            return States.STANDING;
        }
    }

    /**
     * 定义马里奥的物理刚体
     */
    private void defineMario() {
        // 创建一个物体定义（BodyDef），用于描述物体的物理属性
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM); // 设置物体的初始位置
        bodyDef.type = BodyDef.BodyType.DynamicBody; // 设置物体为动态刚体，即可被物理引擎模拟

        // 创建物理刚体并添加到Box2D世界中
        body = world.createBody(bodyDef);

        // 创建一个 FixtureDef，用于描述刚体的形状、密度、摩擦力和弹性等物理属性
        final FixtureDef fixtureDef = new FixtureDef();

        // 创建一个圆形的形状
        final CircleShape shape = new CircleShape();
        shape.setRadius(5 / MarioBros.PPM); // 设置半径
        fixtureDef.filter.categoryBits = MarioBros.MARIO_BIT;
        fixtureDef.filter.maskBits = MarioBros.DEFAULT_BIT | MarioBros.BRICK_BIT | MarioBros.COIN_BIT;

        // 将圆形形状赋给 FixtureDef
        fixtureDef.shape = shape;

        // 将 FixtureDef 添加到物理刚体中，以应用其物理属性
        body.createFixture(fixtureDef);

        final EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 5 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 5 / MarioBros.PPM));
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData("head");
    }

    /**
     * 创建一个由一系列帧组成的动画
     *
     * @param startFrame 动画的起始帧索引
     * @param limit      动画的帧数
     * @return 创建的动画对象 {@link Animation}
     */
    private Animation<TextureRegion> createAnimation(int startFrame, int limit) {
        // 创建一个空的 TextureRegion 数组
        final Array<TextureRegion> frames = new Array<>();
        // 使用 Stream.iterate 方法生成一个从 startFrame 开始、步长为 1 的整数序列，再使用 limit 方法限制序列长度
        Stream.iterate(startFrame, i -> i + 1)
            .limit(limit)
            // 遍历整数序列，并将每个整数 i 转换为对应的 TextureRegion 对象，添加到 frames 数组中
            .map(i -> new TextureRegion(getTexture(), i * 16, 11, 16, 16))
            .forEach(frames::add);
        // 创建一个以 0.1 秒为间隔的 Animation<TextureRegion> 对象，使用 frames 数组中的纹理作为帧
        return new Animation<>((float) 0.1, frames);
    }
}
