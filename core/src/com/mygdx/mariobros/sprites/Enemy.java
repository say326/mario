package com.mygdx.mariobros.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.mariobros.screens.GameScreen;


public abstract class Enemy extends Sprite {
    protected World world;
    protected GameScreen screen;
    public Body body;

    public Enemy(GameScreen gameScreen, float x, float y) {
        this.world = gameScreen.getWorld();
        this.screen = gameScreen;
        setPosition(x, y);
    }

    protected abstract void defineEnemy();
}
