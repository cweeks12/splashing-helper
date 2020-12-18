package com.splashinghelper;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.http.api.item.ItemStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@PluginDescriptor(
	name = "Splashing Helper"
)
public class SplashingHelperPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private SplashingHelperConfig config;

	@Inject
	private SplashingHelperOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ItemManager itemManager;

	private int previousMagicBonus = -1;

	@Override
	protected void startUp()
	{
		log.info("Example started!");
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		log.info("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Provides
	SplashingHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SplashingHelperConfig.class);
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		int currentMagicBonus = getEquipmentMagicBonus();

		// Only do stuff if we change to save the CPU
		if (getEquipmentMagicBonus() > -64 || !autocastAvailable())
		{
			return;
		}

		if (config.enable()) {
			Map<RuneTypes, Integer> inventoryRunes = getInventoryRunes();

			for(RuneTypes rune : inventoryRunes.keySet())
			{
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", " " + inventoryRunes.get(rune), null);
			}
		}
	}

	private Map<RuneTypes, Integer> getInventoryRunes(){
		final ItemContainer container = client.getItemContainer(InventoryID.INVENTORY);

		Map<RuneTypes, Integer> runes = new HashMap<>();

		for (Item item : container.getItems())
		{
			switch (item.getId()){
				case ItemID.MIND_RUNE:
					runes.put(RuneTypes.MIND, item.getQuantity());
					break;
				case ItemID.CHAOS_RUNE:
					runes.put(RuneTypes.CHAOS, item.getQuantity());
					break;
				case ItemID.DEATH_RUNE:
					runes.put(RuneTypes.DEATH, item.getQuantity());
					break;
				case ItemID.BLOOD_RUNE:
					runes.put(RuneTypes.BLOOD, item.getQuantity());
					break;
				case ItemID.WRATH_RUNE:
					runes.put(RuneTypes.WRATH, item.getQuantity());
					break;
				case ItemID.SOUL_RUNE:
					runes.put(RuneTypes.SOUL, item.getQuantity());
					break;

				case ItemID.WATER_RUNE:
					runes.put(RuneTypes.WATER, item.getQuantity());
					break;
				case ItemID.AIR_RUNE:
					runes.put(RuneTypes.AIR, item.getQuantity());
					break;
				case ItemID.EARTH_RUNE:
					runes.put(RuneTypes.EARTH, item.getQuantity());
					break;
				case ItemID.FIRE_RUNE:
					runes.put(RuneTypes.FIRE, item.getQuantity());
					break;

				case ItemID.DUST_RUNE:
					runes.put(RuneTypes.DUST, item.getQuantity());
					break;
				case ItemID.SMOKE_RUNE:
					runes.put(RuneTypes.SMOKE, item.getQuantity());
					break;
				case ItemID.MIST_RUNE:
					runes.put(RuneTypes.MIST, item.getQuantity());
					break;
				case ItemID.MUD_RUNE:
					runes.put(RuneTypes.MUD, item.getQuantity());
					break;
				case ItemID.STEAM_RUNE:
					runes.put(RuneTypes.STEAM, item.getQuantity());
					break;
				case ItemID.LAVA_RUNE:
					runes.put(RuneTypes.LAVA, item.getQuantity());
					break;
			}
		}
		return runes;
	}

	private List<RuneTypes> getEquippedRunes(){

		List<RuneTypes> equippedInfiniteRunes = new ArrayList<>();

		final ItemContainer container = client.getItemContainer(InventoryID.EQUIPMENT);

		if (container == null)
		{
			return null;
		}

		Item[] equippedItems = container.getItems();

		if (EquipmentInventorySlot.SHIELD.getSlotIdx() < equippedItems.length)
		{
			switch (equippedItems[EquipmentInventorySlot.SHIELD.getSlotIdx()].getId())
			{
				case ItemID.TOME_OF_FIRE:
					equippedInfiniteRunes.add(RuneTypes.FIRE);
					break;
			}
		}

		if (EquipmentInventorySlot.WEAPON.getSlotIdx() < equippedItems.length)
		{
			switch(equippedItems[EquipmentInventorySlot.WEAPON.getSlotIdx()].getId())
			{
				case ItemID.STAFF_OF_AIR:
				case ItemID.AIR_BATTLESTAFF:
				case ItemID.MYSTIC_AIR_STAFF:
					equippedInfiniteRunes.add(RuneTypes.AIR);
					break;

				case ItemID.STAFF_OF_WATER:
				case ItemID.WATER_BATTLESTAFF:
				case ItemID.MYSTIC_WATER_STAFF:
					equippedInfiniteRunes.add(RuneTypes.WATER);
					break;

				case ItemID.STAFF_OF_EARTH:
				case ItemID.EARTH_BATTLESTAFF:
				case ItemID.MYSTIC_EARTH_STAFF:
					equippedInfiniteRunes.add(RuneTypes.EARTH);
					break;

				case ItemID.STAFF_OF_FIRE:
				case ItemID.FIRE_BATTLESTAFF:
				case ItemID.MYSTIC_FIRE_STAFF:
					equippedInfiniteRunes.add(RuneTypes.FIRE);
					break;

				case ItemID.STEAM_BATTLESTAFF:
				case ItemID.MYSTIC_STEAM_STAFF:
					equippedInfiniteRunes.add(RuneTypes.WATER);
					equippedInfiniteRunes.add(RuneTypes.FIRE);
					break;

				case ItemID.LAVA_BATTLESTAFF:
				case ItemID.MYSTIC_LAVA_STAFF:
					equippedInfiniteRunes.add(RuneTypes.EARTH);
					equippedInfiniteRunes.add(RuneTypes.FIRE);
					break;

				case ItemID.MIST_BATTLESTAFF:
				case ItemID.MYSTIC_MIST_STAFF:
					equippedInfiniteRunes.add(RuneTypes.WATER);
					equippedInfiniteRunes.add(RuneTypes.AIR);
					break;

				case ItemID.SMOKE_BATTLESTAFF:
				case ItemID.MYSTIC_SMOKE_STAFF:
					equippedInfiniteRunes.add(RuneTypes.AIR);
					equippedInfiniteRunes.add(RuneTypes.FIRE);
					break;

				case ItemID.MUD_BATTLESTAFF:
				case ItemID.MYSTIC_MUD_STAFF:
					equippedInfiniteRunes.add(RuneTypes.WATER);
					equippedInfiniteRunes.add(RuneTypes.EARTH);
					break;

				case ItemID.DUST_BATTLESTAFF:
				case ItemID.MYSTIC_DUST_STAFF:
					equippedInfiniteRunes.add(RuneTypes.EARTH);
					equippedInfiniteRunes.add(RuneTypes.AIR);
					break;
			}
		}

		return equippedInfiniteRunes;

	}

	private int getEquipmentMagicBonus(){
		final ItemContainer container = client.getItemContainer(InventoryID.EQUIPMENT);

		if (container == null)
		{
			return -1;
		}

		Item[] items = container.getItems();

		int magicBonus = 0;

		for (Item item: items) {
			if (item.getId() > 512)
			{
				ItemStats stats = itemManager.getItemStats(item.getId(), false);
				if (stats != null)
				{
					magicBonus += stats.getEquipment().getAmagic();
				}
			}
		}

		return magicBonus;
	}

	private boolean autocastAvailable()
	{
		int equippedWeaponVarBit = client.getVar(Varbits.EQUIPPED_WEAPON_TYPE);

		AttackStyle[] attackStyles = WeaponType.getWeaponType(equippedWeaponVarBit).getAttackStyles();

		for (AttackStyle style : attackStyles)
		{
			if (style == AttackStyle.CASTING)
			{
				return true;
			}
		}
		return false;
	}

	private boolean autocastOn()
	{
		int attackStyleVarBit = client.getVar(VarPlayer.ATTACK_STYLE);
		int equippedWeaponVarBit = client.getVar(Varbits.EQUIPPED_WEAPON_TYPE);

		AttackStyle[] attackStyles = WeaponType.getWeaponType(equippedWeaponVarBit).getAttackStyles();
		if (attackStyleVarBit >= attackStyles.length)
		{
			// We don't have a valid read, return
			return false;
		}

		if (attackStyles[attackStyleVarBit] != AttackStyle.CASTING)
		{
			return false;
		}

		return true;
	}
}
