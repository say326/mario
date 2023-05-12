package com.mygdx.mariobros.listeners;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.mariobros.sprites.InteractiveTileObject;

/**
 * 碰撞事件监听器
 */
public class WorldContactListener implements ContactListener {

    /**
     * 马里奥的大脑袋标记 用于区分当前 {@link Fixture} 是不是马里奥的脑袋
     */
    private static final String HEAD_TAG = "head";

    /**
     * 处理碰撞事件
     *
     * @param contact 碰撞接触对象
     */
    @Override
    public void beginContact(final Contact contact) {
        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();
        if (fixtureA.getUserData() == HEAD_TAG || fixtureB.getUserData() == HEAD_TAG) {
            final Fixture object = fixtureA.getUserData().equals(HEAD_TAG) ? fixtureB : fixtureA;
            final Object userData = object.getUserData();
            if (userData != null && InteractiveTileObject.class.isAssignableFrom(userData.getClass())){
                ((InteractiveTileObject) userData).onHeadHit();
            }
        }
    }


    @Override
    public void endContact(Contact contact) {
        // TODO 暂未使用
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO 暂未使用
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO 暂未使用
    }
}
