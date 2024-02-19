package io.xlorey.ads.handlers;

import io.xlorey.ads.Main;
import io.xlorey.fluxloader.events.OnServerInitialize;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Author: Deknil
 * Date: 19.02.2024
 * GitHub: <a href="https://github.com/Deknil">https://github.com/Deknil</a>
 * Description: Handling the server initialization event
 * <p> xLoreyAds © 2024. All rights reserved. </p>
 */
public class OnServerInitHandler extends OnServerInitialize {
    @Override
    public void handleEvent() {
        Main.scheduler = Executors.newSingleThreadScheduledExecutor();
        Main.scheduler.scheduleAtFixedRate(Main::showAds, 0, Main.getDefaultConfig().getInt("adsTime"), TimeUnit.MINUTES);

    }
}
