package com.splashinghelper;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;

import java.awt.*;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

public class SplashingHelperOverlay extends OverlayPanel {

    private final Client client;
    private final SplashingHelperPlugin plugin;

    @Inject
    private SplashingHelperOverlay(Client client, SplashingHelperPlugin plugin)
    {
        super(plugin);
        setPosition(OverlayPosition.TOP_LEFT);
        this.client = client;
        this.plugin = plugin;
        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Cooking overlay"));
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        return null;

        //return super.render(graphics);
    }
}
