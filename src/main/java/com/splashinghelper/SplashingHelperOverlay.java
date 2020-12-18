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
         session = plugin.getSession();
        if (session == null)
        {
            return null;
        }

        if (isCooking() || Duration.between(session.getLastCookingAction(), Instant.now()).getSeconds() < COOK_TIMEOUT)
        {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Cooking")
                    .color(Color.GREEN)
                    .build());
        }
        else
        {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("NOT cooking")
                    .color(Color.RED)
                    .build());
        }

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Cooked:")
                .right(session.getCookAmount() + (session.getCookAmount() >= 1 ? " (" + xpTrackerService.getActionsHr(Skill.COOKING) + "/hr)" : ""))
                .build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Burnt:")
                .right(session.getBurnAmount() + (session.getBurnAmount() >= 1 ? " (" + FORMAT.format(session.getBurntPercentage()) + "%)" : ""))
                .build());

        return super.render(graphics);
    }
}
