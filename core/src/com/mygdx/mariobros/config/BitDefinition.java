package com.mygdx.mariobros.config;

/**
 * 这些 bit 是用于 Box2D 物理引擎中的碰撞检测的 categoryBits 和 maskBits 属性的定义。
 * categoryBits 属性表示该物体所属的类别，而 maskBits 属性表示该物体可以与哪些类别的物体发生碰撞。
 * 例如，如果一个对象的 categoryBits 为 GROUND_BIT，它可以与 maskBits 中包含 GROUND_BIT 的物体发生碰撞，而与其他物体不会发生碰撞。
 */
public class BitDefinition {
    /**
     * 私有构造
     */
    private BitDefinition() {
    }

    /**
     * 表示地面，主要用于 其他物体 与 地面 的碰撞检测。
     */
    public static final short GROUND_BIT = 1;

    /**
     * 表示玩家马里奥，主要用于 Mario 与 其他物体 的碰撞检测。
     */
    public static final short MARIO_BIT = 2;

    /**
     * 表示砖块，主要用于 其他物体 与 砖块 的碰撞检测。
     */
    public static final short BRICK_BIT = 4;

    /**
     * 表示金币，主要用于 其他物体 与 金币 的碰撞检测。
     */
    public static final short COIN_BIT = 8;

    /**
     * 表示被摧毁的砖块，主要用于 被摧毁的砖块 与 其他物体 的碰撞检测
     */
    public static final short DESTROYED_BIT = 16;

    /**
     * 表示实体类型，如管道、楼梯砖等，主要用于 实体类型物体 与 其他物体 的碰撞检测。
     */
    public static final short OBJECT_BIT = 32;

    /**
     * 表示敌人，主要用于 敌人 与 其他物体 的碰撞检测。
     */
    public static final short ENEMY_BIT = 64;
}
