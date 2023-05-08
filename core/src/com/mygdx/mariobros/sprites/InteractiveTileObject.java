package com.mygdx.mariobros.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.mariobros.MarioBros;


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

    protected InteractiveTileObject(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        final BodyDef bodyDef = new BodyDef();
        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape shape = new PolygonShape();

        final float halfWidth = bounds.getWidth() / 2; //获取半宽
        final float halfHeight = bounds.getHeight() / 2; //获取半高

        // 设置为静态物体
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // 设置物体重心
        bodyDef.position.set((bounds.getX() + halfWidth) / MarioBros.PPM, (bounds.getY() + halfHeight) / MarioBros.PPM);
        // 创建刚体
        body = world.createBody(bodyDef);
        // 设置形状
        shape.setAsBox(halfWidth / MarioBros.PPM, halfHeight / MarioBros.PPM);
        fixtureDef.shape = shape;
        // 创建夹具
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
            (int)(body.getPosition().x * MarioBros.PPM / 16),
            (int)(body.getPosition().y * MarioBros.PPM / 16)
        );
    }
}
