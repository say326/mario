package com.mygdx.mariobros.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.mariobros.MarioBros;


public class Coin extends InteractiveTileObject {

    private static TiledMapTileSet tileSet;

    private final int BLANK_COIN_ID = 28;

    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("LOG NOW...", "mario's head is hitting coin now");
        if (getCell().getTile().getId() == BLANK_COIN_ID)
            MarioBros.assetManager.get("audio/sounds/bump.wav", Sound.class).play();
        else MarioBros.assetManager.get("audio/sounds/coin.wav", Sound.class).play();
        getCell().setTile(tileSet.getTile(BLANK_COIN_ID));
    }
}
