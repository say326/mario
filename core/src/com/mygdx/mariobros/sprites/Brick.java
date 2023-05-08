package com.mygdx.mariobros.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.mariobros.MarioBros;
import com.mygdx.mariobros.scenes.Hud;

public class Brick extends InteractiveTileObject {

    public Brick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("LOG NOW...", "mario's head is hitting brick now");
        setCategoryFilter(MarioBros.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.getScore(200);
        MarioBros.assetManager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
