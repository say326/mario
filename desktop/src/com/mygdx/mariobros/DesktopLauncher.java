package com.mygdx.mariobros;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * 主要启动类，用于启动 Mario Bros 游戏桌面版本。
 * 在 macOS 上，需要使用 -XstartOnFirstThread JVM 参数启动应用程序。
 */
public class DesktopLauncher {

    public static void main(String[] arg) {
        final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60); // 设置前台帧率为60帧/秒
        config.setTitle("Mario Bros"); // 设置窗口标题为"Mario Bros"
        new Lwjgl3Application(new MarioBros(), config); // 创建游戏窗口并运行
    }

}
