package com.mygdx.mariobros.sprites;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.mariobros.MarioBros;
import com.mygdx.mariobros.config.GraphicsConfig;
import com.mygdx.mariobros.screens.GameScreen;


/**
 * 交互式图块对象
 */
public abstract class InteractiveTileObject {

    protected World world;

    protected TiledMap map;

    protected TiledMapTile tile;

    protected Rectangle bounds;

    protected Fixture fixture;

    protected Body body;

    protected MarioBros game;


    protected InteractiveTileObject(final GameScreen screen, final RectangleMapObject mapObject) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.game = screen.getGame();
        this.bounds = mapObject.getRectangle();

        final float halfWidth = bounds.getWidth() / 2; //获取半宽
        final float halfHeight = bounds.getHeight() / 2; //获取半高

        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((bounds.getX() + halfWidth) / GraphicsConfig.PPM, (bounds.getY() + halfHeight) / GraphicsConfig.PPM);
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(halfWidth / GraphicsConfig.PPM, halfHeight / GraphicsConfig.PPM);
        fixtureDef.shape = shape;

        fixture = body.createFixture(fixtureDef);
    }

    public abstract void onHeadHit();

    public void setCategoryFilter(final short filterBit) {
        final Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell() {
        final TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell(
            (int)(body.getPosition().x * GraphicsConfig.PPM / 16),
            (int)(body.getPosition().y * GraphicsConfig.PPM / 16)
        );
    }
}
