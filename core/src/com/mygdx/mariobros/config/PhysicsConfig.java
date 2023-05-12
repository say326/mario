package com.mygdx.mariobros.config;

/**
 * 物理引擎配置
 */
public class PhysicsConfig {
    private PhysicsConfig() {
    }

    /**
     * 时间步长，物理世界更新间隔
     */
    public static final float TIME_STEP = 1 / 60f;

    /**
     * 速度迭代频率，频率越高计算越精准，但是相对应的，性能消耗也会增加
     */
    public static final int VELOCITY_ITERATIONS = 6;

    /**
     * 位置计算频率，频率越高计算越精准，但是相对应的，性能消耗也会增加
     */
    public static final int POSITION_ITERATIONS = 2;
}
