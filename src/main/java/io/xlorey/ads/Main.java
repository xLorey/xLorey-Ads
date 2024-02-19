package io.xlorey.ads;

import io.xlorey.ads.handlers.OnServerInitHandler;
import io.xlorey.fluxloader.plugin.Configuration;
import io.xlorey.fluxloader.plugin.Plugin;
import io.xlorey.fluxloader.server.api.ServerUtils;
import io.xlorey.fluxloader.shared.EventManager;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Plugin entry point
 */
public class Main extends Plugin {
    private static Main instance;
    public static ScheduledExecutorService scheduler;
    public static int currentAdIndex = 0;

    /**
     * Initializes the plugin and sets up the default configuration.
     */
    @Override
    public void onInitialize() {
        instance = this;

        EventManager.subscribe(new OnServerInitHandler());

        saveDefaultConfig();
    }

    /**
     * Getting the standard config
     * @return standard config
     */
    public static Configuration getDefaultConfig() {
        return instance.getConfig();
    }

    /**
     * Properly shuts down the plugin, including terminating the scheduler.
     */
    @Override
    public void onTerminate() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Displays an advertising message in the general chat.
     * Selects a random message from the list of advertisements and sends it to the server chat.
     */
    public static void showAds() {
        List<Object> adsList = getDefaultConfig().getList("adsMessages");
        if (adsList == null || adsList.isEmpty()) {
            return;
        }

        String adText = (String) adsList.get(currentAdIndex);

        String formattedText = getDefaultConfig().getString("adsMessageFormat")
                .replace("<TEXT>", adText)
                .replace("<SPACE_SYMBOL>", "\u200B");

        ServerUtils.sendServerChatMessage(formattedText);

        currentAdIndex = (currentAdIndex + 1) % adsList.size();
    }
}
