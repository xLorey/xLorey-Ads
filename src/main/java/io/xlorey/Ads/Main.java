package io.xlorey.Ads;

import io.xlorey.FluxLoader.annotations.SubscribeEvent;
import io.xlorey.FluxLoader.plugin.Plugin;
import io.xlorey.FluxLoader.server.api.ServerUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Plugin entry point
 */
public class Main extends Plugin {
    private static ScheduledExecutorService scheduler;
    private int currentAdIndex = 0;

    /**
     * Initializes the plugin and sets up the default configuration.
     */
    @Override
    public void onInitialize() {
        saveDefaultConfig();
    }

    /**
     * Handling the server initialization event
     */
    @SubscribeEvent(eventName="onServerInitialize")
    public void onServerInitializeHandler(){
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::showAds, 0, getConfig().getInt("adsTime"), TimeUnit.MINUTES);
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
    public void showAds() {
        List<Object> adsList = getConfig().getList("adsMessages");
        if (adsList == null || adsList.isEmpty()) {
            return;
        }

        String adText = (String) adsList.get(currentAdIndex);

        String formattedText = getConfig().getString("adsMessageFormat")
                .replace("<TEXT>", adText)
                .replace("<SPACE_SYMBOL>", "\u200B");

        ServerUtils.sendServerChatMessage(formattedText);

        currentAdIndex = (currentAdIndex + 1) % adsList.size();
    }
}
